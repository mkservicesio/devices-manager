package gr.esdalab.atlas.devices.adapters.rest.controllers.errors;

import gr.esdalab.atlas.devices.adapters.rest.resources.out.errors.ErrorOutput;
import gr.esdalab.atlas.devices.core.application.exceptions.AtlasException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Order(-2)
@Slf4j
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

    private final MessageSource messagesSource;

    /**
     *
     * @param errorAttributes
     * @param resources
     * @param applicationContext
     */
    public GlobalErrorWebExceptionHandler(final ErrorAttributes errorAttributes,
                                          final WebProperties.Resources resources,
                                          final ApplicationContext applicationContext,
                                          final ServerCodecConfigurer configurer,
                                          final MessageSource messagesSource) {
        super(errorAttributes, resources, applicationContext);
        super.setMessageWriters(configurer.getWriters());
        super.setMessageReaders(configurer.getReaders());
        this.messagesSource = messagesSource;
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    /**
     *
     * @param request
     * @return
     */
    private Mono<ServerResponse> renderErrorResponse(final ServerRequest request) {

        final Throwable throwable = getError(request);
        HttpStatus status;
        final List<ErrorOutput> errors = new ArrayList<>();
        throwable.printStackTrace();
        log.error(throwable.getMessage());
        if (throwable instanceof ResponseStatusException) {

            if( throwable instanceof WebExchangeBindException){
                final WebExchangeBindException exception = (WebExchangeBindException) throwable;
                status = HttpStatus.BAD_REQUEST;
                errors.addAll(exception.getBindingResult().getFieldErrors().stream().map(ErrorOutput::from).collect(Collectors.toList()));
            }else{
                final ResponseStatusException exception = (ResponseStatusException) throwable;
                status = exception.getStatus();
                errors.add(ErrorOutput.from(Optional.ofNullable(exception.getReason()).orElse(GenericErrorMessage.UNKNOWN_ERROR)));
            }

        }else if (throwable instanceof AtlasException.ResourceNotExistException) {
            final AtlasException.ResourceNotExistException exception = (AtlasException.ResourceNotExistException) throwable;
            status = HttpStatus.NOT_FOUND;
            errors.addAll(exception.getErrors().stream().map(it -> ErrorOutput.from(it.getErrorMessage())).collect(Collectors.toList()));
        }else if (throwable instanceof AtlasException.DomainViolationException) {
            final AtlasException.DomainViolationException exception = (AtlasException.DomainViolationException) throwable;
            status = HttpStatus.UNPROCESSABLE_ENTITY;
            errors.addAll(exception.getErrors().stream().map(it -> ErrorOutput.from(it.getErrorMessage())).collect(Collectors.toList()));
        }else if (throwable instanceof AtlasException.MisConfigurationException) {
            final AtlasException.MisConfigurationException exception = (AtlasException.MisConfigurationException) throwable;
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            errors.addAll(exception.getErrors().stream().map(it -> ErrorOutput.from(it.getErrorMessage())).collect(Collectors.toList()));
        }else{
            status = HttpStatus.BAD_REQUEST;
            errors.add(ErrorOutput.from(throwable.getMessage()));
        }

        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        BodyInserters.fromValue(errors.stream()
                                .map(err -> err.translate(Optional.ofNullable(messagesSource.getMessage(err.getMessage(), null, err.getMessage(), Locale.ENGLISH))))
                                .collect(Collectors.toList()))
                );

    }

    /**
     * Generic Error Messages.
     */
    public static class GenericErrorMessage{
        public static final String GENERIC_ERROR = "{generic.error}";
        public static final String UNKNOWN_ERROR = "{generic.unknown.error}";
    }

}
