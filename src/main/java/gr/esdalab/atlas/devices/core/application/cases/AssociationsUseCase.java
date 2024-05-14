package gr.esdalab.atlas.devices.core.application.cases;

import gr.esdalab.atlas.devices.core.domain.vehicles.Vehicle;

/**
 *
 */
public interface AssociationsUseCase {

    /**
     * Associate a location with a specific vehicle.
     * @param locationId
     * @param association
     */
    void associationLocationWith(int locationId, Vehicle.ActiveVehicle association);

    /**
     * Delete
     * @param locationId
     */
    void cleanUpForLocation(int locationId);

    /**
     * Remove an association.
     * @param locationId
     * @param association
     */
    void removeLocationAssociationWith(int locationId, Vehicle.ActiveVehicle association);
}
