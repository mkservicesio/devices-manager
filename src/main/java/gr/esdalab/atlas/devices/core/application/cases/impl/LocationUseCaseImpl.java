package gr.esdalab.atlas.devices.core.application.cases.impl;

import gr.esdalab.atlas.devices.adapters.rest.resources.in.LocationInput;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.LocationOutput;
import gr.esdalab.atlas.devices.core.application.cases.LocationsUseCase;
import gr.esdalab.atlas.devices.core.application.factories.LocationFactory;
import gr.esdalab.atlas.devices.core.domain.common.Appendage;
import gr.esdalab.atlas.devices.core.domain.Coordinates;
import gr.esdalab.atlas.devices.core.domain.Device;
import gr.esdalab.atlas.devices.core.domain.events.AssociationEvent;
import gr.esdalab.atlas.devices.core.domain.events.LocationEvent;
import gr.esdalab.atlas.devices.core.domain.locations.impl.IndoorLocation;
import gr.esdalab.atlas.devices.core.domain.locations.impl.OutdoorLocation;
import gr.esdalab.atlas.devices.core.domain.repositories.*;
import gr.esdalab.atlas.devices.core.domain.vehicles.Vehicle;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationUseCaseImpl implements LocationsUseCase {

    private final CommonRepository commonRepo;
    private final IndoorLocationsRepository indoorLocationsRepo;
    private final OutdoorLocationsRepository outdoorLocationsRepo;
    private final LocationsRepository locationsRepo;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public Optional<LocationOutput> create(@NonNull final LocationInput.DraftLocationInput draftLocation) {
        final Appendage appendage = commonRepo.getCommonAppendage();
        Optional<LocationOutput> locationOutput = Optional.empty();
        //TODO Can be implemented better??? //Refactor with Factory Pattern.
        if( draftLocation.getType() == LocationInput.LocationType.INDOOR ){
            final IndoorLocation indoor = new IndoorLocation(draftLocation.getIdentity(),
                    draftLocation.getLabel(),
                    draftLocation.getCoordinates().stream().map(Coordinates.GPSCoordinates::from).collect(Collectors.toList()),
                    appendage,
                    Collections.emptyList() //Current version does not support rooms on indoor locations.
            );
            locationOutput = Optional.of(LocationOutput.from(indoorLocationsRepo.create(appendage, indoor)));
        }else if( draftLocation.getType() == LocationInput.LocationType.OUTDOOR ){
            final OutdoorLocation<Device> outdoor = new OutdoorLocation<>(draftLocation.getIdentity(),
                    draftLocation.getLabel(),
                    draftLocation.getCoordinates().stream().map(Coordinates.GPSCoordinates::from).collect(Collectors.toList()),
                    appendage,
                    Collections.emptyList(),// Current Version does not support indoor locations for an outdoor environment.
                    Collections.emptyList() //Current version does not support associated devices for outdoor location
            );
            locationOutput = Optional.of(LocationOutput.from(outdoorLocationsRepo.create(appendage, outdoor)));
        }

        locationOutput.ifPresent(location -> {
            if( draftLocation.getAssociations() != null && ( (draftLocation.getAssociations().getVehicles() != null) && !draftLocation.getAssociations().getVehicles().isEmpty() )){
                    draftLocation.getAssociations().getVehicles().forEach(vehicle -> {
                        eventPublisher.publishEvent(new AssociationEvent.LocationAssociationEvent<>(this, location.getLocationId(), new Vehicle.ActiveVehicle(vehicle)));
                    });
            }
        });
        return locationOutput;
    }

    @Override
    @Transactional
    public void delete(final int locationId) {
        eventPublisher.publishEvent(new LocationEvent.LocationPreDeletedEvent(this, locationId));
        locationsRepo.deleteById(locationId);
    }

    @Override
    @Transactional
    public Optional<LocationOutput> update(final int locationId,
                                           @NonNull final LocationInput.CreatedLocationInput locationInput) {
        final Appendage appendage = commonRepo.getCommonAppendageOfLocation(locationId);
        final Optional<LocationOutput> locationOutput = locationsRepo.update(locationId, LocationFactory.from(locationInput, appendage))
                .map(location -> {
                    if( location instanceof IndoorLocation.CreatedIndoorLocation) return LocationOutput.from((IndoorLocation.CreatedIndoorLocation)location);
                    return LocationOutput.from((OutdoorLocation.CreatedOutdoorLocation<? extends Device>)location);
                });
        eventPublisher.publishEvent(new LocationEvent.LocationPreUpdateEvent(this, locationId));
        locationOutput.ifPresent(location -> {
            locationInput.getAssociations().getVehicles().forEach(vehicle -> {
                eventPublisher.publishEvent(new AssociationEvent.LocationAssociationEvent<>(this, location.getLocationId(), new Vehicle.ActiveVehicle(vehicle)));
            });
        });
        return locationOutput;
    }

}
