package gr.esdalab.atlas.devices.adapters.rest.resources.inout;

import gr.esdalab.atlas.devices.common.utils.ApplicationUtils;
import gr.esdalab.atlas.devices.core.domain.common.Datatype;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Threshold Input/Output class.
 */
@Getter
@RequiredArgsConstructor
public class ThresholdInputOutput {

    @NotNull(message = "error.datatype.threshold.comparison.required")
    @Pattern(regexp = "^(LT|LTE|EQ|NEQ|GT|GTE)$", message = "error.datatype.threshold.comparison.specific")
    private final String comparison;

    @NotNull(message = "error.datatype.threshold.value.required")
    @Pattern(regexp = "^-?[0-9]{0,10}\\.?[0-9]{1,10}?$", message = "error.datatype.threshold.value.only.numbers")
    private final String value;

    /**
     * Mapper
     * @param threshold
     * @return
     */
    public static ThresholdInputOutput from(@NonNull final Datatype.Threshold threshold) {
        return new ThresholdInputOutput(threshold.getComparison().name(), threshold.getValue());
    }

    /**
     * Mapper
     * @param threshold
     * @return
     */
    public static ThresholdInputOutput from(@NonNull final String threshold) {
        final String[] thresholdSpl = threshold.split(ApplicationUtils.THRESHOLD_SEPARATOR);
        return new ThresholdInputOutput(thresholdSpl[ApplicationUtils.THRESHOLD_COMPARISON_PART], thresholdSpl[ApplicationUtils.THRESHOLD_VALUE_PART]);
    }

}
