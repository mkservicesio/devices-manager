package gr.esdalab.atlas.devices.adapters.rest.resources.in.vehicles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Input Event for generating a new vehicle.
 */
@Getter
@RequiredArgsConstructor
public abstract class VehicleInput {

    /**
     * The owner of the Vehicle.
     */
    private final int owner;

    /**
     * The user-friendly name of the vehicle.
     */
    @NotNull(message = "error.vehicles.label.required")
    private final String label;

    /**
     * Input for the generation of a vehicle.
     */
    @Getter
    public static class VehicleCreationInput extends VehicleInput{

        /**
         * The compartments of the vehicle.
         */
        private final int compartments;

        @JsonCreator
        public VehicleCreationInput(@JsonProperty("owner") final int owner,
                                    @JsonProperty("label") final String label,
                                    @JsonProperty("compartments") final int compartments){
            super(owner, label);
            this.compartments = compartments;
        }
    }

    /**
     * Update Input of the vehicle.
     */
    @Getter
    public static class VehicleUpdateInput extends VehicleInput{

        @JsonCreator
        public VehicleUpdateInput(@JsonProperty("owner") final int owner,
                                  @JsonProperty("label") final String label){
            super(owner, label);
        }
    }

}
