package gr.esdalab.atlas.devices.adapters.mqtt;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

@ConditionalOnProperty(prefix = "mqtt.connector", name = "enabled", havingValue = "on")
@MessagingGateway(name="MQTTOutputGateway", defaultRequestChannel="atlasOutboundChannel")
public interface MQTTOutputGateway {

    /**
     * Forwarding a message throw the MQTT connector
     * @param topic - The topic to forward the message
     * @param data - The actual data.
     * @return
     */
    void publish(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) int qos, byte[] data);

}
