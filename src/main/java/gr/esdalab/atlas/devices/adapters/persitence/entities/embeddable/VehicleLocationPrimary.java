package gr.esdalab.atlas.devices.adapters.persitence.entities.embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleLocationPrimary implements Serializable {
    private static final long serialVersionUID = 4272458234723L;

    /**
     * The Vehicle of the POI.
     */
    private int vehicleId;

    /**
     * The location associated with the vehicle.
     */
    private int locationId;

}
