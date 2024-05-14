package gr.esdalab.atlas.devices.core.application.query.impl;

import gr.esdalab.atlas.devices.common.resources.in.HealthInput;
import gr.esdalab.atlas.devices.core.application.exceptions.AtlasException;
import gr.esdalab.atlas.devices.core.application.query.AppendagesQueryService;
import gr.esdalab.atlas.devices.core.application.query.DevicesQueryService;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.devices.DeviceOutput;
import gr.esdalab.atlas.devices.core.domain.DomainError;
import gr.esdalab.atlas.devices.core.domain.events.MonitorEvent;
import gr.esdalab.atlas.devices.core.domain.sensors.SensorDevice;
import gr.esdalab.atlas.devices.core.domain.sensors.impl.DataSensorDevice;
import gr.esdalab.atlas.devices.core.domain.sensors.impl.PresenceSensorDevice;
import gr.esdalab.atlas.devices.core.domain.repositories.DevicesRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of {@DevicesQueryService}
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DevicesQueryServiceImpl implements DevicesQueryService {

    private final DevicesRepository devicesRepo;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Optional<DeviceOutput> getDevice(final int deviceId,
                                            @Nullable final HealthInput healthInput) {
        return devicesRepo.getDevice(deviceId).map(device -> {

            if( device instanceof DataSensorDevice.OnlineDataSensorDevice){
                Optional.ofNullable(healthInput).ifPresent(health -> {
                    eventPublisher.publishEvent(new MonitorEvent.DeviceMonitorEvent<>(this, (DataSensorDevice.OnlineDataSensorDevice)device));
                });
                return DeviceOutput.from((DataSensorDevice.OnlineDataSensorDevice)device);
            }else if( device instanceof DataSensorDevice.CreatedDataSensorDevice){
                Optional.ofNullable(healthInput).ifPresent(health -> {
                    eventPublisher.publishEvent(new MonitorEvent.DeviceMonitorEvent<>(this, (DataSensorDevice.CreatedDataSensorDevice)device));
                });
                return DeviceOutput.from((DataSensorDevice.CreatedDataSensorDevice)device);
            } else if( device instanceof PresenceSensorDevice.CreatedPresenceSensorDevice){
                Optional.ofNullable(healthInput).ifPresent(health -> {
                    eventPublisher.publishEvent(new MonitorEvent.DeviceMonitorEvent<>(this, (PresenceSensorDevice.CreatedPresenceSensorDevice)device));
                });
                return DeviceOutput.from((PresenceSensorDevice.CreatedPresenceSensorDevice)device);
            }
            throw new AtlasException.MisConfigurationException(new DomainError.MisConfiguredDomainError("Device instance is not handled correctly."));
        });
    }

    @Override
    public Optional<DeviceOutput> getDevice(@NonNull final String deviceId) {
        return devicesRepo.getDeviceByUrn(deviceId).map(device -> {
            if( device instanceof DataSensorDevice.OnlineDataSensorDevice){
                eventPublisher.publishEvent(new MonitorEvent.DeviceMonitorEvent<>(this, (DataSensorDevice.OnlineDataSensorDevice)device));
                return DeviceOutput.from((DataSensorDevice.OnlineDataSensorDevice)device);
            }else if( device instanceof DataSensorDevice.CreatedDataSensorDevice){
                eventPublisher.publishEvent(new MonitorEvent.DeviceMonitorEvent<>(this, (DataSensorDevice.CreatedDataSensorDevice)device));
                return DeviceOutput.from((DataSensorDevice.CreatedDataSensorDevice)device);
            }else if( device instanceof PresenceSensorDevice.CreatedPresenceSensorDevice){
                eventPublisher.publishEvent(new MonitorEvent.DeviceMonitorEvent<>(this, (PresenceSensorDevice.CreatedPresenceSensorDevice)device));
                return DeviceOutput.from((PresenceSensorDevice.CreatedPresenceSensorDevice)device);
            }
            throw new AtlasException.MisConfigurationException(new DomainError.MisConfiguredDomainError("Device instance is not handled correctly."));
        });
    }

    @Override
    public List<DeviceOutput> getDevicesByAppendageId(final int appendageId) {
        return devicesRepo.getDevicesByAppendageId(appendageId).stream()
                .map(this::map)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeviceOutput> listDevices() {
        return devicesRepo.getDevices().stream()
                .map(this::map)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Nullable
    private DeviceOutput map(@NonNull final SensorDevice device){
        if( device instanceof DataSensorDevice.OnlineDataSensorDevice){
            return DeviceOutput.from((DataSensorDevice.OnlineDataSensorDevice)device);
        }else if( device instanceof DataSensorDevice.CreatedDataSensorDevice){
            return DeviceOutput.from((DataSensorDevice.CreatedDataSensorDevice)device);
        }else if( device instanceof PresenceSensorDevice.CreatedPresenceSensorDevice){
            return DeviceOutput.from((PresenceSensorDevice.CreatedPresenceSensorDevice)device);
        }
        log.warn("Unspecified implementation of device: {}", device.getClass());
        return null;
    }

}
