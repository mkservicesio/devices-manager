package gr.esdalab.atlas.devices.adapters.persitence.impl.commands;

import gr.esdalab.atlas.devices.adapters.persitence.VehicleCommandsJpaRepository;
import gr.esdalab.atlas.devices.adapters.persitence.VehiclesJpaRepository;
import gr.esdalab.atlas.devices.adapters.persitence.entities.VehicleCommandJpa;
import gr.esdalab.atlas.devices.adapters.persitence.entities.VehicleJpa;
import gr.esdalab.atlas.devices.common.IotCommandStatus;
import gr.esdalab.atlas.devices.common.errors.ErrorMessage;
import gr.esdalab.atlas.devices.core.application.exceptions.AtlasException;
import gr.esdalab.atlas.devices.core.application.factories.vehicles.VehicleCommandsFactory;
import gr.esdalab.atlas.devices.core.domain.DomainError;
import gr.esdalab.atlas.devices.core.domain.repositories.CommandsRepository;
import gr.esdalab.atlas.devices.core.domain.vehicles.commands.VehicleCommand;
import gr.esdalab.atlas.devices.core.domain.vehicles.impl.OilVehicle;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehiclesCommandsRepositoryImpl implements CommandsRepository<OilVehicle.CreatedOilVehicle, VehicleCommand<OilVehicle.CreatedOilVehicle>> {

    private final VehiclesJpaRepository vehiclesJpaRepo;
    private final VehicleCommandsJpaRepository vehicleCommandsJpaRepo;
    private final VehicleCommandsFactory commandsFactory;

    @Override
    public void persist(@NonNull final OilVehicle.CreatedOilVehicle vehicle,
                        @NonNull final String commandType,
                        @NonNull final VehicleCommand<OilVehicle.CreatedOilVehicle> command) {
        final VehicleJpa vehicleJpa = vehiclesJpaRepo.findById(vehicle.getId())
                .orElseThrow(() -> new AtlasException.ResourceNotExistException(new DomainError.NotFoundDomainError(ErrorMessage.VEHICLE_NOT_EXIST.getMessage())));
        vehicleCommandsJpaRepo.save(new VehicleCommandJpa(IotCommandStatus.PENDING, commandType, vehicleJpa, command.getPart().getData().getContent()));
    }

    @Override
    public List<VehicleCommand<OilVehicle.CreatedOilVehicle>> getCommands(@NonNull final OilVehicle.CreatedOilVehicle vehicle) {
        return vehicleCommandsJpaRepo.findByVehicleJpaIdAndStatus(vehicle.getId(), IotCommandStatus.PENDING)
                .stream()
                .map(it -> new VehicleCommand<>(it.getCommandId(), it.getStatus(), vehicle, commandsFactory.generateCommandPart(it.getType().toLowerCase(), it.getParts())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void toState(final int commandId,
                        @NonNull final IotCommandStatus status) {
        vehicleCommandsJpaRepo.findById(commandId).ifPresent(commandJpa -> {
            vehicleCommandsJpaRepo.save(commandJpa.onState(status));
        });
    }

    @Override
    @Transactional
    public void remove(final int commandId) {
        vehicleCommandsJpaRepo.deleteById(commandId);
    }

    @Override
    public VehicleCommand<OilVehicle.CreatedOilVehicle> getCommand(final int commandId,
                                                                   @NonNull final OilVehicle.CreatedOilVehicle vehicle) {
        return vehicleCommandsJpaRepo.findByCommandIdAndVehicleJpaId(commandId, vehicle.getId())
                .map(it -> new VehicleCommand<>(it.getCommandId(), it.getStatus(), vehicle, commandsFactory.generateCommandPart(it.getType().toLowerCase(), it.getParts())))
                .orElseThrow(() -> new AtlasException.ResourceNotExistException(new DomainError.NotFoundDomainError(ErrorMessage.VEHICLE_COMMAND_NOT_EXIST.getMessage())));
    }
}
