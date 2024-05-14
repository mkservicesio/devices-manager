package gr.esdalab.atlas.devices.adapters.rest.controllers.devices;

import gr.esdalab.atlas.devices.adapters.rest.RestEndpointsConfig;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.devices.DeviceOutput;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.devices.RobotOutput;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.errors.ErrorOutput;
import gr.esdalab.atlas.devices.common.errors.ErrorMessage;
import gr.esdalab.atlas.devices.common.resources.in.HealthInput;
import gr.esdalab.atlas.devices.core.application.query.DevicesQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(RestEndpointsConfig.DEVICES_MANAGER_DEVICES_API)
@RequiredArgsConstructor
public class DevicesController {

	private final DevicesQueryService devicesQueryServ;

	/**
	 * Return a list of available devices.
	 * @return
	 */
	@Operation(operationId = "getDevices", description = "List Available Devices", tags = {"Devices"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "List of available devices."),
	})
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DeviceOutput> getDevices(){
		return devicesQueryServ.listDevices();
	}

	/**
	 * Return device information (Retrieved by ID)
	 * @param deviceId
	 * @return
	 */
	@Operation(operationId = "getDevice", description = "Get a specific device", tags = {"Devices"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Get Device"),
			@ApiResponse(
					responseCode = "404",
					description = "Error: Device not found",
					content = @Content(
							array = @ArraySchema(
									schema = @Schema( implementation = ErrorOutput.class)
							)
					)
			)
	})
	@GetMapping(value = "/{deviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Parameter(name = "deviceId", in = ParameterIn.PATH, description = "The device ID to request", example = "1", schema = @Schema(implementation = Integer.class))
	public DeviceOutput getById(@PathVariable int deviceId,
								@RequestParam(value = "ping", required = false, defaultValue = "false") final boolean ping){
		return devicesQueryServ.getDevice(deviceId, (ping) ? new HealthInput("-", Collections.emptyList()) : null )
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.DEVICE_NOT_EXIST.getMessage()));
	}
	
}
