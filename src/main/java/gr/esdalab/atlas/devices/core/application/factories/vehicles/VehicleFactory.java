package gr.esdalab.atlas.devices.core.application.factories.vehicles;

import gr.esdalab.atlas.devices.adapters.rest.resources.in.vehicles.VehicleInput;
import gr.esdalab.atlas.devices.config.DevicesManagerConfig;
import gr.esdalab.atlas.devices.config.VehiclesConfig;
import gr.esdalab.atlas.devices.core.application.factories.SensorDeviceFactory;
import gr.esdalab.atlas.devices.core.domain.common.Appendage;
import gr.esdalab.atlas.devices.core.domain.common.Datatype;
import gr.esdalab.atlas.devices.core.domain.gateways.Gateway;
import gr.esdalab.atlas.devices.core.domain.repositories.CommonRepository;
import gr.esdalab.atlas.devices.core.domain.repositories.DevicesRepository;
import gr.esdalab.atlas.devices.core.domain.repositories.GatewaysRepository;
import gr.esdalab.atlas.devices.core.domain.sensors.impl.DataSensorDevice;
import gr.esdalab.atlas.devices.core.domain.vehicles.impl.OilVehicle;
import gr.esdalab.atlas.devices.core.domain.vehicles.Vehicle;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Factory class for vehicles.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class VehicleFactory {

    private static final int COMPARTMENT_START_INDEX = 0;

    private final GatewaysRepository gatewaysRepo;
    private final CommonRepository commonRepo;
    private final DevicesRepository devicesRepo;
    private final DevicesManagerConfig devicesManagerConfig;
    private final VehiclesConfig vehiclesConfig;


    /**
     *
     * @return
     */
    public OilVehicle create(@NonNull final Appendage appendage,
                             @NonNull final VehicleInput.VehicleCreationInput vehicle){
        final Gateway.CreatedGateway gateway = gatewaysRepo.createDefaultGateway(appendage);
        final List<OilVehicle.Compartment> compartments = IntStream.range(COMPARTMENT_START_INDEX,vehicle.getCompartments())
                .mapToObj(it -> VehicleCompartmentFactory.generate(it, generateCompartmentDevices(appendage)))
                .collect(Collectors.toList());
        return new OilVehicle(
                Collections.singletonList(gateway),
                new Vehicle.VehicleOwner(vehicle.getOwner()),
                vehicle.getLabel(),
                Vehicle.VehicleStatus.INACTIVE,
                compartments
        );

    }

    /**
     *  Generate the devices of the compartment.
     *  On each device a single datatype is attached.
     * @return
     */
    private List<DataSensorDevice.CreatedDataSensorDevice> generateCompartmentDevices(@NonNull final Appendage appendage){
        final SensorDeviceFactory factory = new SensorDeviceFactory(devicesManagerConfig.getAtlasUrnPrefix());
        final List<Datatype> datatypes = commonRepo.getDatatypes();
        return vehiclesConfig.getCompartmentSensors()
                .stream()
                .map(sensorType -> {
                    return (DataSensorDevice.CreatedDataSensorDevice) devicesRepo.create(factory.generate(appendage, sensorType, datatypes));
                })
                .collect(Collectors.toList());
    }

    /**
     * Internal Factory for the generation of compartment.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class VehicleCompartmentFactory{

        /**
         *
         * @param idx
         * @return
         */
        public static OilVehicle.Compartment generate(final int idx,
                                                      @NonNull final List<DataSensorDevice.CreatedDataSensorDevice> devices){
            return new OilVehicle.Compartment(idx, devices);
        }

    }

}
