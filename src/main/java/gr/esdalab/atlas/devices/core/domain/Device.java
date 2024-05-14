package gr.esdalab.atlas.devices.core.domain;

import gr.esdalab.atlas.devices.core.domain.common.Appendage;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Null;
import java.util.List;
import java.util.Optional;

@Getter
public abstract class Device {

    /**
     * ATLAS Unique URN
     */
    private final String urn;

    /**
     * The type of the device.
     */
    private final DeviceType type;

    /**
     * The device mode.
     */
    private final DeviceMode mode;

    /**
     * Appendage of the device.
     */
    private final Appendage appendage;

    /**
     * The coordinates of the device.
     */
    private final List<Coordinates> coordinates;

    /**
     *  Health information about the device.
     */
    private final Health.Device health;

    /**
     * ATLAS Device Constructor
     * @param urn
     * @param type
     * @param mode
     * @param coordinates
     */
    protected Device(@NonNull final String urn,
                     @NonNull final DeviceType type,
                     @NonNull final DeviceMode mode,
                     @NonNull final Appendage appendage,
                     @NonNull final List<Coordinates> coordinates,
                     final Health.Device health) {
        this.urn = urn;
        this.type = type;
        this.mode = mode;
        this.appendage = appendage;
        assert coordinates.size() < 3 : "A device up to 2 coordinates";
        assert coordinates.stream().filter(cord -> cord instanceof Coordinates.GPSCoordinates).count() < 2 : "A device can have only ONE GPS coordinate.";
        assert coordinates.stream().filter(cord -> cord instanceof Coordinates.CartesianCoordinates).count() < 2 : "A device can have only ONE Cartesian coordinate.";
        this.coordinates = coordinates;
        this.health = health;
    }

    /**
     * Device Types.
     */
    public enum DeviceType{

        /**
         * Real Device
         */
        REAL,

        /**
         * Simulated Device.
         */
        SIMULATED;
    }

    /**
     * Device Mode
     */
    public enum DeviceMode{

        /**
         * Running in the real production system.
         */
        PRODUCTION,

        /**
         * In a CI/CD Pipeline the devices can be in this environment.
         */
        STAGING,

        /**
         * Used for training
         */
        TRAINING,

        /**
         * Developing application using this device.
         */
        DEVELOPMENT;

    }

}
