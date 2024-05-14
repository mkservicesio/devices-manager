package gr.esdalab.atlas.devices.core.application.query;

import gr.esdalab.atlas.devices.adapters.rest.resources.out.IdOutput;

import java.util.Optional;

/**
 * Service for query generic information
 */
public interface GenericQueryService {

    /**
     * Return the datatype
     * @return
     */
    Optional<IdOutput.DatatypeOutput> getDatatype(String datatype);

}
