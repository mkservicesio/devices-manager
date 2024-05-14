package gr.esdalab.atlas.devices.core.application.factories.vehicles;

import gr.esdalab.atlas.devices.adapters.rest.resources.in.vehicles.VehicleCommandInput;
import gr.esdalab.atlas.devices.common.errors.ErrorMessage;
import gr.esdalab.atlas.devices.core.application.exceptions.AtlasException;
import gr.esdalab.atlas.devices.core.domain.DomainError;
import gr.esdalab.atlas.devices.core.domain.iot.commands.CommandPart;
import gr.esdalab.atlas.devices.core.domain.iot.commands.IotCommand;
import gr.esdalab.atlas.devices.core.domain.repositories.VehiclesRepository;
import gr.esdalab.atlas.devices.core.domain.vehicles.commands.VehicleCommand;
import gr.esdalab.atlas.devices.core.domain.vehicles.commands.parts.VehicleMainUnitCommandPart;
import gr.esdalab.atlas.devices.core.domain.vehicles.commands.parts.VehicleMetrilockCommandPart;
import gr.esdalab.atlas.devices.core.domain.vehicles.impl.OilVehicle;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Factory class for vehicles.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class VehicleCommandsFactory {

    /**
     * Vehicles Repository.
     */
    private final VehiclesRepository vehiclesRepo;

    /**
     *
     * @param vehicleId
     * @param vehicleCommandInput
     * @return
     */
    public VehicleCommand<OilVehicle.CreatedOilVehicle> generate(final int vehicleId,
                                                                 @NonNull final VehicleCommandInput vehicleCommandInput){
        return new VehicleCommand<>(
                vehiclesRepo.getVehicle(vehicleId).orElseThrow(() -> new AtlasException.ResourceNotExistException(new DomainError.NotFoundDomainError(ErrorMessage.VEHICLE_NOT_EXIST.getMessage()))),
                generateCommandPart(vehicleCommandInput)
        );
    }

    /**
     * Generate Command
     *
     * @param vehicleCommandInput
     * @return
     */
    public IotCommand.Part<CommandPart<String>> generateCommandPart(@NonNull final VehicleCommandInput vehicleCommandInput) {
        final List<IotCommand.Part<String>> parts = vehicleCommandInput.getParts().stream().map(it -> new IotCommand.Part<>(it.getField(), it.getValue())).collect(Collectors.toList());
        return generateCommandPart(vehicleCommandInput.getType(), parts);
    }

    /**
     *
     * @param partType
     * @param parts
     * @return
     */
    public IotCommand.Part<CommandPart<String>> generateCommandPart(@NonNull final String partType,
                                                                            @NonNull final List<IotCommand.Part<String>> parts) {
        switch ( partType ){
            case "mu":
                return new IotCommand.Part<>(new VehicleMainUnitCommandPart(parts));
            case "ml":
                return new IotCommand.Part<>(new VehicleMetrilockCommandPart(parts));
            default:
                throw new AtlasException.MisConfigurationException(new DomainError.MisConfiguredDomainError(ErrorMessage.INVALID_VEHICLE_COMMAND.getMessage()));
        }
    }

}
