package gr.esdalab.atlas.devices.core.application.cases.impl;

import gr.esdalab.atlas.devices.adapters.persitence.DatatypesJpaRepository;
import gr.esdalab.atlas.devices.adapters.rest.resources.inout.ThresholdInputOutput;
import gr.esdalab.atlas.devices.common.errors.ErrorMessage;
import gr.esdalab.atlas.devices.core.application.cases.DatatypesUseCase;
import gr.esdalab.atlas.devices.core.application.exceptions.AtlasException;
import gr.esdalab.atlas.devices.core.domain.common.Datatype;
import gr.esdalab.atlas.devices.core.domain.DomainError;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DatatypesUseCaseImpl implements DatatypesUseCase {

    private final DatatypesJpaRepository datatypesJpaRepo;
    private final Validator validator;

    @Override
    public void updateThreshold(final int datatypeId,
                                @NonNull final ThresholdInputOutput thresholdInput) {
        final Datatype.Threshold threshold = Datatype.Threshold.from(thresholdInput);
        final Set<ConstraintViolation<Datatype.Threshold>> violations = validator.validate(threshold);
        if( !violations.isEmpty() ){
            throw new AtlasException.DomainViolationException(violations.stream().map(it -> new DomainError.RuleViolationDomainError(it.getMessage())).collect(Collectors.toList()));
        }
        datatypesJpaRepo.findById(datatypeId)
                .ifPresentOrElse(
                        datatypeJpa -> {
                            log.info("Updating datatype with ID {}, Datatype: {}", datatypeId, datatypeJpa.getDatatype());
                            datatypesJpaRepo.save(datatypeJpa.withThreshold(threshold.asString()));
                        },
                        () -> {
                            throw new AtlasException.ResourceNotExistException(new DomainError.NotFoundDomainError(ErrorMessage.DATATYPE_NOT_EXIST.getMessage()));
                        });
    }
}
