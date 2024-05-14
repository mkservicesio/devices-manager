package gr.esdalab.atlas.devices.adapters.rest.resources.out.errors;

import com.fasterxml.jackson.annotation.JsonInclude;
import gr.esdalab.atlas.devices.core.domain.DomainError;
import lombok.*;
import org.springframework.validation.FieldError;

import java.util.Optional;

/**
 * General Error Output
 */
@RequiredArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorOutput {

    /**
     * Error Code (In case of domain error)
     */
    private final String code;

    /**
     * Error field (In case of error in rest input fields.)
     */
    private final String field;

    /**
     * Error Message
     */
    private final String message;

    /**
     * Return Default error from Atlas Exception.
     * @param generic
     * @return
     */
    public static ErrorOutput from(@NonNull final String generic) {
        return ErrorOutput.builder()
                .code(DomainError.GENERIC)
                .field(null)
                .message(generic)
                .build();
    }

    /**
     *
     * @param error
     * @return
     */
    public static ErrorOutput from(@NonNull final FieldError error) {
        return ErrorOutput.builder()
                .field(error.getField())
                .code(null)
                .message(error.getDefaultMessage())
                .build();
    }

    /**
     *
     * @param translation
     * @return
     */
    public ErrorOutput translate(final Optional<String> translation) {
        return ErrorOutput.builder()
                .code(this.code)
                .field(this.field)
                .message(translation.orElse("-"))
                .build();
    }
}
