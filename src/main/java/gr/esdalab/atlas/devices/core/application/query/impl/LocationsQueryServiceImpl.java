package gr.esdalab.atlas.devices.core.application.query.impl;

import gr.esdalab.atlas.devices.adapters.rest.resources.out.LocationMapOutput;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.LocationOutput;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.PageableOutput;
import gr.esdalab.atlas.devices.core.application.query.LocationsQueryService;
import gr.esdalab.atlas.devices.core.domain.Device;
import gr.esdalab.atlas.devices.core.domain.common.PageableOf;
import gr.esdalab.atlas.devices.core.domain.locations.Location;
import gr.esdalab.atlas.devices.core.domain.locations.impl.IndoorLocation;
import gr.esdalab.atlas.devices.core.domain.locations.impl.OutdoorLocation;
import gr.esdalab.atlas.devices.core.domain.repositories.LocationsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of {@LocationsQueryService}
 */
@Service
@RequiredArgsConstructor
public class LocationsQueryServiceImpl implements LocationsQueryService {
    private final LocationsRepository locationsRepo;

    @Override
    public PageableOutput<LocationOutput> getLocations(final Map<String, String> query) {
        PageableOf<Location> locations = locationsRepo.getLocations(Optional.ofNullable(query).map(LocationQuery::new).orElseGet(LocationQuery::new));
        return new PageableOutput<>(
                locations.getTotal(),
                locations.getData()
                        .stream()
                        .map(it -> {
                            if( it instanceof IndoorLocation.CreatedIndoorLocation){
                                return LocationOutput.from((IndoorLocation.CreatedIndoorLocation)it);
                            }
                            return LocationOutput.from((OutdoorLocation.CreatedOutdoorLocation<?>) it);
                        })
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Optional<LocationOutput> getLocation(final int locationId) {
        return locationsRepo.getLocation(locationId)
                .map(location -> {
                    if( location instanceof IndoorLocation.CreatedIndoorLocation) return LocationOutput.from((IndoorLocation.CreatedIndoorLocation)location);
                    return LocationOutput.from((OutdoorLocation.CreatedOutdoorLocation<? extends Device>)location);
                });
    }

    @Override
    public Optional<LocationMapOutput> getMapOf(final int locationId) {
        return locationsRepo.getLocationMap(locationId).map(LocationMapOutput::from);
    }

    @Override
    public Optional<LocationOutput> getLocationByAppendage(final int appendageId) {
        return locationsRepo.getLocationOfAppendage(appendageId)
                .map(location -> {
                    if( location instanceof IndoorLocation.CreatedIndoorLocation) return LocationOutput.from((IndoorLocation.CreatedIndoorLocation)location);
                    return LocationOutput.from((OutdoorLocation.CreatedOutdoorLocation<? extends Device>)location);
                });
    }
}
