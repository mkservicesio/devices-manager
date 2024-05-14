package gr.esdalab.atlas.devices.adapters.rest.resources.out.devices;

import com.fasterxml.jackson.annotation.JsonInclude;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.networking.NetworkAdapterOutput;
import gr.esdalab.atlas.devices.core.domain.Health;
import gr.esdalab.atlas.devices.core.domain.gateways.Gateway;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GatewayOutput {

    /**
     * Gateway ID
     */
    private final int gatewayId;

    /**
     * Gateway Identity
     */
    @NonNull
    private final String identity;

    /**
     * Gateway available addresses
     */
    @NonNull
    private final List<NetworkAdapterOutput> networks;

    /**
     * The appendage ID refer to a coommon reference points for inter-connected devices.
     */
    private final int appendageId;

    /**
     * The version.
     */
    private final String version;

    /**
     * Last online.
     */
    private final Date onlineAt;

    /**
     * Map Domain to Adapter output object.
     * @param createdGateway
     * @return
     */
    public static GatewayOutput from(@NonNull final Gateway.CreatedGateway createdGateway) {
        return GatewayOutput.builder()
                .gatewayId(createdGateway.getId())
                .identity(createdGateway.getUrn())
                .networks(createdGateway.getAdapters().stream().map(NetworkAdapterOutput::from).collect(Collectors.toList()))
                .appendageId(createdGateway.getAppendage().getAppendageId())
                .version(createdGateway.getVersion())
                .onlineAt(Optional.ofNullable(createdGateway.getHealth()).map(Health.Device::getOnlineAt).orElse(null))
                .build();
    }
}
