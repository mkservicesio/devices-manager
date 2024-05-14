package gr.esdalab.atlas.devices.adapters.rest.controllers.vehicles;

import gr.esdalab.atlas.devices.adapters.rest.RestEndpointsConfig;
import gr.esdalab.atlas.devices.adapters.rest.resources.in.LocationInput;
import gr.esdalab.atlas.devices.adapters.rest.resources.in.vehicles.VehicleSensorThresholdInput;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.BasicVehicleOutput;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.PageableOutput;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.errors.ErrorOutput;
import gr.esdalab.atlas.devices.common.errors.ErrorMessage;
import gr.esdalab.atlas.devices.core.application.cases.VehiclesCreationUseCase;
import gr.esdalab.atlas.devices.core.application.query.VehiclesQueryService;
import gr.esdalab.atlas.devices.adapters.rest.resources.in.vehicles.VehicleInput;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.vehicles.VehicleOutput;
import gr.esdalab.atlas.devices.core.domain.vehicles.Vehicle;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping(RestEndpointsConfig.DEVICES_MANAGER_VEHICLES_API)
@RequiredArgsConstructor
public class VehiclesController {

	private final VehiclesCreationUseCase vehicleCreationUseCase;
	private final VehiclesQueryService vehiclesQueryService;

	/**
	 * Create new vehicle
	 * @return
	 */
	@Operation(operationId = "createVehicle", description = "Create a new vehicle", tags = {"Vehicles"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Create new vehicle."),
			@ApiResponse(
					responseCode = "400",
					description = "Error: Invalid input data.",
					content = @Content(
							array = @ArraySchema(
									schema = @Schema( implementation = ErrorOutput.class)
							)
					)
			)
	})
	@RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public VehicleOutput createVehicle(@Valid @RequestBody VehicleInput.VehicleCreationInput vehicleInput){
		return vehicleCreationUseCase.create(vehicleInput).orElseThrow(() -> {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to create Vehicle.");
		});
	}

	/**
	 * Update Basic information of a vehicle.
	 * @return
	 */
	@Operation(operationId = "updateVehicle", description = "Update a vehicle.", tags = {"Vehicles"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Update an existing vehicle."),
	})
	@RequestMapping(value = "/{vehicleId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public void updateVehicle(@PathVariable int vehicleId,
							  @Valid @RequestBody VehicleInput.VehicleUpdateInput vehicleInput){
		vehicleCreationUseCase.update(vehicleId, vehicleInput);
	}

	/**
	 * Return available vehicles
	 * @return
	 */
	@Operation(operationId = "listVehicles", description = "Get all available vehicles", tags = {"Vehicles"})
	@Parameters(
			value = {
					@Parameter(
							name = "page",
							in = ParameterIn.QUERY,
							description = "Pagination Page, Default to 0",
							schema = @Schema( type = "integer")
					),
					@Parameter(
							name = "pageSize",
							in = ParameterIn.QUERY,
							description = "Pagination Query Size, Default to 50",
							schema = @Schema( type = "integer")
					),
					@Parameter(
							name = "sort",
							in = ParameterIn.QUERY,
							description = "Sorting Query Parameter",
							example = "label,asc",
							schema = @Schema( type = "string")
					),
					@Parameter(
							name = "owner",
							in = ParameterIn.QUERY,
							description = "Filter vehicles by owner ID, Equal operation.",
							schema = @Schema( type = "integer")
					),
					@Parameter(
							name = "status",
							in = ParameterIn.QUERY,
							description = "Filter vehicles by status. Allowed values: INACTIVE, ACTIVE",
							schema = @Schema( type = "Enum", implementation = Vehicle.VehicleStatus.class)
					),
					@Parameter(
							name = "label",
							in = ParameterIn.QUERY,
							description = "Filter vehicles by its label, Like operation.",
							schema = @Schema( type = "string")
					),
			}
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "List available vehicles",
					content = @Content( array = @ArraySchema( schema = @Schema( implementation = PageableOutput.class))))
	})
	@GetMapping("")
	public PageableOutput<BasicVehicleOutput> listVehicles(@RequestParam(required = false) final Map<String, String> query){
		return vehiclesQueryService.listVehicles(query);
	}

	/**
	 * Return Vehicle information (Retrieved by ID)
	 * @param vehicleId
	 * @return
	 */
	@Operation(operationId = "getVehicle", description = "Get a specific vehicle", tags = {"Vehicles"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Get Vehicle"),
			@ApiResponse(
					responseCode = "404",
					description = "Error: Vehicle not found",
					content = @Content(
							array = @ArraySchema(
									schema = @Schema( implementation = ErrorOutput.class)
							)
					)
			)
	})
	@GetMapping("/{vehicleId}")
	public VehicleOutput getById(@PathVariable int vehicleId){
		return vehiclesQueryService.getVehicle(vehicleId).orElseThrow(() -> {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.VEHICLE_NOT_EXIST.getMessage());
		});
	}

	/**
	 * Deactivate vehicle (by ID)
	 * @param vehicleId
	 * @return
	 */
	@Operation(operationId = "deactivateVehicle", description = "De-Activate specific vehicle", tags = {"Vehicles"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Deactivation OK"),
			@ApiResponse(
					responseCode = "404",
					description = "Error: Vehicle not found",
					content = @Content(
							array = @ArraySchema(
									schema = @Schema( implementation = ErrorOutput.class)
							)
					)
			)
	})
	@DeleteMapping("/{vehicleId}")
	public void deactivateVehicleById(@PathVariable int vehicleId){
		vehicleCreationUseCase.deactivate(vehicleId);
	}

	/**
	 * Deactivate vehicle (by ID)
	 * @param vehicleId
	 * @return
	 */
	@Operation(operationId = "activateVehicle", description = "Activate specific vehicle", tags = {"Vehicles"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Activation OK"),
			@ApiResponse(
					responseCode = "404",
					description = "Error: Vehicle not found",
					content = @Content(
							array = @ArraySchema(
									schema = @Schema( implementation = ErrorOutput.class)
							)
					)
			)
	})
	@GetMapping("/{vehicleId}/activate")
	public void activateVehicleById(@PathVariable int vehicleId){
		vehicleCreationUseCase.activate(vehicleId);
	}

	/**
	 * Update Vehicle Compartment Sensor Thresholds
	 * @return
	 */
	@Operation(operationId = "updateSensorThreshold", description = "Update thresholds on vehicle compartments.", tags = {"Vehicles"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Update vehicle sensor threshold."),
			@ApiResponse(
					responseCode = "404",
					description = "Error: Vehicle not found",
					content = @Content(
							array = @ArraySchema(
									schema = @Schema( implementation = ErrorOutput.class)
							)
					)
			)
	})
	@RequestMapping(value = "/{vehicleId}/thresholds", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void updateSensorThreshold(@PathVariable int vehicleId,
									  @Valid @RequestBody VehicleSensorThresholdInput thresholdInput){
		vehicleCreationUseCase.setThreshold(vehicleId, thresholdInput);
	}

	/**
	 * Apply a new point of interest in the Vehicle
	 * @return
	 */
	@Operation(operationId = "addPointOfInterest", description = "Add a new point of interest in Vehicle.", tags = {"Vehicles"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Point of interest added successfully."),
			@ApiResponse(
					responseCode = "404",
					description = "Error: Vehicle not found",
					content = @Content(
							array = @ArraySchema(
									schema = @Schema( implementation = ErrorOutput.class)
							)
					)
			)
	})
	@RequestMapping(value = "/{vehicleId}/pois", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void addPointOfInterest(@PathVariable int vehicleId,
								   @Valid @RequestBody LocationInput.DraftLocationInput locationInput){
		vehicleCreationUseCase.addPointOfInterest(vehicleId, locationInput);
	}

	/**
	 * Remove an existing point of interest in the Vehicle
	 * @return
	 */
	@Operation(operationId = "removePointOfInterest", description = "Remove an existing point of interest in Vehicle.", tags = {"Vehicles"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Point of interest removed successfully."),
			@ApiResponse(
					responseCode = "404",
					description = "Error: Vehicle not found",
					content = @Content(
							array = @ArraySchema(
									schema = @Schema( implementation = ErrorOutput.class)
							)
					)
			)
	})
	@RequestMapping(value = "/{vehicleId}/pois/{locationId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void removePointOfInterest(@PathVariable int vehicleId,
									  @PathVariable int locationId){
		vehicleCreationUseCase.removePointOfInterest(vehicleId, locationId);
	}

}
