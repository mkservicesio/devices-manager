package gr.esdalab.atlas.devices.core.domain.common;

import gr.esdalab.atlas.devices.adapters.rest.resources.inout.ThresholdInputOutput;
import gr.esdalab.atlas.devices.common.utils.ApplicationUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Pattern;

/**
 * Sensor Datatype re-presentation.
 */
@Getter
@RequiredArgsConstructor
public class Datatype {

    private final int id;
    private final String label;
    private final Threshold threshold;

    /**
     *
     * @param datatype
     * @param threshold
     * @return
     */
    public static Datatype withThreshold(@NonNull final Datatype datatype, @NonNull final Threshold threshold){
        return new Datatype(datatype.getId(), datatype.getLabel(), threshold);
    }

    @Getter
    @RequiredArgsConstructor
    public static class Threshold{

        private final Comparison comparison;

        @Pattern(regexp = "^-?[0-9]{0,10}\\.?[0-9]{1,10}?$", message = "error.datatype.threshold.value.only.numbers")
        private final String value;

        /**
         * Mapper of the threshold
         * @param threshold
         * @return
         */
        public static Threshold from(@NonNull final String threshold) {
            final String[] thresholdSpl = threshold.split(ApplicationUtils.THRESHOLD_SEPARATOR);
            return new Threshold(Comparison.valueOf(thresholdSpl[ApplicationUtils.THRESHOLD_COMPARISON_PART]), thresholdSpl[ApplicationUtils.THRESHOLD_VALUE_PART]);
        }

        /**
         * Mapper
         * @param threshold
         * @return
         */
        public static Threshold from(@NonNull final ThresholdInputOutput threshold) {
            return new Threshold(Comparison.valueOf(threshold.getComparison()), threshold.getValue());
        }

        public String asString() {
            return this.getComparison().name() + ApplicationUtils.THRESHOLD_SEPARATOR + getValue();
        }
    }

    /**
     * Threshold Comparison
     */
    public enum Comparison {
        LT, // Less than
        LTE, // Less than or Equal
        EQ, // Equal
        NEQ, // Not Equal
        GT, // Greater than
        GTE; // Greater than or Equal
    }

}
