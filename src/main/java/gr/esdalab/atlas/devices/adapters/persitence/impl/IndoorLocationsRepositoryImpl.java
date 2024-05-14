package gr.esdalab.atlas.devices.adapters.persitence.impl;

import gr.esdalab.atlas.devices.adapters.persitence.AppendagesJpaRepository;
import gr.esdalab.atlas.devices.adapters.persitence.LocationsJpaRepository;
import gr.esdalab.atlas.devices.adapters.persitence.entities.LocationJpa;
import gr.esdalab.atlas.devices.common.errors.ErrorMessage;
import gr.esdalab.atlas.devices.core.application.exceptions.AtlasException;
import gr.esdalab.atlas.devices.core.domain.DomainError;
import gr.esdalab.atlas.devices.core.domain.common.Appendage;
import gr.esdalab.atlas.devices.core.domain.locations.impl.IndoorLocation;
import gr.esdalab.atlas.devices.core.domain.repositories.IndoorLocationsRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IndoorLocationsRepositoryImpl implements IndoorLocationsRepository {

    private final AppendagesJpaRepository appendagesJpaRepo;
    private final LocationsJpaRepository locationsJpaRepo;

    @Override
    public IndoorLocation.CreatedIndoorLocation create(@NonNull final Appendage appendage,
                                                       @NonNull final IndoorLocation indoor) {
        final LocationJpa locationJpa = locationsJpaRepo
                .save(LocationJpa
                        .draft(indoor.getIdentifier(),
                                indoor.getLabel(),
                                appendagesJpaRepo.getOne(appendage.getAppendageId()),
                                true,
                                new ArrayList<>(indoor.getCoordinates())
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
    private Optional<IndoorLocation.CreatedIndoorLocation> getLocation(final int locationId) {
        return locationsJpaRepo.findById(locationId).map(IndoorLocation.CreatedIndoorLocation::from);
    }

}
