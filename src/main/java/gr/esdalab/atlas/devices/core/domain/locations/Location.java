package gr.esdalab.atlas.devices.core.domain.locations;

import gr.esdalab.atlas.devices.core.domain.Coordinates;
import gr.esdalab.atlas.devices.core.domain.common.Appendage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Interface for Locations
 */
public interface Location {

    /**
     * Re-presents a physical location.
     */
    @RequiredArgsConstructor
    @Getter
    abstract class PhysicalLocation implements Location {

        /**
         * An identifier for location
         */
        private final String identifier;

        /**
         * A friendly name of the location.
         */
        private final String label;

        /**
         * The coordinates of the location.
         */
        private final List<Coordinates.GPSCoordinates> coordinates;

        /**
         * The common appendage.
         */
        private final Appendage appendage;

    }

}
