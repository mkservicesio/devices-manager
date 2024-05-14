package gr.esdalab.atlas.devices.core.domain.vehicles.commands.parts;

import gr.esdalab.atlas.devices.core.domain.iot.commands.CommandPart;
import gr.esdalab.atlas.devices.core.domain.iot.commands.IotCommand;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

/**
 * Main Unit command.
 */
@Getter
public class VehicleMainUnitCommandPart implements CommandPart<String> {

    private static final String COMMAND_TYPE = "MU";

    /**
     * The parts of this command.
     */
    private final List<IotCommand.Part<String>> parts;

    /**
     * Required argument constructor.
     * @param parts
     */
    public VehicleMainUnitCommandPart(@NonNull final List<IotCommand.Part<String>> parts) {
        this.parts = parts;
    }

    @Override
    public String getType() {
        return COMMAND_TYPE;
    }

    @Override
    public List<IotCommand.Part<String>> getContent() {
        return parts;
    }
}
