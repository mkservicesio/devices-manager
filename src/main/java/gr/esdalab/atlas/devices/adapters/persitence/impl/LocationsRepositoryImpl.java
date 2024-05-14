package gr.esdalab.atlas.devices.adapters.persitence.impl;

import gr.esdalab.atlas.devices.adapters.persitence.AppendagesJpaRepository;
import gr.esdalab.atlas.devices.adapters.persitence.LocationMapJpaRepository;
import gr.esdalab.atlas.devices.adapters.persitence.LocationsJpaRepository;
import gr.esdalab.atlas.devices.adapters.persitence.entities.LocationJpa;
import gr.esdalab.atlas.devices.adapters.persitence.specs.LocationSpecification;
import gr.esdalab.atlas.devices.common.errors.ErrorMessage;
import gr.esdalab.atlas.devices.core.application.exceptions.AtlasException;
import gr.esdalab.atlas.devices.core.application.query.LocationsQueryService;
import gr.esdalab.atlas.devices.core.domain.DomainError;
import gr.esdalab.atlas.devices.core.domain.common.PageableOf;
import gr.esdalab.atlas.devices.core.domain.locations.Location;
import gr.esdalab.atlas.devices.core.domain.locations.impl.IndoorLocation;
import gr.esdalab.atlas.devices.core.domain.locations.impl.OutdoorLocation;
import gr.esdalab.atlas.devices.core.domain.locations.helpers.RepresentationMap;
import gr.esdalab.atlas.devices.core.domain.repositories.LocationsRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationsRepositoryImpl implements LocationsRepository {

    private final LocationsJpaRepository locationsJpaRepo;
    private final LocationMapJpaRepository locationMapJpaRepo;
    private final AppendagesJpaRepository appendagesJpaRepo;

    @Override
    public Optional<Location> getLocation(final int locationId) {
        return locationsJpaRepo.findById(locationId)
                .map(locationJpa -> {
                    if( locationJpa.isIndoor() ){
                        return IndoorLocation.CreatedIndoorLocation.from(locationJpa);
                    }
                    return OutdoorLocation.CreatedOutdoorLocation.from(locationJpa);
                });
    }

    @Override
    public Optional<Location> update(final int locationId,
                                     @NonNull final Location location) {
        return locationsJpaRepo.findById(locationId)
                .map(locationJpa -> {
                    if( location instanceof IndoorLocation ){
                        final IndoorLocation indoor = (IndoorLocation)location;
                        return locationsJpaRepo.save(locationJpa.update(indoor.getIdentifier(), indoor.getLabel(), true, indoor.getCoordinates()));
                    }
                    final OutdoorLocation<?> outdoor = (OutdoorLocation<?>)location;
                    return locationsJpaRepo.save(locationJpa.update(outdoor.getIdentifier(), outdoor.getLabel(), false, outdoor.getCoordinates()));
                }).map(locationJpa -> {
                    if( locationJpa.isIndoor() ){
                        return IndoorLocation.CreatedIndoorLocation.from(locationJpa);
                    }
                    return OutdoorLocation.CreatedOutdoorLocation.from(locationJpa);
                });
    }

    @Override
    public Optional<RepresentationMap> getLocationMap(final int locationId) {
        return locationsJpaRepo.findById(locationId)
                .map(it -> locationMapJpaRepo.findById(it.getId()))
                .orElseThrow(() -> {
                    throw new AtlasException.ResourceNotExistException(new DomainError.NotFoundDomainError(ErrorMessage.LOCATION_NOT_EXIST.getMessage()));
                })
                .map(RepresentationMap::from);
    }

    @Override
    @Transactional
    public void deleteById(final int locationId) {
        locationsJpaRepo.findById(locationId)
                .ifPresentOrElse(locationJpa -> {
                            Optional.ofNullable(locationJpa.getMap()).ifPresent(locationMapJpaRepo::delete);
                            locationsJpaRepo.delete(locationJpa);
                            appendagesJpaRepo.delete(locationJpa.getAppendage());
                        },
                        () -> {
                            log.warn("Location {} not found!!", locationId);
                            throw new AtlasException.ResourceNotExistException(new DomainError.NotFoundDomainError(ErrorMessage.LOCATION_NOT_EXIST.getMessage()));
                        });
    }

    @Override
    public PageableOf<Location> getLocations(@NonNull final LocationsQueryService.LocationQuery query) {
        Page<LocationJpa> page = locationsJpaRepo.findAll(LocationSpecification.from(query), query.to());
        return new PageableOf<>(
                page.getTotalElements(),
                page.get()
                        .map(locationJpa -> {
                            if( locationJpa.isIndoor() ){
                                return IndoorLocation.CreatedIndoorLocation.from(locationJpa);
                            }
                            return OutdoorLocation.CreatedOutdoorLocation.from(locationJpa);
                        })
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Optional<Location> getLocationOfAppendage(final int appendageId) {
        return appendagesJpaRepo.findById(appendageId)
                .map(locationsJpaRepo::findByAppendage)
                .filter(Optional::isPresent)
                .flatMap(locationJpaOpt -> locationJpaOpt
                        .map(locationJpa -> {
                            if (locationJpa.isIndoor()) {
                                return IndoorLocation.CreatedIndoorLocation.from(locationJpa);
                            }
                            return OutdoorLocation.CreatedOutdoorLocation.from(locationJpa);
                        }));
    }
}
