package gr.esdalab.atlas.devices.adapters.rest.resources.out;

import gr.esdalab.atlas.devices.core.domain.vehicles.impl.OilVehicle;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BasicVehicleOutput {

    /**
     * The ATLAS ID
     */
    private final int id;

    /**
     * The Owner ID
     */
    private final int ownerId;

    /**
     * The label of the vehicle.
     */
    private final String label;

    /**
     * Vehicle Status
     */
    private final String status;

    /**
     * Vehicles Compartment
     */
    private final int compartments;

    /**
     *
     * @param vehicle
     * @return
     */
    public static BasicVehicleOutput from(@NonNull final OilVehicle.CreatedOilVehicle vehicle) {
        return new BasicVehicleOutput(vehicle.getId(), vehicle.getOwner().getOwnerId(), vehicle.getLabel(), vehicle.getStatus().name(), vehicle.getCompartments().size());
    }
}
