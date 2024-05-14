package gr.esdalab.atlas.devices.core.application.cases;

import gr.esdalab.atlas.devices.adapters.rest.resources.inout.ThresholdInputOutput;

/**
 * 
 */
public interface DatatypesUseCase {

    /**
     *
     * @param datatypeId
     * @param threshold
     * @return
     */
    void updateThreshold(int datatypeId, ThresholdInputOutput threshold);
}
