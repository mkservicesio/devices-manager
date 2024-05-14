package gr.esdalab.atlas.devices.core.domain;

import gr.esdalab.atlas.devices.common.DeviceStatus;
import lombok.Value;

import java.util.Date;

/**
 * Mark inteface for Health information
 */
public interface Health {

    /**
     * Health implementation for device.
     */
    @Value
    class Device implements Health{
        Date onlineAt;
        DeviceStatus status;
    }

}
