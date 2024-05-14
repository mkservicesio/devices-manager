package gr.esdalab.atlas.devices.core.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import gr.esdalab.atlas.devices.adapters.rest.resources.inout.CoordinatesInOut;
import lombok.Getter;
import lombok.NonNull;

import java.io.Serializable;

/**
 * Interface re-representing coordinates of an object/device.
 */
public interface Coordinates {

    /**
     *  The type of the coordinates.
     * @return
     */
    String getType();

    String CARTESIAN_COORDINATES_IDENTIFIER = "cartesian";
    String GPS_COORDINATES_IDENTIFIER = "gps";

    /**
     * Cartesian Coordinates Type
     */
    @Getter
    class CartesianCoordinates implements Coordinates{
        private final float x;
        private final float y;

        @JsonCreator
        public CartesianCoordinates(@JsonProperty("x") final float x,
                                    @JsonProperty("y") final float y){
            this.x = x;
            this.y = y;
        }

        @Override
        @JsonIgnore
        public String getType() {
            return CARTESIAN_COORDINATES_IDENTIFIER;
        }
    }

    /**
     * GPS Coordinates Type
     */
    @Getter
    class GPSCoordinates implements Coordinates, Serializable {
        private final double latitude;
        private final double longitude;
        private final double altitude;

        @JsonCreator
        public GPSCoordinates(@JsonProperty("latitude") final double latitude,
                              @JsonProperty("longitude") final double longitude,
                              @JsonProperty("altitude") final double altitude){
            this.latitude = latitude;
            this.longitude = longitude;
            this.altitude = altitude;
        }

        @Override
        @JsonIgnore
        public String getType() {
            return GPS_COORDINATES_IDENTIFIER;
        }

        /**
         *
         * @param gpsInput
         * @return
         */
        public static GPSCoordinates from(@NonNull final CoordinatesInOut.GPSInOut gpsInput) {
            return new GPSCoordinates(gpsInput.getLatitude(), gpsInput.getLongitude(), gpsInput.getAltitude());
        }
    }

}
