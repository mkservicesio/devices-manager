package gr.esdalab.atlas.devices.core.application.cases.impl;

import gr.esdalab.atlas.devices.core.application.cases.AssociationsUseCase;
import gr.esdalab.atlas.devices.core.domain.locations.Location;
import gr.esdalab.atlas.devices.core.domain.locations.impl.OutdoorLocation;
import gr.esdalab.atlas.devices.core.domain.repositories.LocationsRepository;
import gr.esdalab.atlas.devices.core.domain.repositories.VehiclesRepository;
import gr.esdalab.atlas.devices.core.domain.vehicles.Vehicle;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AssociationsUseCaseImpl implements AssociationsUseCase {

    private final VehiclesRepository vehiclesRepository;
    private final LocationsRepository locationsRepo;

    @Override
    public void associationLocationWith(final int locationId,
                                        @NonNull final Vehicle.ActiveVehicle association) {
        final Location location = locationsRepo.getLocation(locationId).orElseThrow(() -> { throw new RuntimeException("Location not found."); });
        if( !(location instanceof OutdoorLocation.CreatedOutdoorLocation) ) throw new RuntimeException("Vehicle association can only be made in outdoor locations");
        vehiclesRepository.addPointOfInterest(association, (OutdoorLocation.CreatedOutdoorLocation<?>)location );
    }

    @Override
    public void cleanUpForLocation(final int locationId) {
        vehiclesRepository.cleanAssociationsByLocation(locationId);
    }

    @Override
    public void removeLocationAssociationWith(final int locationId,
                                              @NonNull final Vehicle.ActiveVehicle association) {
        final Location location = locationsRepo.getLocation(locationId).orElseThrow(() -> { throw new RuntimeException("Location not found."); });
        if( !(location instanceof OutdoorLocation.CreatedOutdoorLocation) ) throw new RuntimeException("Vehicle association can only be made in outdoor locations");
        vehiclesRepository.removePointOfInterest(association, (OutdoorLocation.CreatedOutdoorLocation<?>)location );
    }
}
