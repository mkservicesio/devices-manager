package gr.esdalab.atlas.devices.core.domain.events.listeners.monitoring;

import gr.esdalab.atlas.devices.core.application.cases.EdgeMonitoringUseCase;
import gr.esdalab.atlas.devices.core.application.exceptions.AtlasException;
import gr.esdalab.atlas.devices.core.domain.DomainError;
import gr.esdalab.atlas.devices.core.domain.events.MonitorEvent;
import gr.esdalab.atlas.devices.core.domain.sensors.impl.DataSensorDevice;
import gr.esdalab.atlas.devices.core.domain.sensors.impl.PresenceSensorDevice;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MonitoringEventsListener implements ApplicationListener<MonitorEvent> {

    private final EdgeMonitoringUseCase edgeMonitoringUseCase;

    @Override
    public void onApplicationEvent(@NonNull final MonitorEvent event) {
        log.info("Monitoring event received. Event Type {}", event.getClass());
        if( event instanceof MonitorEvent.DeviceMonitorEvent){
            doHandle((MonitorEvent.DeviceMonitorEvent<?>)event);
            return;
        }else if( event instanceof MonitorEvent.RobotMonitorEvent){
            doHandle((MonitorEvent.RobotMonitorEvent)event);
            return;
        }
        throw new AtlasException.MisConfigurationException(new DomainError.MisConfiguredDomainError("Event type "+event.getClass()+" handling is not supported."));
    }

    /**
     * Handling Monitoring events.
     * @param event
     */
    private void doHandle(@NonNull final MonitorEvent.DeviceMonitorEvent<?> event) {
        if( event.getDevice() instanceof DataSensorDevice.CreatedDataSensorDevice){
            edgeMonitoringUseCase.deviceHealthCheck(((DataSensorDevice.CreatedDataSensorDevice)event.getDevice()).getId());
            return;
        }else if( event.getDevice() instanceof PresenceSensorDevice.CreatedPresenceSensorDevice){
            edgeMonitoringUseCase.deviceHealthCheck(((PresenceSensorDevice.CreatedPresenceSensorDevice)event.getDevice()).getId());
            return;
        }
        throw new AtlasException.MisConfigurationException(new DomainError.MisConfiguredDomainError("Device type "+event.getDevice().getClass()+" handling is not supported."));
    }

    /**
     * Handling Monitoring events.
     * @param event
     */
    private void doHandle(@NonNull final MonitorEvent.RobotMonitorEvent event) {
        edgeMonitoringUseCase.robotHealthCheck(event.getRobot().getId());
    }
}
