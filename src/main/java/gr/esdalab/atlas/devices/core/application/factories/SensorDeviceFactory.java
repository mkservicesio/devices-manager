package gr.esdalab.atlas.devices.core.application.factories;

import gr.esdalab.atlas.devices.common.utils.ApplicationUtils;
import gr.esdalab.atlas.devices.core.domain.common.Appendage;
import gr.esdalab.atlas.devices.core.domain.common.Datatype;
import gr.esdalab.atlas.devices.core.domain.Device;
import gr.esdalab.atlas.devices.core.domain.sensors.impl.DataSensorDevice;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

/**
 * A Factory for Sensor Devices
 */
@RequiredArgsConstructor
public class SensorDeviceFactory {
    private static final String SENSOR_IDENTIFIER = "sensor:";
    private final String urnPrefix;

    /**
     * Generating a Sensor Device.
     * @return
     */
    public DataSensorDevice generate(@NonNull final Appendage appendage,
                                     @NonNull final String sensorType,
                                     @NonNull final List<Datatype> dataTypes){
        final String urn = urnPrefix+sensorType+SENSOR_IDENTIFIER+ApplicationUtils.getUniqueIdentifier();
        return new DataSensorDevice(urn, Device.DeviceType.REAL, Device.DeviceMode.PRODUCTION, appendage, Collections.emptyList(), dataTypes, null);
    }

}
