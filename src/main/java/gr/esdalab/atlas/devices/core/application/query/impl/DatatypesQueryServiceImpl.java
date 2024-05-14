package gr.esdalab.atlas.devices.core.application.query.impl;

import gr.esdalab.atlas.devices.adapters.persitence.DatatypesJpaRepository;
import gr.esdalab.atlas.devices.adapters.rest.resources.inout.ThresholdInputOutput;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.IdOutput;
import gr.esdalab.atlas.devices.core.application.query.DatatypesQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of {@DatatypesQueryService}
 */
@Service
@RequiredArgsConstructor
public class DatatypesQueryServiceImpl implements DatatypesQueryService {

    private final DatatypesJpaRepository datatypesJpaRepo;

    @Override
    public List<IdOutput.DatatypeThresholdOutput> getDatatypes() {
        return datatypesJpaRepo.findAll().stream()
                .map(datatypeJpa -> new IdOutput.DatatypeThresholdOutput(datatypeJpa.getId(),
                            datatypeJpa.getDatatype(),
                            ThresholdInputOutput.from(datatypeJpa.getThreshold())
                        )
                )
                .collect(Collectors.toList());
    }

    @Override
    public Optional<IdOutput.DatatypeThresholdOutput> getDatatype(final int datatypeId) {
        return datatypesJpaRepo.findById(datatypeId)
                .map(datatypeJpa -> new IdOutput.DatatypeThresholdOutput(
                            datatypeJpa.getId(),
                            datatypeJpa.getDatatype(),
                            ThresholdInputOutput.from(datatypeJpa.getThreshold())
                        )
                );
    }
}
