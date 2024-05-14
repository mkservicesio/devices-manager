package gr.esdalab.atlas.devices.adapters.rest.resources.out;

import gr.esdalab.atlas.devices.core.domain.locations.helpers.RepresentationMap;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Re-presents a location re-presentation information.
 */
@Getter
@RequiredArgsConstructor
public class LocationMapOutput {
    private final String identity;
    private final int width;
    private final int height;
    private final double resolution;

    /**
     *
     * @param map
     * @return
     */
    public static LocationMapOutput from(@NonNull final RepresentationMap map) {
        return new LocationMapOutput(map.getIdentity(), map.getWidth(), map.getHeight(), map.getResolution());
    }
}
