package gr.esdalab.atlas.devices.adapters.rest.resources.out;

import gr.esdalab.atlas.devices.adapters.persitence.entities.AppendageJpa;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.devices.DeviceOutput;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.devices.GatewayOutput;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.devices.RobotOutput;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AppendageOutput {

    /**
     * The appendage ID
     */
    @Schema(name = "Appendage ID", example = "1", required = true)
    private final int appendageId;

    /**
     * The locations that this appendage contains.
     */
    private final LocationOutput location;

    /**
     * List with the available gateways
     */
    private final List<GatewayOutput> gateways;

    /**
     * List with the available robots
     */
    private final List<RobotOutput> robots;

    /**
     * List with the available devices
     */
    private final List<DeviceOutput> devices;

    /**
     * Mapper method.
     * @param appendageJpa
     * @return
     */
    public static AppendageOutput from(@NonNull final AppendageJpa appendageJpa,
                                       final LocationOutput location,
                                       @NonNull final List<GatewayOutput> gateways,
                                       @NonNull final List<RobotOutput> robots,
                                       @NonNull final List<DeviceOutput> devices) {
        return new AppendageOutput(
                appendageJpa.getId(),
                location,
                gateways,
                robots,
                devices
        );
    }
}
