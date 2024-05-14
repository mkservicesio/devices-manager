package gr.esdalab.atlas.devices.adapters.rest.resources.in;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

/**
 * Generic input class for defining association between different system domain objects
 * This class must only be used as nested class.
 */
@Getter
public class AssociationInput {

    /**
     * Contains a list of associated vehicles.
     */
    private final List<Integer> vehicles;


    @JsonCreator
    public AssociationInput(@JsonProperty("vehicles") final List<Integer> vehicles){
        this.vehicles = vehicles;
    }

}
