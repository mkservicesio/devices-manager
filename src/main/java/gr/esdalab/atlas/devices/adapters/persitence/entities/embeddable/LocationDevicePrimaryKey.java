package gr.esdalab.atlas.devices.adapters.persitence.entities.embeddable;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class LocationDevicePrimaryKey implements Serializable {

    /**
     * Location ID
     */
    private int locationId;

    /**
     * Device ID
     */
    private int attachedDeviceid;

}
