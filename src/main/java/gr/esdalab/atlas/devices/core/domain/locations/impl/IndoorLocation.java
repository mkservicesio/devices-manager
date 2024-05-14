package gr.esdalab.atlas.devices.core.domain.locations.impl;

import gr.esdalab.atlas.devices.adapters.persitence.entities.LocationJpa;
import gr.esdalab.atlas.devices.core.domain.Coordinates;
import gr.esdalab.atlas.devices.core.domain.Device;
import gr.esdalab.atlas.devices.core.domain.common.Appendage;
import gr.esdalab.atlas.devices.core.domain.locations.Location;
import gr.esdalab.atlas.devices.core.domain.locations.helpers.RepresentationMap;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

/**
 * A location re-presenting an indoor environment (E.g AALHouse).
 */
@Getter
public class IndoorLocation extends Location.PhysicalLocation {

    /**
     * The rooms of the Location.
     */
    private final List<Room<? extends Device>> rooms;

    /**
     * Default Required Arguments Constructor.
     * @param identifier
     * @param label
     * @param coordinates
     */
    public IndoorLocation(@NonNull final String identifier,
                          final String label,
                          @NonNull final List<Coordinates.GPSCoordinates> coordinates,
                          @NonNull final Appendage appendage,
                          @NonNull final List<Room<? extends Device>> rooms) {
        super(identifier, label, coordinates, appendage);
        this.rooms = rooms;

    }

    /**
     * A room of an indoor environment.
     * @param <T>
     */
    @Getter
    @RequiredArgsConstructor
    public static class Room <T extends Device>{

        /**
         * The coordinates of the location.
         */
        private final List<Coordinates> coordinates;

        /**
         * The Devices of the Room
         */
        private final List<T> devices;

        /**
         * The re-presentation map.
         */
        private final RepresentationMap map;
    }

    /**
     * Crated Indoor Location
     */
    @Getter
    public static class CreatedIndoorLocation extends IndoorLocation{
        private final int locationId;

        /**
         * Default Required Arguments Constructor.
         *
         * @param locationId
         * @param identifier
         * @param label
         * @param coordinates
         * @param rooms
         */
        public CreatedIndoorLocation(final int locationId,
                                     @NonNull final String identifier,
                                     final String label,
                                     @NonNull final List<Coordinates.GPSCoordinates> coordinates,
                                     @NonNull final Appendage appendage,
                                     @NonNull final List<Room<? extends Device>> rooms) {
            super(identifier, label, coordinates, appendage, rooms);
            this.locationId = locationId;
        }

        /**
         *
         * @param locationJpa
         * @return
         */
        public static CreatedIndoorLocation from(@NonNull final LocationJpa locationJpa) {
            return new CreatedIndoorLocation(locationJpa.getId(),
                    locationJpa.getIdentity(),
                    locationJpa.getFriendlyName(),
                    locationJpa.getCoordinates(),
                    new Appendage(locationJpa.getAppendage().getId()),
                    Collections.emptyList() //Empty until rooms implemented
            );
        }
    }


}
