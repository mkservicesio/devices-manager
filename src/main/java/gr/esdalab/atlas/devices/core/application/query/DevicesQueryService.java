package gr.esdalab.atlas.devices.core.application.query;

import gr.esdalab.atlas.devices.adapters.rest.resources.out.devices.DeviceOutput;
import gr.esdalab.atlas.devices.common.resources.in.HealthInput;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

/**
 * Service for query devices information.
 */
public interface DevicesQueryService {

    /**
     * Get Specific Robot by its internal ID.
     * @param deviceId
     * @return
     */
    Optional<DeviceOutput> getDevice(int deviceId, @Nullable HealthInput healthInput);

    /**
     * Get Specific Robot by its identity
     * @param deviceId
     * @return
     */
    Optional<DeviceOutput> getDevice(String deviceId);

    /**
     *  List of available devices per appendage.
     * @param appendageId
     * @return
     */
    List<DeviceOutput> getDevicesByAppendageId(int appendageId);

    /**
     * List Available devices.
     * @return
     */
    List<DeviceOutput> listDevices();
}
