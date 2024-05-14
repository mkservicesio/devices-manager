package gr.esdalab.atlas.devices.core.domain.iot.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import gr.esdalab.atlas.devices.common.IotCommandStatus;
import lombok.*;

/**
 * Abstract class re-presenting a command that will be run on the objects running
 * in the phusical environment, e,g Devices, Robots, Gateways, Vehicles etc...
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class IotCommand {

    /**
     * Return the type of the command.
     * @return
     */
    protected final String type;

    /**
     * Determines the status of the command.
     */
    protected final IotCommandStatus status;

    /**
     * Class re-presenting part of the command.
     * @param <T>
     */
    @Getter
    @RequiredArgsConstructor
    public static class Part<T> {

        /**
         * A user-friendly label for part
         */
        private String field;

        /**
         * The data of the part.
         */
        private final T data;

        /**
         * All arguments Constructor
         */
        @JsonCreator
        public Part(@JsonProperty("field") @NonNull final String field,
                    @JsonProperty("data") @NonNull final T data){
            this.field = field;
            this.data = data;
        }

    }

    /**
     * Command Part separator.
     */
    public enum PartSeparator{

        NONE,
        COMMA,
        ASTERISK,
        COLON;

    }

}
