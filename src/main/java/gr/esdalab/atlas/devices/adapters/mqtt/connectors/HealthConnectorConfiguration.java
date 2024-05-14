package gr.esdalab.atlas.devices.adapters.mqtt.connectors;

import gr.esdalab.atlas.devices.adapters.mqtt.handlers.DevicesHealthMonitoringHandler;
import gr.esdalab.atlas.devices.adapters.mqtt.handlers.HealthMonitoringHandler;
import gr.esdalab.atlas.devices.common.utils.AdapterUtils;
import gr.esdalab.atlas.devices.core.application.cases.EdgeMonitoringUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.ExecutorChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.validation.constraints.Null;

@ConditionalOnProperty(prefix = "mqtt.connector", name = "enabled", havingValue = "on")
@Configuration
@Slf4j
public class HealthConnectorConfiguration {

    private static final int DEFAULT_CHANNELS_POOL = 3;
    private static final String EDGE_HEALTH_TOPIC = "gateways/+/health/monitor";
    private static final String EDGE_DEVICES_HEALTH_TOPIC = "devices/+/health/monitor";
    private static final int DEFAULT_QOS = 0;
    private static final String HEALTH_CLIENT_SUFFIX = ".health@";
    private static final String DEVICES_HEALTH_CLIENT_SUFFIX = ".health-dev@";

    /**
     * Application Name
     */
    private final String mqttClientPrefix;
    private final String mqttDevicesClientPrefix;

    public HealthConnectorConfiguration(@Value("${spring.application.name}") String appName){
        this.mqttClientPrefix = appName + HEALTH_CLIENT_SUFFIX;
        this.mqttDevicesClientPrefix = appName + DEVICES_HEALTH_CLIENT_SUFFIX;
    }

    @Bean
    public MessageChannel healthInboundChannel() {
        ThreadPoolTaskExecutor inboundExecutor = new ThreadPoolTaskExecutor();
        inboundExecutor.setCorePoolSize(DEFAULT_CHANNELS_POOL);
        inboundExecutor.initialize();
        return new ExecutorChannel(inboundExecutor);
    }

    @Bean
    public MessageChannel devicesHealthInboundChannel() {
        ThreadPoolTaskExecutor inboundExecutor = new ThreadPoolTaskExecutor();
        inboundExecutor.setCorePoolSize(DEFAULT_CHANNELS_POOL);
        inboundExecutor.initialize();
        return new ExecutorChannel(inboundExecutor);
    }

    @Bean(name="gateway-health-mqtt-connector")
    public MessageProducer gatewayInboundConnector(MqttPahoClientFactory mqttPahoClientFactory) {
        log.info("Initializing MQTT Connector(health) - Connector topics: {}", EDGE_HEALTH_TOPIC);
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(AdapterUtils.MQTTAdapterUtils.generateClientId(mqttClientPrefix), mqttPahoClientFactory, EDGE_HEALTH_TOPIC);
        DefaultPahoMessageConverter converter = new DefaultPahoMessageConverter();
        converter.setPayloadAsBytes(true);
        adapter.setConverter(converter);
        adapter.setQos(DEFAULT_QOS);
        adapter.setOutputChannel(healthInboundChannel());
        return adapter;
    }

    @Bean(name="devices-health-mqtt-connector")
    public MessageProducer devicesInboundConnector(MqttPahoClientFactory mqttPahoClientFactory) {
        log.info("Initializing MQTT Connector(health) - Connector topics: {}", EDGE_DEVICES_HEALTH_TOPIC);
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(AdapterUtils.MQTTAdapterUtils.generateClientId(mqttDevicesClientPrefix), mqttPahoClientFactory, EDGE_DEVICES_HEALTH_TOPIC);
        DefaultPahoMessageConverter converter = new DefaultPahoMessageConverter();
        converter.setPayloadAsBytes(true);
        adapter.setConverter(converter);
        adapter.setQos(DEFAULT_QOS);
        adapter.setOutputChannel(devicesHealthInboundChannel());
        return adapter;
    }

    @Bean(name="healthMonitoringHandler")
    @ServiceActivator(inputChannel="healthInboundChannel")
    public MessageHandler healthMonitoringHandler(@Null final EdgeMonitoringUseCase edgeMonitoringUseCase) {
        return new HealthMonitoringHandler(edgeMonitoringUseCase);
    }

    @Bean(name="devicesHealthMonitoringHandler")
    @ServiceActivator(inputChannel="devicesHealthInboundChannel")
    public MessageHandler devicesHealthMonitoringHandler(@Null final EdgeMonitoringUseCase edgeMonitoringUseCase) {
        return new DevicesHealthMonitoringHandler(edgeMonitoringUseCase);
    }

}
