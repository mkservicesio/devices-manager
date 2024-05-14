package gr.esdalab.atlas.devices.adapters.persitence.impl;

import gr.esdalab.atlas.devices.adapters.persitence.*;
import gr.esdalab.atlas.devices.adapters.persitence.entities.*;
import gr.esdalab.atlas.devices.adapters.persitence.entities.embeddable.NetworkLayerPrimary;
import gr.esdalab.atlas.devices.common.DeviceStatus;
import gr.esdalab.atlas.devices.common.errors.ErrorMessage;
import gr.esdalab.atlas.devices.core.application.exceptions.AtlasException;
import gr.esdalab.atlas.devices.core.domain.Device;
import gr.esdalab.atlas.devices.core.domain.Health;
import gr.esdalab.atlas.devices.core.domain.NetworkAdapter;
import gr.esdalab.atlas.devices.core.domain.common.Appendage;
import gr.esdalab.atlas.devices.core.domain.common.Datatype;
import gr.esdalab.atlas.devices.core.domain.DomainError;
import gr.esdalab.atlas.devices.core.domain.sensors.impl.DataSensorDevice;
import gr.esdalab.atlas.devices.core.domain.sensors.SensorDevice;
import gr.esdalab.atlas.devices.core.domain.repositories.DevicesRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DevicesRepositoryImpl implements DevicesRepository{

    private final DevicesJpaRepository devicesJpaRepo;
    private final DeviceDatatypeJpaRepository deviceDatatypeJpaRepo;
    private final DatatypesJpaRepository datatypesJpaRepo;
    private final AppendagesJpaRepository appendagesJpaRepo;
    private final NetworkInterfacesJpaRepository networkIfsJpaRepo;

    /**
     *
     * @param deviceId
     * @return
     */
    @Override
    @Transactional
    public Optional<SensorDevice> getDevice(final int deviceId) {
        return devicesJpaRepo.findById(deviceId).map(this::map);
    }

    /**
     *
     * @param deviceId
     * @return
     */
    @Override
    public Optional<SensorDevice> getDeviceByUrn(@NonNull final String deviceId) {
        return devicesJpaRepo.findTop1ByIdentity(deviceId).map(this::map);
    }

    /**
     *
     * @param device
     * @return
     */
    @Override
    @Transactional
    public SensorDevice create(@NonNull final SensorDevice device) {
        if( device instanceof DataSensorDevice ){
            return createDataSensorDevice((DataSensorDevice)device);
        }
        throw new AtlasException.MisConfigurationException(new DomainError.MisConfiguredDomainError("Device Creation type is not allowed. Available creation type are: 'DataSensorDevice'."));
    }

    @Override
    @Transactional
    public void updateThresholds(@NonNull final DataSensorDevice.CreatedDataSensorDevice sensorDevice) {
        final DeviceJpa deviceJpa = devicesJpaRepo.findById(sensorDevice.getId()).orElseThrow(() -> {
            throw new AtlasException.ResourceNotExistException(new DomainError.NotFoundDomainError(ErrorMessage.DEVICE_NOT_EXIST.getMessage()));
        });
        sensorDevice.getDatatypes().stream()
                .forEach(datatype -> {
                    final DatatypeJpa datatypeJpa = datatypesJpaRepo.findById(datatype.getId()).orElseThrow(() -> {
                        throw new AtlasException.ResourceNotExistException(new DomainError.NotFoundDomainError(ErrorMessage.DATATYPE_NOT_EXIST.getMessage()));
                    });
                    deviceDatatypeJpaRepo.save(DeviceDatatypeJpa.from(deviceJpa,datatypeJpa,datatype.getThreshold().asString()));
                });
    }

    @Override
    @Transactional
    public void online(final int deviceId) {
        devicesJpaRepo.findById(deviceId)
                .ifPresentOrElse(device -> devicesJpaRepo.save(device.online()), () -> {
                    throw new AtlasException.ResourceNotExistException(new DomainError.NotFoundDomainError(ErrorMessage.DEVICE_NOT_EXIST.getMessage()));
                });
    }

    @Override
    @Transactional
    public void online(@NonNull final DataSensorDevice.OnlineDataSensorDevice sensorDevice) {
        devicesJpaRepo.findById(sensorDevice.getId()).ifPresent(it -> {
            final DeviceJpa updDeviceJpa = it.online();
            networkIfsJpaRepo.deleteByNetworkLayerId(updDeviceJpa.getNetwork().getId());
            final List<NetworkLayerInterfaceJpa> networkInterfaces = sensorDevice.getAdapters()
                    .stream()
                    .map(interf -> new NetworkLayerInterfaceJpa(
                            new NetworkLayerPrimary(NetworkLayerPrimary.NetworkTechnologyType.valueOf(interf.getTechnology()), updDeviceJpa.getNetwork().getId()),
                            updDeviceJpa.getNetwork(),
                            interf.getAddress()
                    ))
                    .collect(Collectors.toList());
            networkIfsJpaRepo.saveAll(networkInterfaces);
            devicesJpaRepo.save(updDeviceJpa);
        });
    }

    @Override
    public List<SensorDevice> getDevicesByAppendageId(final int appendageId) {
        return devicesJpaRepo.findByAppendageId(appendageId)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<SensorDevice> getDevices() {
        return devicesJpaRepo.getAll()
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    /**
     * Create a data sensor device.
     * @param device
     * @return
     */
    private SensorDevice createDataSensorDevice(@NonNull final DataSensorDevice device) {
        final AppendageJpa appendageJpa = appendagesJpaRepo.getOne(device.getAppendage().getAppendageId());
        final DeviceJpa deviceJpa = devicesJpaRepo.save(DeviceJpa.draft(appendageJpa, device));
        deviceDatatypeJpaRepo.saveAll(device.getDatatypes().stream().map(datatype -> {
            final DatatypeJpa datatypeJpa = datatypesJpaRepo.getOne(datatype.getId());
            return DeviceDatatypeJpa.from(deviceJpa, datatypeJpa, datatypeJpa.getThreshold());
        }).collect(Collectors.toList()));
        return new DataSensorDevice.CreatedDataSensorDevice(
                deviceJpa.getId(),
                deviceJpa.getIdentity(),
                deviceJpa.getMode().getType(),
                deviceJpa.getMode().getMode(),
                Appendage.from(deviceJpa.getAppendage()),
                deviceJpa.getCoordinates(),
                device.getDatatypes(),
                device.getHealth()
        );
    }

    /**
     * Mapper class
     * @param deviceJpa
     * @return
     */
    private SensorDevice map(@NonNull final DeviceJpa deviceJpa){
        if( deviceJpa.getStatus() == DeviceStatus.GenericDeviceStatus.CONNECTED ){
            final List<NetworkLayerInterfaceJpa> commInterfaces = networkIfsJpaRepo.findByNetworkLayer(deviceJpa.getNetwork());
            return new DataSensorDevice.OnlineDataSensorDevice(
                    deviceJpa.getId(),
                    deviceJpa.getIdentity(),
                    deviceJpa.getMode().getType(),
                    deviceJpa.getMode().getMode(),
                    Appendage.from(deviceJpa.getAppendage()),
                    deviceJpa.getCoordinates(),
                    deviceDatatypeJpaRepo.findByDevice(deviceJpa)
                            .stream()
                            .map(datatype -> new Datatype(datatype.getDatatype().getId(), datatype.getDatatype().getDatatype(), Datatype.Threshold.from(datatype.getThreshold())))
                            .collect(Collectors.toList()),
                    commInterfaces.stream().map(NetworkAdapter.AdapterFactory::of).collect(Collectors.toList()),
                    new Health.Device(deviceJpa.getOnlineAt(), deviceJpa.getStatus())
            );
        }
        return new DataSensorDevice.CreatedDataSensorDevice(
                deviceJpa.getId(),
                deviceJpa.getIdentity(),
                deviceJpa.getMode().getType(),
                deviceJpa.getMode().getMode(),
                Appendage.from(deviceJpa.getAppendage()),
                deviceJpa.getCoordinates(),
                deviceDatatypeJpaRepo.findByDevice(deviceJpa)
                        .stream()
                        .map(datatype -> new Datatype(datatype.getDatatype().getId(), datatype.getDatatype().getDatatype(), Datatype.Threshold.from(datatype.getThreshold())))
                        .collect(Collectors.toList()),
                new Health.Device(deviceJpa.getOnlineAt(), deviceJpa.getStatus())
        );
    }

}
