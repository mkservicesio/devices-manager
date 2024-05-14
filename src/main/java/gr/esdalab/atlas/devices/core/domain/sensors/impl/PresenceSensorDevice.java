package gr.esdalab.atlas.devices.core.domain.sensors.impl;

import gr.esdalab.atlas.devices.core.domain.Coordinates;
import gr.esdalab.atlas.devices.core.domain.Health;
import gr.esdalab.atlas.devices.core.domain.common.Appendage;
import gr.esdalab.atlas.devices.core.domain.sensors.SensorDevice;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

@Getter
public class PresenceSensorDevice extends SensorDevice.SensingDevice implements SensorDevice{

    /**
     * Required Arguments Constructor
     * @param urn
     * @param type
     * @param mode
     */
    public PresenceSensorDevice(@NonNull final String urn,
                                @NonNull final DeviceType type,
                                @NonNull final DeviceMode mode,
                                @NonNull final Appendage appendage,
                                @NonNull final List<Coordinates> coordinates,
                                final Health.Device health) {
        super(urn, type, mode, appendage, coordinates, health);
    }

    @Getter
    public static class CreatedPresenceSensorDevice extends PresenceSensorDevice {
        private final int id;

        /**
         * Required Arguments Constructor
         *
         * @param urn
         * @param type
         * @param mode
         */
        public CreatedPresenceSensorDevice(final int id,
                                           @NonNull final String urn,
                                           @NonNull final DeviceType type,
                                           @NonNull final DeviceMode mode,
                                           @NonNull final Appendage appendage,
                                           @NonNull final List<Coordinates> coordinates,
                                           final Health.Device health) {
            super(urn, type, mode, appendage, coordinates, health);
            this.id = id;
        }
    }

}
