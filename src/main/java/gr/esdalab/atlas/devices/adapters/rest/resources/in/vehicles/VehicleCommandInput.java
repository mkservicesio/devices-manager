package gr.esdalab.atlas.devices.adapters.rest.resources.in.vehicles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import gr.esdalab.atlas.devices.adapters.rest.resources.inout.VehicleCommandPartInputOutput;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * Command Input
 */
@Getter
public class VehicleCommandInput {

    /**
     * Type of the command.
     */
    @NotNull
    private final String type;

    /**
     * The parts of the command.
     */
    @NotNull
    @NotEmpty
    private final List<VehicleCommandPartInputOutput> parts;

    /**
     * Required arguments constructor
     * JSON Creator.
     * @param type
     */
    @JsonCreator
    public VehicleCommandInput(@JsonProperty("type") final String type,
                               @JsonProperty("parts") final List<VehicleCommandPartInputOutput> parts){
        this.type = type;
        this.parts = parts;
    }

}
