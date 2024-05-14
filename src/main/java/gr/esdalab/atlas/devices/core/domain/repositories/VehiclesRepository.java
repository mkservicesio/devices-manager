package gr.esdalab.atlas.devices.core.domain.repositories;

import gr.esdalab.atlas.devices.core.application.query.VehiclesQueryService;
import gr.esdalab.atlas.devices.core.domain.common.Appendage;
import gr.esdalab.atlas.devices.core.domain.common.PageableOf;
import gr.esdalab.atlas.devices.core.domain.locations.impl.OutdoorLocation;
import gr.esdalab.atlas.devices.core.domain.vehicles.impl.OilVehicle;
import gr.esdalab.atlas.devices.core.domain.vehicles.Vehicle;

import java.util.Optional;

/**
 * Re-presenting a repository for vehicles.
 */
public interface VehiclesRepository {

    /**
     * Generate a return a vehicle
     * @return
     */
    OilVehicle.CreatedOilVehicle addVehicle(Appendage appendage, OilVehicle vehicle);

    /**
     *
     * @param vehicle
     */
    void update(Vehicle.VehicleInfo vehicle);

    /**
     * Get Vehicle by its ID.
     * @return
     */
    Optional<OilVehicle.CreatedOilVehicle> getVehicle(int vehicleId);

    /**
     * Get Vehicle by its label
     * @return - The oil vehicle.
     */
    Optional<OilVehicle.CreatedOilVehicle> getVehicle(String label);

    /**
     * List Vehicles
     * @return
     */
    PageableOf<OilVehicle.CreatedOilVehicle> getVehicles(final VehiclesQueryService.VehicleQuery query);

    /**
     *
     * @param vehicle
     */
    void process(Vehicle.ActiveVehicle vehicle);

    /**
     *
     * @param vehicle
     */
    void process(Vehicle.InActiveVehicle vehicle);

    /**
     *
     * @param vehicle
     */
    void addPointOfInterest(Vehicle.ActiveVehicle vehicle, OutdoorLocation.CreatedOutdoorLocation<?> location);

    /**
     *
     * @param locationId
     */
    void cleanAssociationsByLocation(int locationId);

    /**
     *
     * @param association
     * @param location
     */
    void removePointOfInterest(Vehicle.ActiveVehicle association, OutdoorLocation.CreatedOutdoorLocation<?> location);
}
