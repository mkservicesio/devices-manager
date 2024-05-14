package gr.esdalab.atlas.devices.core.application.cases;

import gr.esdalab.atlas.devices.adapters.rest.resources.in.LocationInput;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.LocationOutput;

import java.util.Optional;

/**
 * Locations Related Use Case.
 */
public interface LocationsUseCase {

    /**
     *
     * @param location
     * @return
     */
    Optional<LocationOutput> create(LocationInput.DraftLocationInput location);

    /**
     * Delete location
     * @param locationId
     */
    void delete(int locationId);

    /**
     * Update an existing location
     * @param locationId
     * @param locationInput
     * @return
     */
    Optional<LocationOutput> update(int locationId, LocationInput.CreatedLocationInput locationInput);
}
