package gr.esdalab.atlas.devices.adapters.rest.resources.inout;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.NotNull;

/**
 *
 */
@Getter
public class VehicleCommandPartInputOutput{

    /**
     * The field of the vehicle command
     */
    @NotNull
    private final String field;

    /**
     * The value of the field.
     */
    @NotNull
    private final String value;

    /**
     * Required arguments constructor
     * JSON Creator.
     */
    @JsonCreator
    public VehicleCommandPartInputOutput(@JsonProperty("field") final String field,
                                         @JsonProperty("value") final String value){
        this.field = field;
        this.value = value;
    }

}
