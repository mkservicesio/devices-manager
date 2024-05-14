package gr.esdalab.atlas.devices.adapters.rest.resources.in;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import gr.esdalab.atlas.devices.adapters.rest.resources.inout.CoordinatesInOut;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

/**
 * Input Event for generating a new vehicle.
 */
@Getter
@RequiredArgsConstructor
public abstract class LocationInput {

    /**
     * An identifier
     */
    protected final String identity;

    /**
     * A user-label for the user.
     */
    protected final String label;

    /**
     * The Location Type.
     */
    public enum LocationType{
        INDOOR, OUTDOOR;
    }

    protected final AssociationInput associations;


    /**
     * New Location Input
     */
    @Getter
    public static class DraftLocationInput extends LocationInput{

        /**
         * The type of the location.
         */
        private final LocationType type;

        /**
         * The coordinates of the location.
         */
        private final List<CoordinatesInOut.GPSInOut> coordinates;

        /**
         * Required arguments constructor.
         * @param identity
         * @param label
         */
        @JsonCreator
        public DraftLocationInput(@JsonProperty("identity") @NonNull final String identity,
                                  @JsonProperty("label") @NonNull final String label,
                                  @JsonProperty("type") @NonNull final LocationType type,
                                  @JsonProperty("coordinates") @NonNull final List<CoordinatesInOut.GPSInOut> coordinates,
                                  @JsonProperty("associations") final AssociationInput associations) {
            super(identity, label, associations);
            this.type = type;
            this.coordinates = coordinates;
        }

        /**
         * Including the Location input the Vehicle ID to associate.
         * @param vehicleId
         * @return
         */
        public DraftLocationInput withVehicleAssociation(final int vehicleId) {
            return new DraftLocationInput(this.identity, this.label, this.type, this.coordinates, new AssociationInput(Collections.singletonList(vehicleId)));
        }
    }

    /**
     * Created Location Input.
     */
    @Getter
    public static class CreatedLocationInput extends DraftLocationInput {


        /**
         * Required arguments constructor.
         *
         * @param identity
         * @param label
         */
        public CreatedLocationInput(@JsonProperty("identity") @NonNull final String identity,
                                    @JsonProperty("label") @NonNull final String label,
                                    @JsonProperty("type") @NonNull final LocationType type,
                                    @JsonProperty("coordinates") @NonNull final List<CoordinatesInOut.GPSInOut> coordinates,
                                    @JsonProperty("associations") final AssociationInput associations) {
            super(identity, label, type, coordinates, associations);
        }
    }



}
