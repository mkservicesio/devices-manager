package gr.esdalab.atlas.devices.core.domain.vehicles;

import gr.esdalab.atlas.devices.adapters.persitence.entities.VehicleJpa;
import gr.esdalab.atlas.devices.core.domain.gateways.Gateway;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Re-presents a Vehicle
 */
@Getter
public abstract class Vehicle{

    /**
     * These devices are related to the vehicle only.
     * E.g For an OilVehicle the devices of the compartments are different.
     */
    private final List<Gateway.CreatedGateway> gateways;

    /**
     * The Owner of the vehicle.
     */
    private final VehicleOwner owner;

    /**
     * The user-friendly name of the vehicle.
     */
    private final String label;

    /**
     * Vehicle Status
     */
    private final VehicleStatus status;

    /**
     *
     * @param gateways
     * @param owner
     */
    public Vehicle(@NonNull final List<Gateway.CreatedGateway> gateways,
                   final VehicleOwner owner,
                   @NonNull final String label,
                   @NonNull final VehicleStatus status) {
        this.gateways = gateways;
        this.owner = owner;
        this.label = label;
        this.status = status;
    }

    /**
     * Re-presents a vehicle owner.
     */
    @Getter
    @RequiredArgsConstructor
    public static class VehicleOwner{
        /**
         * ATLAS Owner ID
         */
        private final int ownerId;
    }

    /**
     * Re-presents an active Vehicle
     */
    @Getter
    @RequiredArgsConstructor
    public static class ActiveVehicle{
        private final int vehicleId;
    }

    /**
     * Re-presents an in-active Vehicle
     */
    @Getter
    @RequiredArgsConstructor
    public static class InActiveVehicle{
        private final int vehicleId;
    }

    /**
     * Re-presents an active Vehicle
     */
    @Getter
    @RequiredArgsConstructor
    public static class VehicleInfo{
        private final int vehicleId;
        private final int ownerId;
        private final String label;
    }

    public enum VehicleStatus{
        INACTIVE, //Inactive Vehicle.
        ACTIVE, //Active Vehicle.
        ON_ROUTE; //Vehicle is on route.

        /**
         *
         * @param status
         * @return
         */
        public static VehicleStatus from(@NonNull final VehicleJpa.VehicleStatus status){
            return VehicleStatus.valueOf(status.name());
        }

    }

}
