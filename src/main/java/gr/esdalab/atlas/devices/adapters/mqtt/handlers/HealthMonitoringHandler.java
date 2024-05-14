package gr.esdalab.atlas.devices.adapters.mqtt.handlers;

import gr.esdalab.atlas.devices.core.application.cases.EdgeMonitoringUseCase;
import gr.esdalab.atlas.devices.common.resources.in.HealthInput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
public class HealthMonitoringHandler implements MessageHandler {

    private static final int MQTT_GATEWAY_IDENTIFIER_INDEX = 2;
    private final Pattern PATTERN = Pattern.compile("(gateways\\/emqx\\@)([a-zA-Z0-9\\.]+)(\\/health\\/monitor)");
    private final EdgeMonitoringUseCase edgeMonitoringUseCase;

    /**
     * Handle a HealthDto Message.
     * @param message
     * @throws MessagingException
     */
    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        String msgTopic = (String)message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC);
        Matcher matcher = PATTERN.matcher(msgTopic);
        if(matcher.matches()) {
            HealthInput.HealthDtoDeSerializer.get((byte[]) message.getPayload()).ifPresent(healthInput -> {
                log.info("Message [Topic: " + msgTopic + "] Gateway["+matcher.group(MQTT_GATEWAY_IDENTIFIER_INDEX)+"], Received: " + healthInput);
                edgeMonitoringUseCase.healthCheck(matcher.group(MQTT_GATEWAY_IDENTIFIER_INDEX), healthInput);
            });
        }else{
            log.warn("Health Monitoring topic not matched!!! Topic: {}", msgTopic);
        }
    }

}
