package gr.esdalab.atlas.devices.core.application.query;

import gr.esdalab.atlas.devices.adapters.rest.resources.out.IdOutput;

import java.util.List;
import java.util.Optional;

public interface DatatypesQueryService {


    /**
     * 
     * @return
     */
    List<IdOutput.DatatypeThresholdOutput> getDatatypes();

    /**
     *
     * @param datatypeId
     * @return
     */
    Optional<IdOutput.DatatypeThresholdOutput> getDatatype(int datatypeId);
}
