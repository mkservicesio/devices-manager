package gr.esdalab.atlas.devices.common.resources.in;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import gr.esdalab.atlas.devices.adapters.persitence.entities.embeddable.NetworkLayerPrimary;
import gr.esdalab.atlas.devices.common.utils.ApplicationUtils;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Getter
public class HealthInput {

    /**
     * Gateway Version
     */
    private final String version;

    /**
     * List of the available addresses that gateway listens.
     */
    private final List<NetworkInterfaceInput> interfaces;

    /**
     *
     * @param version
     * @param interfaces
     */
    @JsonCreator
    public HealthInput(@JsonProperty("version") @NonNull final String version,
                       @JsonProperty("interfaces") @NonNull final List<NetworkInterfaceInput> interfaces){
        this.version = version;
        this.interfaces = interfaces;
    }

    @Getter
    public static class NetworkInterfaceInput{
        /**
         * The network interface technology
         */
        private final NetworkLayerPrimary.NetworkTechnologyType type;

        /**
         * The address for this interface
         */
        private final List<String> addresses;

        @JsonCreator
        public NetworkInterfaceInput(@JsonProperty("type") @NonNull final NetworkLayerPrimary.NetworkTechnologyType type,
                                     @JsonProperty("addresses") @NonNull final List<String> addresses){
            this.type = type;
            this.addresses = addresses;
        }

    }

    /**
     *  Map Location DeSerializers
     */
    @Slf4j
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class HealthDtoDeSerializer{

        /**
         * De-Serialize a Map Location event.
         * @param data
         * @return
         */
        public static Optional<HealthInput> get(byte[] data){
            try {
                return Optional.of(ApplicationUtils.getJsonMapper().readValue(data, HealthInput.class));
            } catch (IOException e) {
                log.error("Unable to parse health message",e);
                return Optional.empty();
            }
        }
    }


}
