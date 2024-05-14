package gr.esdalab.atlas.devices.core.domain.locations.impl;

import gr.esdalab.atlas.devices.adapters.persitence.entities.LocationJpa;
import gr.esdalab.atlas.devices.core.domain.Coordinates;
import gr.esdalab.atlas.devices.core.domain.Device;
import gr.esdalab.atlas.devices.core.domain.common.Appendage;
import gr.esdalab.atlas.devices.core.domain.locations.Location;
import lombok.Getter;
import lombok.NonNull;

import java.util.Collections;
import java.util.List;

@Getter
public class OutdoorLocation<T extends Device> extends Location.PhysicalLocation {

    /**
     * The indoor location of the outdoor location.
     *  For example an area has one home.
     */
    private final List<IndoorLocation> indoor;

    /**
     * The Devices of the outdoor location
     */
    private final List<T> devices;

    /**
     * Default required arguments constructor.
     * @param identifier
     * @param label
     * @param coordinates
     */
    public OutdoorLocation(@NonNull final String identifier,
                           final String label,
                           @NonNull final List<Coordinates.GPSCoordinates> coordinates,
                           @NonNull final Appendage appendage,
                           @NonNull final List<IndoorLocation> indoor,
                           @NonNull final List<T> devices) {
        super(identifier, label, coordinates, appendage);
        this.indoor = indoor;
        this.devices = devices;
    }

    /**
     * Crated Indoor Location
     */
    @Getter
    public static class CreatedOutdoorLocation<T extends Device> extends OutdoorLocation<T>{
        private final int locationId;

        /**
         * Default Required Arguments Constructor.
         * @param locationId
         * @param identifier
         * @param label
         * @param coordinates
         * @param indoor
         * @param devices
         */
        public CreatedOutdoorLocation(final int locationId,
                                      @NonNull final String identifier,
                                      final String label,
                                      @NonNull final List<Coordinates.GPSCoordinates> coordinates,
                                      @NonNull final Appendage appendage,
                                      @NonNull final List<IndoorLocation> indoor,
                                      @NonNull final List<T> devices) {
            super(identifier, label, coordinates, appendage, indoor, devices);
            this.locationId = locationId;
        }

        /**
         *
         * @param locationJpa
         * @return
         */
        public static CreatedOutdoorLocation<? extends Device> from(@NonNull final LocationJpa locationJpa) {
            return new CreatedOutdoorLocation<>(locationJpa.getId(),
                    locationJpa.getIdentity(),
                    locationJpa.getFriendlyName(),
                    locationJpa.getCoordinates(),
                    new Appendage(locationJpa.getAppendage().getId()),
                    Collections.emptyList(), //Empty until implemented.
                    Collections.emptyList() //Empty until implemented.
            );
        }
    }

}
