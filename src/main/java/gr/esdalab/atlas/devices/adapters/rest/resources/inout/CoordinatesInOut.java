package gr.esdalab.atlas.devices.adapters.rest.resources.inout;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import gr.esdalab.atlas.devices.core.domain.Coordinates;
import lombok.Getter;
import lombok.NonNull;

public interface CoordinatesInOut {

    @Getter
    class CartesianInOut implements CoordinatesInOut{
        private final float x;
        private final float y;

        @JsonCreator
        public CartesianInOut(@JsonProperty("x") final float x,
                              @JsonProperty("y") final float y){
            this.x = x;
            this.y = y;
        }

        /**
         *
         * @param coordinates
         * @return
         */
        public static CartesianInOut from(@NonNull final Coordinates.CartesianCoordinates coordinates) {
            return new CartesianInOut(coordinates.getX(), coordinates.getY());
        }

    }

    @Getter
    class GPSInOut implements CoordinatesInOut{
        private final double latitude;
        private final double longitude;
        private final double altitude;

        @JsonCreator
        public GPSInOut(@JsonProperty("latitude") final double latitude,
                        @JsonProperty("longitude") final double longitude,
                        @JsonProperty("altitude") final double altitude){
            this.latitude = latitude;
            this.longitude = longitude;
            this.altitude = altitude;
        }

        /**
         *
         * @param coordinates
         * @return
         */
        public static GPSInOut from(@NonNull final Coordinates.GPSCoordinates coordinates) {
            return new GPSInOut(coordinates.getLatitude(), coordinates.getLongitude(), coordinates.getAltitude());
        }

    }
}
