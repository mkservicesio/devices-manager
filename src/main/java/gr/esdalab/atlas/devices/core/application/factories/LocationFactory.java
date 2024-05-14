package gr.esdalab.atlas.devices.core.application.factories;

import gr.esdalab.atlas.devices.adapters.rest.resources.in.LocationInput;
import gr.esdalab.atlas.devices.core.application.exceptions.AtlasException;
import gr.esdalab.atlas.devices.core.domain.Coordinates;
import gr.esdalab.atlas.devices.core.domain.DomainError;
import gr.esdalab.atlas.devices.core.domain.common.Appendage;
import gr.esdalab.atlas.devices.core.domain.locations.Location;
import gr.esdalab.atlas.devices.core.domain.locations.impl.IndoorLocation;
import gr.esdalab.atlas.devices.core.domain.locations.impl.OutdoorLocation;
import lombok.NonNull;

import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Location Factory
 */
public class LocationFactory {

    /**
     *
     * @param locationInput
     * @return
     */
    public static Location from(@NonNull final LocationInput.CreatedLocationInput locationInput,
                                @NonNull final Appendage appendage){
        if( locationInput.getType() == LocationInput.LocationType.INDOOR ){
            return new IndoorLocation(locationInput.getIdentity(),
                    locationInput.getLabel(),
                    locationInput.getCoordinates().stream().map(Coordinates.GPSCoordinates::from).collect(Collectors.toList()),
                    appendage,
                    Collections.emptyList() //Empty until implemented.
            );
        }else if( locationInput.getType() == LocationInput.LocationType.OUTDOOR ){
            return new OutdoorLocation<>(locationInput.getIdentity(),
                    locationInput.getLabel(),
                    locationInput.getCoordinates().stream().map(Coordinates.GPSCoordinates::from).collect(Collectors.toList()),
                    appendage,
                    Collections.emptyList(), //Empty until implemented.
                    Collections.emptyList() //Empty, device will be attached at runtime.
            );
        }
        throw new AtlasException.MisConfigurationException(new DomainError.MisConfiguredDomainError("Currently only two types of locations are allowed. INDOOR, OUTDOOR"));
    }

}
