package gr.esdalab.atlas.devices.core.domain.repositories;

import gr.esdalab.atlas.devices.core.domain.common.Appendage;
import gr.esdalab.atlas.devices.core.domain.common.Datatype;

import java.util.List;

/**
 * Re-presenting a repository for common functionalities.
 */
public interface CommonRepository {

    /**
     *
     * @return
     */
    Appendage getCommonAppendage();

    /**
     * List available datatypes of the system.
     * @return
     */
    List<Datatype> getDatatypes();

    /**
     * Return
     * @param locationId
     * @return
     */
    Appendage getCommonAppendageOfLocation(int locationId);
}
