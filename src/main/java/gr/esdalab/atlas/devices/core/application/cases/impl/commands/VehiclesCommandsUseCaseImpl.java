package gr.esdalab.atlas.devices.core.application.cases.impl.commands;

import gr.esdalab.atlas.devices.common.IotCommandStatus;
import gr.esdalab.atlas.devices.common.errors.ErrorMessage;
import gr.esdalab.atlas.devices.core.application.cases.CommandsUseCase;
import gr.esdalab.atlas.devices.core.application.exceptions.AtlasException;
import gr.esdalab.atlas.devices.core.domain.DomainError;
import gr.esdalab.atlas.devices.core.domain.iot.commands.IotCommand;
import gr.esdalab.atlas.devices.core.domain.repositories.CommandsRepository;
import gr.esdalab.atlas.devices.core.domain.repositories.VehiclesRepository;
import gr.esdalab.atlas.devices.core.domain.vehicles.commands.VehicleCommand;
import gr.esdalab.atlas.devices.core.domain.vehicles.impl.OilVehicle;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Vehicles Commands Use Case.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class VehiclesCommandsUseCaseImpl implements CommandsUseCase<VehicleCommand<OilVehicle.CreatedOilVehicle>> {

    private final CommandsRepository<OilVehicle.CreatedOilVehicle, VehicleCommand<OilVehicle.CreatedOilVehicle>> vehiclesCommandsRepo;
    private final VehiclesRepository vehiclesRepo;

    @Override
    public void apply(@NonNull final VehicleCommand<OilVehicle.CreatedOilVehicle> command) {
        if(command.getPart().getData() == null){
            throw new AtlasException.MisConfigurationException(new DomainError.MisConfiguredDomainError("Vehicle command part must extends IoT command."));
        }
        //TODO Include also validations.
        log.info("Going to apply command of type: {} on vehicle {}", command.getType(), command.getVehicle().getId());
        vehiclesCommandsRepo.persist(command.getVehicle(), command.getType(), command);
    }

    @Override
    @Transactional
    public List<VehicleCommand<OilVehicle.CreatedOilVehicle>> request(final int vehicleId) {
          final OilVehicle.CreatedOilVehicle oilVehicle = vehiclesRepo.getVehicle(vehicleId)
                .orElseThrow(() -> new AtlasException.ResourceNotExistException(new DomainError.NotFoundDomainError(ErrorMessage.VEHICLE_NOT_EXIST.getMessage())));
          return vehiclesCommandsRepo.getCommands(oilVehicle)
                  .stream()
                  .map(it -> {
                      vehiclesCommandsRepo.toState(it.getCommandId(), IotCommandStatus.IN_PROGRESS);
                      return it;
                  }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void processed(final int vehicleId,
                          final int commandId) {
        final OilVehicle.CreatedOilVehicle oilVehicle = vehiclesRepo.getVehicle(vehicleId)
                .orElseThrow(() -> new AtlasException.ResourceNotExistException(new DomainError.NotFoundDomainError(ErrorMessage.VEHICLE_NOT_EXIST.getMessage())));
        final VehicleCommand<OilVehicle.CreatedOilVehicle> command =vehiclesCommandsRepo.getCommand(commandId, oilVehicle);
        if( command.getStatus() != IotCommandStatus.IN_PROGRESS ){
            throw new AtlasException.DomainViolationException(List.of(new DomainError.RuleViolationDomainError(ErrorMessage.VEHICLE_COMMAND_IN_WRONG_STATUS.getMessage())));
        }
        vehiclesCommandsRepo.toState(command.getCommandId(), IotCommandStatus.APPLIED);
    }

    @Override
    public void delete(final int vehicleId,
                       final int commandId) {
        final OilVehicle.CreatedOilVehicle oilVehicle = vehiclesRepo.getVehicle(vehicleId)
                .orElseThrow(() -> new AtlasException.ResourceNotExistException(new DomainError.NotFoundDomainError(ErrorMessage.VEHICLE_NOT_EXIST.getMessage())));
        final VehicleCommand<OilVehicle.CreatedOilVehicle> command =vehiclesCommandsRepo.getCommand(commandId, oilVehicle);
        if( command.getStatus() != IotCommandStatus.PENDING ){
            throw new AtlasException.DomainViolationException(List.of(new DomainError.RuleViolationDomainError(ErrorMessage.VEHICLE_COMMAND_IN_WRONG_STATUS.getMessage())));
        }
        vehiclesCommandsRepo.remove(command.getCommandId());
    }

}
