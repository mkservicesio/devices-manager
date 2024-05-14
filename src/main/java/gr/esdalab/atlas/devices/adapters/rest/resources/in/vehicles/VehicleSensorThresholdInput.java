package gr.esdalab.atlas.devices.adapters.rest.resources.in.vehicles;

import gr.esdalab.atlas.devices.adapters.rest.resources.inout.ThresholdInputOutput;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
public class VehicleSensorThresholdInput {

    /**
     * The compartment index
     */
    private final int compartment;

    /**
     * The Device ID
     */
    private final int device;

    /**
     * The datatype ID
     */
    private final int datatype;

    /**
     * Threshold value.
     */
    @NotNull(message = "error.vehicles.sensor.threshold.required")
    private final ThresholdInputOutput threshold;

}
