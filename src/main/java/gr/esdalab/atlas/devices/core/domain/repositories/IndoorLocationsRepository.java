package gr.esdalab.atlas.devices.core.domain.repositories;

import gr.esdalab.atlas.devices.core.domain.common.Appendage;
import gr.esdalab.atlas.devices.core.domain.locations.impl.IndoorLocation;

/**
 * Re-presenting a repository for locations.
 */
public interface IndoorLocationsRepository {

    /**
     * Create an indoor location
     * @param appendage
     * @param indoor
     * @return
     */
    IndoorLocation.CreatedIndoorLocation create(Appendage appendage, IndoorLocation indoor);

}
