package gr.esdalab.atlas.devices.adapters.rest.resources.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import gr.esdalab.atlas.devices.core.domain.Coordinates;
import gr.esdalab.atlas.devices.core.domain.Device;
import gr.esdalab.atlas.devices.core.domain.locations.impl.IndoorLocation;
import gr.esdalab.atlas.devices.core.domain.locations.impl.OutdoorLocation;
import lombok.*;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LocationOutput {

    /**
     * Location ID
     */
    private final int locationId;

    /**
     * Location Identity
     */
    @NonNull
    private final String identity;

    /**
     * Location label
     */
    @NonNull
    private final String label;

    /**
     *  The GPS coordinates of the location.
     */
    private final List<Coordinates.GPSCoordinates> coordinates;

    /**
     * Appendage ID
     */
    private final int appendageId;

    /**
     *
     * @param location
     * @return
     */
    public static LocationOutput from(@NonNull final IndoorLocation.CreatedIndoorLocation location) {
        return new LocationOutput(location.getLocationId(),
                location.getIdentifier(),
                location.getLabel(),
                location.getCoordinates(),
                location.getAppendage().getAppendageId()
        );
    }

    /**
     *
     * @param location
     * @return
     */
    public static LocationOutput from(@NonNull final OutdoorLocation.CreatedOutdoorLocation<? extends Device> location) {
        return new LocationOutput(location.getLocationId(),
                location.getIdentifier(),
                location.getLabel(),
                location.getCoordinates(),
                location.getAppendage().getAppendageId()
        );
    }
}
