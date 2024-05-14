package gr.esdalab.atlas.devices.core.application.cases;

import gr.esdalab.atlas.devices.adapters.rest.resources.in.LocationInput;
import gr.esdalab.atlas.devices.adapters.rest.resources.in.vehicles.VehicleInput;
import gr.esdalab.atlas.devices.adapters.rest.resources.in.vehicles.VehicleSensorThresholdInput;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.vehicles.VehicleOutput;

import java.util.Optional;

/**
 * Use Case - Create a Vehicle.
 */
public interface VehiclesCreationUseCase {

    /**
     *
     * @param vehicle
     * @return
     */
    Optional<VehicleOutput> create(VehicleInput.VehicleCreationInput vehicle);

    /**
     * Activate Vehicle
     * @param vehicleId
     */
    void activate(int vehicleId);

    /**
     * De-Activate
     * @param vehicleId
     */
    void deactivate(int vehicleId);

    /**
     * Set Threshold to specific compartment device.
     * @param thresholdInput
     */
    void setThreshold(int vehicleId, VehicleSensorThresholdInput thresholdInput);

    /**
     * Update Vehicle.
     * @param vehicleInput
     * @return
     */
    void update(int vehicleId, VehicleInput.VehicleUpdateInput vehicleInput);

    /**
     * Add a new Point of Interest.
     * @param vehicleId
     * @param locationInput
     */
    void addPointOfInterest(int vehicleId, LocationInput.DraftLocationInput locationInput);

    /**
     * Remove an existing Point of Interest.
     * @param vehicleId
     * @param locationId
     */
    void removePointOfInterest(int vehicleId, int locationId);
}
