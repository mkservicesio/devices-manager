package gr.esdalab.atlas.devices.core.application.query.impl;

import gr.esdalab.atlas.devices.adapters.persitence.DatatypesJpaRepository;
import gr.esdalab.atlas.devices.core.application.query.GenericQueryService;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.IdOutput;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of {@GenericQueryService}
 */
@Service
@RequiredArgsConstructor
public class GenericQueryServiceImpl implements GenericQueryService {

    private final DatatypesJpaRepository datatypesJpaRepo;

    @Override
    public Optional<IdOutput.DatatypeOutput> getDatatype(@NonNull final String datatype) {
        return datatypesJpaRepo.findByDatatype(datatype)
                .map(it -> new IdOutput.DatatypeOutput(it.getId(), it.getDatatype()));
    }
}
