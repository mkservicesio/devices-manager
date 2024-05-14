package gr.esdalab.atlas.devices.core.domain.repositories;

import gr.esdalab.atlas.devices.core.domain.sensors.impl.DataSensorDevice;
import gr.esdalab.atlas.devices.core.domain.sensors.SensorDevice;

import java.util.List;
import java.util.Optional;

/**
 * Re-presenting a repository for devices.
 */
public interface DevicesRepository {

    /**
     * Return Device.
     * @return
     */
    Optional<SensorDevice> getDevice(int deviceId);

    /**
     * Return Device.
     * @return
     */
    Optional<SensorDevice> getDeviceByUrn(String deviceId);

    /**
     *
     * @param device
     * @return
     */
    SensorDevice create(SensorDevice device);

    /**
     * Update device thresholds
     * @param forUpdateDevice
     */
    void updateThresholds(DataSensorDevice.CreatedDataSensorDevice forUpdateDevice);

    /**
     *
     * @param deviceId
     */
    void online(int deviceId);

    /**
     * Online from a sensor device.
     * @param sensorDevice
     */
    void online(DataSensorDevice.OnlineDataSensorDevice sensorDevice);

    /**
     * List Devices of a specific appendage.
     * @param appendageId
     * @return
     */
    List<SensorDevice> getDevicesByAppendageId(int appendageId);

    /**
     * Get System Available Devices
     * @return
     */
    List<SensorDevice> getDevices();
}
