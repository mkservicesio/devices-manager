package gr.esdalab.atlas.devices.core.domain;

import gr.esdalab.atlas.devices.core.application.exceptions.AtlasException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * General
 */
public interface DomainError {

    String GENERIC = "ERRGEN001";

    String getErrorMessage();

    @Getter
    @RequiredArgsConstructor
    class NotFoundDomainError implements DomainError{
        private final String errorMsg;

        @Override
        public String getErrorMessage() {
            return errorMsg;
        }
    }

    @Getter
    @RequiredArgsConstructor
    class RuleViolationDomainError implements DomainError{
        private final String errorMsg;

        @Override
        public String getErrorMessage() {
            return errorMsg;
        }
    }

    @Getter
    @RequiredArgsConstructor
    class MisConfiguredDomainError implements DomainError{
        private final String errorMsg;

        @Override
        public String getErrorMessage() {
            return errorMsg;
        }
    }

}
