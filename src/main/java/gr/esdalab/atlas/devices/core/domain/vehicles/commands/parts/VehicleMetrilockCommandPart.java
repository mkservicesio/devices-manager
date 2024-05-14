package gr.esdalab.atlas.devices.core.domain.vehicles.commands.parts;

import gr.esdalab.atlas.devices.core.domain.iot.commands.CommandPart;
import gr.esdalab.atlas.devices.core.domain.iot.commands.IotCommand;
import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;

/**
 * Main Unit command.
 */
@Getter
public class VehicleMetrilockCommandPart implements CommandPart<String> {

    private static final String COMMAND_TYPE = "ML";

    /**
     * The parts of this command.
     */
    private final List<IotCommand.Part<String>> parts;

    /**
     * Required argument constructor.
     * @param parts
     */
    public VehicleMetrilockCommandPart(@NonNull final List<IotCommand.Part<String>> parts) {
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
