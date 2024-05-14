package gr.esdalab.atlas.devices.adapters.persitence.impl;

import gr.esdalab.atlas.devices.adapters.persitence.AppendagesJpaRepository;
import gr.esdalab.atlas.devices.adapters.persitence.LocationsJpaRepository;
import gr.esdalab.atlas.devices.adapters.persitence.entities.LocationJpa;
import gr.esdalab.atlas.devices.common.errors.ErrorMessage;
import gr.esdalab.atlas.devices.core.application.exceptions.AtlasException;
import gr.esdalab.atlas.devices.core.domain.DomainError;
import gr.esdalab.atlas.devices.core.domain.common.Appendage;
import gr.esdalab.atlas.devices.core.domain.Device;
import gr.esdalab.atlas.devices.core.domain.locations.impl.OutdoorLocation;
import gr.esdalab.atlas.devices.core.domain.repositories.OutdoorLocationsRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OutdoorLocationsRepositoryImpl implements OutdoorLocationsRepository {

    private final AppendagesJpaRepository appendagesJpaRepo;
    private final LocationsJpaRepository locationsJpaRepo;

    @Override
    public OutdoorLocation.CreatedOutdoorLocation<? extends Device> create(@NonNull final Appendage appendage,
                                                                           @NonNull final OutdoorLocation<? extends Device> outdoor) {

        final LocationJpa locationJpa = locationsJpaRepo
                .save(LocationJpa.draft(
                            outdoor.getIdentifier(),
                            outdoor.getLabel(),
                            appendagesJpaRepo.getOne(appendage.getAppendageId()),
                            false,
                            outdoor.getCoordinates()
                        )
                );
        return getLocation(locationJpa.getId())
                .orElseThrow(() -> new AtlasException.MisConfigurationException(new DomainError.MisConfiguredDomainError(ErrorMessage.LOCATION_NOT_EXIST.getMessage())));
    }

    /**
     *
     * @param locationId
     * @return
     */
    private Optional<OutdoorLocation.CreatedOutdoorLocation<? extends Device>> getLocation(final int locationId) {
        return locationsJpaRepo.findById(locationId).map(OutdoorLocation.CreatedOutdoorLocation::from);
    }

}
