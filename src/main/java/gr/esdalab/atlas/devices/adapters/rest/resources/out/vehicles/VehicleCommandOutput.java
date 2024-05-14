package gr.esdalab.atlas.devices.adapters.rest.resources.out.vehicles;

import gr.esdalab.atlas.devices.adapters.rest.resources.inout.VehicleCommandPartInputOutput;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Class Re-representing a command output.
 */
@Getter
@RequiredArgsConstructor
public class VehicleCommandOutput {

    /**
     * Command internal ID
     */
    private final int commandId;

    /**
     * The vehicle ID that this command be applied.
     */
    private final int vehicleId;

    /**
     * The type of the command.
     */
    private final String type;

    /**
     * The parts of this command.
     */
    private final List<VehicleCommandPartInputOutput> parts;

}
