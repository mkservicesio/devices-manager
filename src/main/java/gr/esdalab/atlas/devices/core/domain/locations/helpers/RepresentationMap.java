package gr.esdalab.atlas.devices.core.domain.locations.helpers;

import gr.esdalab.atlas.devices.adapters.persitence.entities.LocationMapJpa;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Representation Map
 */
@Getter
@RequiredArgsConstructor
public class RepresentationMap {

    private final String identity;
    private final int width;
    private final int height;
    private final double resolution;

    /**
     *
     * @param locationMapJpa
     * @return
     */
    public static RepresentationMap from(@NonNull final LocationMapJpa locationMapJpa) {
        return new RepresentationMap(locationMapJpa.getLocation().getIdentity(), locationMapJpa.getWidth(), locationMapJpa.getHeight(), locationMapJpa.getResolution());
    }
}
