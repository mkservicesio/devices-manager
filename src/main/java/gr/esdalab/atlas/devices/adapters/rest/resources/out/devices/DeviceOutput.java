package gr.esdalab.atlas.devices.adapters.rest.resources.out.devices;

import com.fasterxml.jackson.annotation.JsonInclude;
import gr.esdalab.atlas.devices.adapters.rest.resources.inout.CoordinatesInOut;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.IdOutput;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.networking.NetworkAdapterOutput;
import gr.esdalab.atlas.devices.common.DeviceStatus;
import gr.esdalab.atlas.devices.core.domain.Coordinates;
import gr.esdalab.atlas.devices.core.domain.Health;
import gr.esdalab.atlas.devices.core.domain.sensors.impl.DataSensorDevice;
import gr.esdalab.atlas.devices.core.domain.sensors.impl.PresenceSensorDevice;
import lombok.*;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeviceOutput {

    /**
     * The ATLAS Device ID.
     */
    private final int id;

    /**
     * Device URN
     */
    private final String urn;

    /**
     * Device Type
     */
    @NonNull
    private final String type;

    /**
     * Device Mode
     */
    @NonNull
    private final String mode;

    /**
     * GPS Location
     */
    @Nullable
    private final CoordinatesInOut.GPSInOut location;

    /**
     * Cartesian Location
     */
    @Nullable
    private final CoordinatesInOut.CartesianInOut virtual;

    /**
     *  List of available datatypes of device.
     */
    private final List<IdOutput.DatatypeThresholdOutput> datatypes;

    /**
     * The appendage identifier of the device.
     */
    private final DeviceAppendageOutput appendage;

    /**
     * Last online.
     */
    private final Date onlineAt;

    /**
     * Status of the device.
     */
    private final String status;

    /**
     * A list of available networking
     */
    private final List<NetworkAdapterOutput> networks;


    /**
     * Simplified version of appendage for device.
     */
    public static class DeviceAppendageOutput extends IdOutput<Integer>{

        /**
         *  Required arguments constructor.
         */
        public DeviceAppendageOutput(final int appendageId){
            super(appendageId, "-");
        }
    }

    /**
     * Constructs a data related sensor.
     * @param device
     * @return
     */
    public static DeviceOutput from(@NonNull final DataSensorDevice.CreatedDataSensorDevice device) {
        return DeviceOutput.builder()
                .id(device.getId())
                .urn(device.getUrn())
                .type(device.getType().name())
                .mode(device.getMode().name())
                .location(device.getCoordinates()
                        .stream()
                        .filter(cord -> cord instanceof Coordinates.GPSCoordinates)
                        .map(cord -> (Coordinates.GPSCoordinates) cord)
                        .map(CoordinatesInOut.GPSInOut::from)
                        .findFirst().orElse(null)
                )
                .virtual(device.getCoordinates()
                        .stream()
                        .filter(cord -> cord instanceof Coordinates.CartesianCoordinates)
                        .map(cord -> (Coordinates.CartesianCoordinates) cord)
                        .map(CoordinatesInOut.CartesianInOut::from)
                        .findFirst().orElse(null)
                )
                .datatypes(device.getDatatypes()
                        .stream()
                        .map(IdOutput.DatatypeThresholdOutput::from)
                        .collect(Collectors.toList())
                )
                .appendage(new DeviceAppendageOutput(device.getAppendage().getAppendageId()))
                .onlineAt(Optional.ofNullable(device.getHealth()).map(Health.Device::getOnlineAt).orElse(null))
                .status(device.getHealth().getStatus().toString())
                .networks(Collections.emptyList())
                .build();
    }

    /**
     * Constructs a data related sensor.
     * @param device
     * @return
     */
    public static DeviceOutput from(@NonNull final DataSensorDevice.OnlineDataSensorDevice device) {
        return DeviceOutput.builder()
                .id(device.getId())
                .urn(device.getUrn())
                .type(device.getType().name())
                .mode(device.getMode().name())
                .location(device.getCoordinates()
                        .stream()
                        .filter(cord -> cord instanceof Coordinates.GPSCoordinates)
                        .map(cord -> (Coordinates.GPSCoordinates) cord)
                        .map(CoordinatesInOut.GPSInOut::from)
                        .findFirst().orElse(null)
                )
                .virtual(device.getCoordinates()
                        .stream()
                        .filter(cord -> cord instanceof Coordinates.CartesianCoordinates)
                        .map(cord -> (Coordinates.CartesianCoordinates) cord)
                        .map(CoordinatesInOut.CartesianInOut::from)
                        .findFirst().orElse(null)
                )
                .datatypes(device.getDatatypes()
                        .stream()
                        .map(IdOutput.DatatypeThresholdOutput::from)
                        .collect(Collectors.toList())
                )
                .appendage(new DeviceAppendageOutput(device.getAppendage().getAppendageId()))
                .onlineAt(Optional.ofNullable(device.getHealth()).map(Health.Device::getOnlineAt).orElse(null))
                .status(device.getHealth().getStatus().toString())
                .networks(device.getAdapters().stream().map(NetworkAdapterOutput::from).collect(Collectors.toList()))
                .build();
    }

    /**
     * Constructs a presence related sensor.
     * @param device
     * @return
     */
    public static DeviceOutput from(@NonNull final PresenceSensorDevice.CreatedPresenceSensorDevice device) {
        return DeviceOutput.builder()
                .id(device.getId())
                .urn(device.getUrn())
                .type(device.getType().name())
                .mode(device.getMode().name())
                .location(device.getCoordinates()
                        .stream()
                        .filter(cord -> cord instanceof Coordinates.GPSCoordinates)
                        .map(cord -> (Coordinates.GPSCoordinates) cord)
                        .map(CoordinatesInOut.GPSInOut::from)
                        .findFirst().orElse(null)
                )
                .virtual(device.getCoordinates()
                        .stream()
                        .filter(cord -> cord instanceof Coordinates.CartesianCoordinates)
                        .map(cord -> (Coordinates.CartesianCoordinates) cord)
                        .map(CoordinatesInOut.CartesianInOut::from)
                        .findFirst().orElse(null)
                )
                .appendage(new DeviceAppendageOutput(device.getAppendage().getAppendageId()))
                .onlineAt(Optional.ofNullable(device.getHealth()).map(Health.Device::getOnlineAt).orElse(null))
                .status(DeviceStatus.GenericDeviceStatus.NOT_CONNECTED.name())
                .build();
    }
}
