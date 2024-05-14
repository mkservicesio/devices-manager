package gr.esdalab.atlas.devices.core.domain.repositories;

import gr.esdalab.atlas.devices.core.domain.common.Appendage;
import gr.esdalab.atlas.devices.core.domain.Device;
import gr.esdalab.atlas.devices.core.domain.locations.impl.OutdoorLocation;

/**
 * Re-presenting a repository for locations.
 */
public interface OutdoorLocationsRepository {

    /**
     *
     * @param appendage
     * @param outdoor
     * @return
     */
    OutdoorLocation.CreatedOutdoorLocation<? extends Device> create(Appendage appendage, OutdoorLocation<? extends Device> outdoor);

}
