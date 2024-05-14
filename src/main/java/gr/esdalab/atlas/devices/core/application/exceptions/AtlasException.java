package gr.esdalab.atlas.devices.core.application.exceptions;

import gr.esdalab.atlas.devices.core.domain.DomainError;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

/**
 *  General Atlas Exception.
 */
@Getter
@RequiredArgsConstructor
public abstract class AtlasException extends RuntimeException{

    /**
     * The Domain Error.
     */
    private final List<DomainError> errors;

    /**
     * Exception (Requested data not found)
     */
    public static class ResourceNotExistException extends AtlasException{

        /**
         *
         */
        public ResourceNotExistException(@NonNull final DomainError.NotFoundDomainError error){
            super(Collections.singletonList(error));
        }
    }

    /**
     * Exception (Domain Violation Exception)
     */
    public static class DomainViolationException extends AtlasException{

        /**
         *
         */
        public DomainViolationException(@NonNull final List<DomainError> errors){
            super(errors);
        }
    }

    /**
     * Exception (MisConfigured Exception)
     * Exception thrown when mis-configuration (Mainly at implementation) occured.
     */
    public static class MisConfigurationException extends AtlasException{

        /**
         *
         */
        public MisConfigurationException(@NonNull final DomainError.MisConfiguredDomainError error){
            super(Collections.singletonList(error));
        }
    }

}
