package gr.esdalab.atlas.devices.adapters.persitence.impl;

import gr.esdalab.atlas.devices.adapters.persitence.AppendagesJpaRepository;
import gr.esdalab.atlas.devices.adapters.persitence.DatatypesJpaRepository;
import gr.esdalab.atlas.devices.adapters.persitence.LocationsJpaRepository;
import gr.esdalab.atlas.devices.adapters.persitence.entities.AppendageJpa;
import gr.esdalab.atlas.devices.common.errors.ErrorMessage;
import gr.esdalab.atlas.devices.core.application.exceptions.AtlasException;
import gr.esdalab.atlas.devices.core.domain.DomainError;
import gr.esdalab.atlas.devices.core.domain.common.Appendage;
import gr.esdalab.atlas.devices.core.domain.common.Datatype;
import gr.esdalab.atlas.devices.core.domain.repositories.CommonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommonRepositoryImpl implements CommonRepository{

    private final AppendagesJpaRepository appendagesJpaRepo;
    private final DatatypesJpaRepository datatypesJpaRepo;
    private final LocationsJpaRepository locationsJpaRepo;

    @Override
    @Transactional
    public Appendage getCommonAppendage() {
        return new Appendage(appendagesJpaRepo.save(new AppendageJpa()).getId());
    }

    @Override
    public List<Datatype> getDatatypes() {
        return datatypesJpaRepo
                .findAll()
                .stream()
                .map(it -> new Datatype(it.getId(), it.getDatatype(), Datatype.Threshold.from(it.getThreshold())))
                .collect(Collectors.toList());
    }

    @Override
    public Appendage getCommonAppendageOfLocation(final int locationId) {
        return locationsJpaRepo.findById(locationId)
                .map(it -> new Appendage(it.getAppendage().getId()))
                .orElseThrow(() -> new AtlasException.ResourceNotExistException(new DomainError.NotFoundDomainError(ErrorMessage.LOCATION_NOT_EXIST.getMessage())));
    }
}
