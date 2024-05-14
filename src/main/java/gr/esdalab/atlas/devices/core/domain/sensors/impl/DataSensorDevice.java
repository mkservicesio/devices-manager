package gr.esdalab.atlas.devices.core.domain.sensors.impl;

import gr.esdalab.atlas.devices.core.domain.Coordinates;
import gr.esdalab.atlas.devices.core.domain.Health;
import gr.esdalab.atlas.devices.core.domain.NetworkAdapter;
import gr.esdalab.atlas.devices.core.domain.common.Appendage;
import gr.esdalab.atlas.devices.core.domain.common.Datatype;
import gr.esdalab.atlas.devices.core.domain.sensors.SensorDevice;
import lombok.Getter;
import lombok.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
public class DataSensorDevice extends SensorDevice.SensingDevice implements SensorDevice{

    private final List<Datatype> datatypes;
    /**
     * Required Arguments Constructor
     *
     * @param urn
     * @param type
     * @param mode
     */
    public DataSensorDevice(@NonNull final String urn,
                            @NonNull final DeviceType type,
                            @NonNull final DeviceMode mode,
                            @NonNull final Appendage appendage,
                            @NonNull final List<Coordinates> coordinates,
                            @NonNull final List<Datatype> datatypes,
                            final Health.Device health) {
        super(urn, type, mode, appendage, coordinates, health);
        this.datatypes = datatypes;
    }

    @Getter
    public static class CreatedDataSensorDevice extends DataSensorDevice{
        private final int id;

        /**
         * Required Arguments Constructor
         *
         * @param urn
         * @param type
         * @param mode
         * @param datatypes
         */
        public CreatedDataSensorDevice(final int id,
                                       @NonNull final String urn,
                                       @NonNull final DeviceType type,
                                       @NonNull final DeviceMode mode,
                                       @NonNull final Appendage appendage,
                                       @NonNull final List<Coordinates> coordinates,
                                       @NonNull final List<Datatype> datatypes,
                                       final Health.Device health) {
            super(urn, type, mode, appendage, coordinates, datatypes, health);
            this.id = id;
        }

        /**
         *
         * @param device
         * @param threshold
         * @param datatypeId
         * @return
         */
        public static CreatedDataSensorDevice withThresholdOnDatatype(@NonNull final CreatedDataSensorDevice device,
                                                              @NonNull final Datatype.Threshold threshold,
                                                              final int datatypeId) {
            final List<Datatype> datatypes = device.getDatatypes().stream().map(datatype -> {
                if( datatype.getId() == datatypeId ){
                    return Datatype.withThreshold(datatype, threshold);
                }
                return datatype;
            }).collect(Collectors.toList());
            return new CreatedDataSensorDevice(device.getId(), device.getUrn(), device.getType(), device.getMode(), device.getAppendage(), device.getCoordinates(), datatypes, device.getHealth());
        }
    }

    /**
     * Re-presents a Device that is online.
     */
    @Getter
    public static class OnlineDataSensorDevice extends CreatedDataSensorDevice{
        private final List<NetworkAdapter> adapters;

        /**
         * Required Arguments Constructor
         *
         * @param id
         * @param urn
         * @param type
         * @param mode
         * @param appendage
         * @param coordinates
         * @param datatypes
         * @param adapters
         * @param health
         */
        public OnlineDataSensorDevice(final int id,
                                      @NonNull final String urn,
                                      @NonNull final DeviceType type,
                                      @NonNull final DeviceMode mode,
                                      @NonNull final Appendage appendage,
                                      @NonNull final List<Coordinates> coordinates,
                                      @NonNull final List<Datatype> datatypes,
                                      @NonNull final List<NetworkAdapter> adapters,
                                      final Health.Device health) {
            super(id, urn, type, mode, appendage, coordinates, datatypes, health);
            this.adapters = adapters;
        }

        /**
         * Generate a Online Data Sensor Device from a device.
         * @param device
         * @return
         */
        public static OnlineDataSensorDevice from(@NonNull final CreatedDataSensorDevice device,
                                                  @NonNull final List<NetworkAdapter> adapters) {
            return new OnlineDataSensorDevice(
                    device.getId(),
                    device.getUrn(),
                    device.getType(),
                    device.getMode(),
                    device.getAppendage(),
                    device.getCoordinates(),
                    device.getDatatypes(),
                    adapters,
                    device.getHealth()
            );
        }
    }

}
