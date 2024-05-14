package gr.esdalab.atlas.devices.core.application.query;

import gr.esdalab.atlas.devices.adapters.rest.resources.out.AppendageOutput;

import java.util.Optional;

/**
 * Service for querying appendages.
 */
public interface AppendagesQueryService {

    /**
     * Return information about a specific appendage.
     * The information included are
     *  1. Locations
     *  2. Robots
     *  3. Gateways
     *  4. Devices
     * @return
     */
    Optional<AppendageOutput> getAppendage(int appendageId);

}
