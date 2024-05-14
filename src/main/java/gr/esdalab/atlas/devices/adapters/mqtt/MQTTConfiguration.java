package gr.esdalab.atlas.devices.adapters.mqtt;

import gr.esdalab.atlas.devices.common.utils.AdapterUtils;
import lombok.Getter;
import org.apache.commons.lang3.RandomStringUtils;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.ExecutorChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * MQTT Configuration
 */
@ConditionalOnProperty(prefix = "mqtt.connector", name = "enabled", havingValue = "on")
@Configuration
public class MQTTConfiguration {

    private static final int DEFAULT_CHANNELS_POOL = 5;
    private static final int DEFAULT_QOS = 0;
    private static final String GENERATED_KEYSTORE_PASSWORD = "";
    private static final String TLS_VERSION = "TLSv1.2";
    private static final String CERTIFICATE_TYPE = "X.509";
    private static final String PRIVATE_KEY_ALIAS = "atlas-prv-key";
    private static final String CA_CERTIFICATE_ALIAS = "ca-atlas-crt";
    private static final String CERTIFICATE_ALIAS = "atlas-crt";
    private static final String DEFAULT_MQTT_CLIENT_SUBSCRIPTION = "atlas/services/health";
    private static final String DEFAULT_MQTT_CLIENT_SEPARATOR = "@";
    private ResourceLoader resourceLoader;

    /**
     * MQTT Client username
     */
    private final String username;

    /**
     * MQTT Client password
     */
    private final String password;

    /**
     * MQTT Client connection hosts.
     */
    private final String hosts;

    /**
     * MQTT SSL Connection
     */
    private final boolean isSsl;

    /**
     * Application Name
     */
    private final String appName;

    private final String caCertPath;
    private final String clientCertPath;
    private final String clientKeyPath;

    @Getter
    private final String mgrRepliesTopic;

    public MQTTConfiguration(@Value("${spring.application.name}") String appName,
                             @Value("${mqtt.credentials.username}") String username,
                             @Value("${mqtt.credentials.password}") String password,
                             @Value("${mqtt.hosts}") String hosts,
                             @Value("${mqtt.ssl}") String ssl,
                             @Value("${mqtt.atlas.certificate.ca:null}") String caCertPath,
                             @Value("${mqtt.atlas.certificate.client:null}") String clientCertPath,
                             @Value("${mqtt.atlas.key.private.client:null}") String clientKeyPath,
                             @Value("${mqtt.replies.topic}") String mgrRepliesTopic,
                             @Autowired ResourceLoader resourceLoader){
        this.appName = appName;
        this.username = username;
        this.password = password;
        this.hosts = hosts;
        this.isSsl = ssl.equals("on");
        this.caCertPath = caCertPath;
        this.clientCertPath = clientCertPath;
        this.clientKeyPath = clientKeyPath;
        this.mgrRepliesTopic = mgrRepliesTopic;
        this.resourceLoader = resourceLoader;
    }

    /**
     * Connection to a non-SSL host.
     * @return
     */
    @Bean
    public MqttPahoClientFactory mqttClientFactory(){
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{hosts});
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        if( this.isSsl ){
            try {
                options.setSocketFactory(getSecureSocketFactory());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean
    @ConditionalOnProperty(prefix = "mqtt", name = "ssl", havingValue = "on")
    public SSLSocketFactory getSecureSocketFactory() throws Exception{
        Security.addProvider(new BouncyCastleProvider());
        CertificateFactory certFactory = CertificateFactory.getInstance(CERTIFICATE_TYPE);

        // load CA certificate
        PEMParser pemParser = new PEMParser(new InputStreamReader(new ByteArrayInputStream(Files.readAllBytes(loadFile(caCertPath)))));
        X509Certificate caCert = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(((X509CertificateHolder)pemParser.readObject()).getEncoded()));
        pemParser.close();

        // load client certificate
        pemParser = new PEMParser(new InputStreamReader(new ByteArrayInputStream(Files.readAllBytes(loadFile(clientCertPath)))));
        X509Certificate cert = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(((X509CertificateHolder)pemParser.readObject()).getEncoded()));
        pemParser.close();

        // load client private key
        pemParser = new PEMParser(new InputStreamReader(new ByteArrayInputStream(Files.readAllBytes(loadFile(clientKeyPath)))));
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        PrivateKeyInfo prvKeyInfo = (PrivateKeyInfo)pemParser.readObject();
        KeyPair key = new KeyPair(null, converter.getPrivateKey(prvKeyInfo));
        pemParser.close();

        // CA certificate is used to authenticate server
        KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
        caKs.load(null, null);

        caKs.setCertificateEntry(CA_CERTIFICATE_ALIAS, caCert);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(caKs);

        // client key and certificates are sent to server so it can authenticate us
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null, null);
        ks.setCertificateEntry(CERTIFICATE_ALIAS, cert);
        ks.setKeyEntry(PRIVATE_KEY_ALIAS, key.getPrivate(), GENERATED_KEYSTORE_PASSWORD.toCharArray(), new java.security.cert.Certificate[]{cert});
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, GENERATED_KEYSTORE_PASSWORD.toCharArray());

        // finally, create SSL socket factory
        SSLContext context = SSLContext.getInstance(TLS_VERSION);
        context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        return context.getSocketFactory();
    }

    /**
     * Checking if path is absolute or not and return the java.Path
     * @param filepath
     * @return
     */
    private Path loadFile(String filepath) throws IOException {
        if(new File(filepath).isAbsolute()) return Paths.get(filepath);
        //Resource is on classpath then.
        return resourceLoader.getResource("classpath:"+filepath).getFile().toPath();
    }

    @Bean
    public MessageChannel defaultInboundChannel() {
        ThreadPoolTaskExecutor inboundExecutor = new ThreadPoolTaskExecutor();
        inboundExecutor.setCorePoolSize(DEFAULT_CHANNELS_POOL);
        inboundExecutor.initialize();
        return new ExecutorChannel(inboundExecutor);
    }


    @Bean(name="atlas-default-mqtt-connector")
    public MessageProducer gatewayInboundConnector() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(AdapterUtils.MQTTAdapterUtils.generateClientId(appName+DEFAULT_MQTT_CLIENT_SEPARATOR), mqttClientFactory(), DEFAULT_MQTT_CLIENT_SUBSCRIPTION);
        DefaultPahoMessageConverter converter = new DefaultPahoMessageConverter();
        converter.setPayloadAsBytes(true);
        adapter.setConverter(converter);
        adapter.setQos(DEFAULT_QOS);
        adapter.setOutputChannel(defaultInboundChannel());
        return adapter;
    }

    /**
     * Configure outbound MQTT connection
     */
    @Bean
    public MessageChannel atlasOutboundChannel() {
        ThreadPoolTaskExecutor outboundExecutor = new ThreadPoolTaskExecutor();
        outboundExecutor.setCorePoolSize(DEFAULT_CHANNELS_POOL);
        outboundExecutor.initialize();
        return new ExecutorChannel(outboundExecutor);
    }

    @Bean
    @ServiceActivator(inputChannel = "atlasOutboundChannel")
    public MessageHandler gatewayOutboundConnector(MqttPahoClientFactory mqttClientFactory){
        MqttPahoMessageHandler handler = new MqttPahoMessageHandler(AdapterUtils.MQTTAdapterUtils.generateClientId(appName+DEFAULT_MQTT_CLIENT_SEPARATOR), mqttClientFactory);
        handler.setAsync(true);
        handler.setAsyncEvents(true);
        return handler;
    }


}
