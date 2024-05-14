package gr.esdalab.atlas.devices.core.domain.vehicles.commands;

import gr.esdalab.atlas.devices.common.IotCommandStatus;
import gr.esdalab.atlas.devices.core.domain.iot.commands.CommandPart;
import gr.esdalab.atlas.devices.core.domain.iot.commands.IotCommand;
import gr.esdalab.atlas.devices.core.domain.vehicles.Vehicle;
import lombok.Getter;
import lombok.NonNull;

/**
 * Abstract class re-presenting commands running on Vehicles.
 */
@Getter
public class VehicleCommand<T extends Vehicle> extends IotCommand {

    /**
     * A local identifier of the vehicle command.
     */
    private int commandId;

    /**
     * Vehicle information for the command.
     */
    private final T vehicle;

    /**
     * The command parts.
     */
    private final Part<CommandPart<String>> part;

    /**
     * Required Arguments Constructor.
     * @param vehicle
     * @param part
     */
    public VehicleCommand(@NonNull final T vehicle,
                          @NonNull final Part<CommandPart<String>> part) {
        super(part.getData().getType(), IotCommandStatus.PENDING);
        this.vehicle = vehicle;
        this.part = part;
    }

    /**
     * All Arguments Constructor.
     * @param vehicle
     * @param part
     */
    public VehicleCommand(final int commandId,
                          @NonNull final IotCommandStatus status,
                          @NonNull final T vehicle,
                          @NonNull final Part<CommandPart<String>> part) {
        super(part.getData().getType(), status);
        this.commandId = commandId;
        this.vehicle = vehicle;
        this.part = part;
    }

}
