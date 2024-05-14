package gr.esdalab.atlas.devices.adapters.persitence.entities.embeddable;

import gr.esdalab.atlas.devices.core.domain.Device;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

/**
 * Representing the device mode, mainly the stage of the device.
 */
@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class DeviceModeIdentity implements Serializable {

    /**
     * The type of the device
     */
    @Enumerated(EnumType.STRING)
    private Device.DeviceType type;


    /**
     * The mode of the device.
     */
    @Enumerated(EnumType.STRING)
    private Device.DeviceMode mode;

}
