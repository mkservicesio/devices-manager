package gr.esdalab.atlas.devices.adapters.rest.controllers;

import gr.esdalab.atlas.devices.adapters.rest.RestEndpointsConfig;
import gr.esdalab.atlas.devices.adapters.rest.resources.in.LocationInput;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.*;
import gr.esdalab.atlas.devices.core.application.cases.LocationsUseCase;
import gr.esdalab.atlas.devices.core.application.query.LocationsQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping(RestEndpointsConfig.DEVICES_MANAGER_LOCATIONS_API)
@RequiredArgsConstructor
public class LocationsController {

	private final LocationsQueryService locationsQuerySrv;
	private final LocationsUseCase locationsUseCase;

	@Operation(operationId = "getLocations", description = "List all available locations of the system", tags = {"Locations"})
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
							name = "identity",
							in = ParameterIn.QUERY,
							description = "Filter locations by their identity, Like operation.",
							schema = @Schema( type = "string")
					),
					@Parameter(
							name = "type",
							in = ParameterIn.QUERY,
							description = "Filter locations by their type, EQUAL operation.",
							schema = @Schema( implementation = LocationInput.LocationType.class)
					),
					@Parameter(
							name = "label",
							in = ParameterIn.QUERY,
							description = "Filter locations by their label, Like operation.",
							schema = @Schema( type = "string")
					),
			}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "List available locations")
	})
	@GetMapping
	public PageableOutput<LocationOutput> getLocations(@RequestParam(required = false) final Map<String, String> query){
		return locationsQuerySrv.getLocations(query);
	}

	@Operation(operationId = "getLocationById", description = "Get a specific location information", tags = {"Locations"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Get location"),
			@ApiResponse(responseCode = "404", description = "Location not found")
	})
	@GetMapping("/{locationId}")
	public LocationOutput getLocationById(@PathVariable int locationId){
		return locationsQuerySrv.getLocation(locationId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Location with ID[" + locationId + "] not found!"));
	}

	@Operation(operationId = "deleteLocationById", description = "Delete a specific location", tags = {"Locations"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Location deleted successfully!!"),
	})
	@DeleteMapping("/{locationId}")
	public void deleteLocationById(@PathVariable int locationId){
		locationsUseCase.delete(locationId);
	}

	/**
	 * Create new location
	 * @return - Location Information.
	 */
	@Operation(operationId = "createLocation", description = "Create a new location", tags = {"Locations"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "A new location is created."),
	})
	@PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public LocationOutput createLocation(@Valid @RequestBody LocationInput.DraftLocationInput locationInput){
		return locationsUseCase.create(locationInput).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to create Location."));
	}

	@Operation(operationId = "updateLocation", description = "Update an existing location", tags = {"Locations"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The location is updated."),
	})
	@PutMapping(value = "/{locationId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public LocationOutput updateLocation(@PathVariable int locationId,
										 @Valid @RequestBody LocationInput.CreatedLocationInput locationInput){
		return locationsUseCase.update(locationId, locationInput).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to update location."));
	}

	@Operation(operationId = "getLocationMap", description = "Get the re-presentation map information of a specific location", tags = {"Locations"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Get Location Map"),
			@ApiResponse(responseCode = "404", description = "Location not found")
	})
	@GetMapping("/{locationId}/map")
	public ResponseEntity<LocationMapOutput> getLocationMap(@PathVariable int locationId){
		return locationsQuerySrv.getMapOf(locationId)
				.map(locationMap -> ResponseEntity.status(HttpStatus.OK).body(locationMap))
				.orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
	}

}
