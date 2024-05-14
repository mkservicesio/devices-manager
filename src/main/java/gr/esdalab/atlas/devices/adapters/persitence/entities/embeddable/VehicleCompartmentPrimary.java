package gr.esdalab.atlas.devices.adapters.persitence.entities.embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleCompartmentPrimary implements Serializable {
    private static final long serialVersionUID = 4272458234723L;

    /**
     * The Vehicle of the compartment
     */
    private int vehicleId;

    /**
     * The index of the compartment
     */
    private int compartmentIdx;

    /**
     * The device that has been attached to the compartment.
     */
    private int deviceId;

}
