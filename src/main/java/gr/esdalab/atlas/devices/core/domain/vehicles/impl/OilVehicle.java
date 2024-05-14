package gr.esdalab.atlas.devices.core.domain.vehicles.impl;

import gr.esdalab.atlas.devices.adapters.persitence.entities.VehicleJpa;
import gr.esdalab.atlas.devices.core.domain.gateways.Gateway;
import gr.esdalab.atlas.devices.core.domain.locations.impl.OutdoorLocation;
import gr.esdalab.atlas.devices.core.domain.sensors.impl.DataSensorDevice;
import gr.esdalab.atlas.devices.core.domain.vehicles.Vehicle;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
public class OilVehicle extends Vehicle {

    private final List<Compartment> compartments;

    /**
     *
     * @param gateways
     * @param owner
     * @param compartments
     */
    public OilVehicle(@NonNull final List<Gateway.CreatedGateway> gateways,
                      final VehicleOwner owner,
                      @NonNull final String label,
                      @NonNull final VehicleStatus status,
                      @NonNull final List<Compartment> compartments) {
        super(gateways, owner, label, status);
        this.compartments = compartments;
    }

    /**
     * A created oil vehicle.
     */
    @Getter
    public static class CreatedOilVehicle extends OilVehicle{

        /**
         * The ID of the vehicle.
         */
        private final int id;

        /**
         * Locations which are related to OilVehicle
         */
        private final List<OutdoorLocation.CreatedOutdoorLocation<?>> locations;

        /**
         * @param gateways
         * @param owner
         * @param compartments
         */
        public CreatedOilVehicle(final int id,
                                 @NonNull final List<Gateway.CreatedGateway> gateways,
                                 final VehicleOwner owner,
                                 @NonNull final String label,
                                 @NonNull final VehicleStatus status,
                                 @NonNull final List<Compartment> compartments,
                                 final List<OutdoorLocation.CreatedOutdoorLocation<?>> locations) {
            super(gateways, owner, label, status, compartments);
            this.id = id;
            this.locations = locations;
        }

        /**
         * Mapper
         * @return
         */
        public static CreatedOilVehicle from(@NonNull final VehicleJpa vehicleJpa){
            return new CreatedOilVehicle(vehicleJpa.getId(),
                    Collections.emptyList(), // Empty because at this point, we don't want information about the gateway.
                    new VehicleOwner(vehicleJpa.getOwner()),
                    vehicleJpa.getLabel(),
                    VehicleStatus.from(vehicleJpa.getStatus()),
                    IntStream.range(0, vehicleJpa.getCompartments()).mapToObj(Compartment::ofIndex).collect(Collectors.toList()),
                    Collections.emptyList() //Empty because we want only the basic output of the vehicle.
            );
        }

        /**
         * Mapper
         * @return
         */
        public static CreatedOilVehicle from(@NonNull final VehicleJpa vehicleJpa,
                                             final List<OutdoorLocation.CreatedOutdoorLocation<?>> locations,
                                             @NonNull final List<Compartment> compartments){
            return new CreatedOilVehicle(vehicleJpa.getId(),
                    Collections.emptyList(), // Empty because at this point, we don't want information about the gateway.
                    new VehicleOwner(vehicleJpa.getOwner()),
                    vehicleJpa.getLabel(),
                    VehicleStatus.from(vehicleJpa.getStatus()),
                    compartments,
                    locations
            );
        }

    }

    /**
     * Oil Vehicle Compartment.
     */
    @Getter
    @RequiredArgsConstructor
    public static class Compartment{

        /**
         * The index number of the compartment.
         */
        private final int idx;

        /**
         * The sensor of this device.
         */
        private final List<DataSensorDevice.CreatedDataSensorDevice> sensors;

        /**
         * Return only the compartment index.
         * @param index
         * @return
         */
        public static Compartment ofIndex(final int index){
            return new Compartment(index, Collections.emptyList());
        }

    }

}
