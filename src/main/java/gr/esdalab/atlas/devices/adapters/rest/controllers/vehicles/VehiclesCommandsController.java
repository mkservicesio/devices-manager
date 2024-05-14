package gr.esdalab.atlas.devices.adapters.rest.controllers.vehicles;

import gr.esdalab.atlas.devices.adapters.rest.RestEndpointsConfig;
import gr.esdalab.atlas.devices.adapters.rest.resources.in.vehicles.VehicleCommandInput;
import gr.esdalab.atlas.devices.adapters.rest.resources.inout.VehicleCommandPartInputOutput;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.vehicles.VehicleCommandOutput;
import gr.esdalab.atlas.devices.core.application.cases.CommandsUseCase;
import gr.esdalab.atlas.devices.core.application.factories.vehicles.VehicleCommandsFactory;
import gr.esdalab.atlas.devices.core.domain.vehicles.commands.VehicleCommand;
import gr.esdalab.atlas.devices.core.domain.vehicles.impl.OilVehicle;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.validation.Valid;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 *
 */
@RestController
@RequestMapping(RestEndpointsConfig.DEVICES_MANAGER_VEHICLES_COMMANDS_API)
@RequiredArgsConstructor
@Slf4j
public class VehiclesCommandsController {

	private final CommandsUseCase<VehicleCommand<OilVehicle.CreatedOilVehicle>> commandsUseCase;
	private final VehicleCommandsFactory vehicleCommandsFactory;

	/**
	 * Return a list with the vehicle commands to be executed.
	 * @param vehicleId
	 * @return
	 */
	@Operation(operationId = "getVehicleCommands", description = "Return a list with the vehicle commands to be executed.", tags = {"Vehicles Commands"})
	@ApiResponses(value = {})
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public Flux<VehicleCommandOutput> getVehiclesCommands(@PathVariable int vehicleId){
		return Flux.fromIterable(commandsUseCase.request(vehicleId)
				.stream()
				.map(it -> {
					return new VehicleCommandOutput(
							it.getCommandId(),
							it.getVehicle().getId(),
							it.getType(),
							it.getPart().getData().getContent().stream().map(part -> new VehicleCommandPartInputOutput(part.getField(), part.getData())).collect(Collectors.toList())
					);
				}).collect(Collectors.toList())
		);
	}

	/**
	 * Set a command to be executed to vehicle.
	 * @return
	 */
	@Operation(operationId = "setCommandToVehicle", description = "Set a command to be executed to vehicle.", tags = {"Vehicles Commands"})
	@ApiResponses(value = {})
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void setCommandToVehicle(@PathVariable int vehicleId,
									@Valid @RequestBody final VehicleCommandInput vehicleCommandInput){
		commandsUseCase.apply(vehicleCommandsFactory.generate(vehicleId, vehicleCommandInput));
	}

	/**
	 * Set a vehicle command as processed correctly from the applied vehicle.
	 * @param vehicleId
	 * @param commandId
	 * @return
	 */
	@Operation(operationId = "setCommandProcessed", description = "Set a vehicle command as processed.", tags = {"Vehicles Commands"})
	@ApiResponses(value = {})
	@GetMapping(value = "/{commandId}/success", produces = MediaType.APPLICATION_JSON_VALUE)
	public void setCommandProcessed(@PathVariable final int vehicleId,
									@PathVariable final int commandId){
		log.info("Going to set command Id {} as processed successfully. Vehicle {}", commandId, vehicleId);
		commandsUseCase.processed(vehicleId, commandId);
	}

	/**
	 * Delete a vehicle command if not already processed
	 * @param vehicleId
	 * @param commandId
	 * @return
	 */
	@Operation(operationId = "deleteVehicleCommand", description = "Delete a vehicle command.", tags = {"Vehicles Commands"})
	@ApiResponses(value = {})
	@DeleteMapping(value = "/{commandId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public void deleteVehicleCommand(@PathVariable final int vehicleId,
									 @PathVariable final int commandId){
		log.info("Going to delete command by Id {} for vehicle {}", commandId, vehicleId);
		commandsUseCase.delete(vehicleId, commandId);
	}

}
