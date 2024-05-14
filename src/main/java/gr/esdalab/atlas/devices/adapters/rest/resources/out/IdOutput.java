package gr.esdalab.atlas.devices.adapters.rest.resources.out;

import com.fasterxml.jackson.annotation.JsonCreator;
import gr.esdalab.atlas.devices.adapters.rest.resources.inout.ThresholdInputOutput;
import gr.esdalab.atlas.devices.core.domain.NetworkAdapter;
import gr.esdalab.atlas.devices.core.domain.common.Datatype;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * A generic class that return at least the ID of ATLAS domain object along with a label.
 */
@RequiredArgsConstructor
@Getter
public abstract class IdOutput<T> {

    /**
     * The ID of the ATLAS object.
     */
    @Schema(anyOf = {String.class, Integer.class}, example = "1", required = true, description = "Unique identifier for the object.")
    private final T id;

    /**
     * The label of the ATLAS Object.
     */
    private final String label;

    /**
     * Datatype Output
     */
    public static class DatatypeOutput extends IdOutput<Integer>{

        /**
         * Default Constructor
         * @param id
         * @param label
         */
        public DatatypeOutput(final int id,
                              @NonNull final String label) {
            super(id, label);
        }
    }

    /**
     * Datatype Threshold output.
     */
    @Getter
    public static class DatatypeThresholdOutput extends IdOutput<Integer>{

        private final ThresholdInputOutput threshold;

        /**
         * Default Constructor
         * @param id
         * @param label
         */
        @JsonCreator
        public DatatypeThresholdOutput(final int id,
                                       @NonNull final String label,
                                       @NonNull final ThresholdInputOutput threshold) {
            super(id, label);
            this.threshold = threshold;
        }

        public static DatatypeThresholdOutput from(@NonNull final Datatype datatype) {
            return new DatatypeThresholdOutput(
                    datatype.getId(),
                    datatype.getLabel(),
                    ThresholdInputOutput.from(datatype.getThreshold())
            );
        }
    }

}
