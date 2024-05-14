package gr.esdalab.atlas.devices.adapters.rest.resources.out;

import gr.esdalab.atlas.devices.adapters.rest.resources.inout.CoordinatesInOut;
import gr.esdalab.atlas.devices.core.domain.locations.impl.OutdoorLocation;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * View Re-presenting a point of interest.
 */
@Getter
@RequiredArgsConstructor
public class PointOfInterestOutput {

    /**
     * The location identifier.
     */
    private final int locationId;

    /**
     * A label for the user.
     */
    private final String label;

    /**
     * The coordinates for the point of interest.
     */
    private final List<CoordinatesInOut.GPSInOut> coordinates;

    /**
     * Mapper
     * @param createdOutdoorLocation
     * @return
     */
    public static PointOfInterestOutput from(@NonNull final OutdoorLocation.CreatedOutdoorLocation<?> createdOutdoorLocation) {
        return new PointOfInterestOutput(createdOutdoorLocation.getLocationId(),
                createdOutdoorLocation.getLabel(),
                createdOutdoorLocation.getCoordinates().stream().map(CoordinatesInOut.GPSInOut::from).collect(Collectors.toList())
        );
    }
}
