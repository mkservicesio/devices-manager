package gr.esdalab.atlas.devices.core.domain.repositories;

import gr.esdalab.atlas.devices.core.application.query.LocationsQueryService;
import gr.esdalab.atlas.devices.core.domain.common.PageableOf;
import gr.esdalab.atlas.devices.core.domain.locations.Location;
import gr.esdalab.atlas.devices.core.domain.locations.helpers.RepresentationMap;

import java.util.Optional;

/**
 * Generic Locations Repository
 */
public interface LocationsRepository {

    /**
     *
     * @param locationId
     * @return
     */
    Optional<Location> getLocation(int locationId);

    /**
     * Update location
     * @param locationId
     * @return
     */
    Optional<Location> update(int locationId, Location location);

    /**
     *
     * @param locationId
     * @return
     */
    Optional<RepresentationMap> getLocationMap(int locationId);

    /**
     * Delete a location.
     * @param locationId
     */
    void deleteById(int locationId);

    /**
     * List Locations
     * @param query
     * @return
     */
    PageableOf<Location> getLocations(LocationsQueryService.LocationQuery query);

    /**
     *
     * @param appendageId
     * @return
     */
    Optional<Location> getLocationOfAppendage(int appendageId);
}
