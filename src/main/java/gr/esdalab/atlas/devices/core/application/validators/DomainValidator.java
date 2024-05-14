package gr.esdalab.atlas.devices.core.application.validators;

import gr.esdalab.atlas.devices.core.domain.DomainError;

import java.util.List;

/**
 * Marker interface Domain Validator.
 */
public interface DomainValidator<T> {

    /**
     * Validate the given data.
     * @param data - The data to validate.
     * @return - Return a list of errors. In case of valid object return and empty array.
     */
    List<DomainError> validate(T data);

}
