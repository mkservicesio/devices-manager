package gr.esdalab.atlas.devices.adapters.rest.controllers.devices;

import gr.esdalab.atlas.devices.adapters.rest.RestEndpointsConfig;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.errors.ErrorOutput;
import gr.esdalab.atlas.devices.common.errors.ErrorMessage;
import gr.esdalab.atlas.devices.core.application.query.RobotsQueryService;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.devices.RobotOutput;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(RestEndpointsConfig.DEVICES_MANAGER_ROBOTS_API)
@RequiredArgsConstructor
public class RobotsController {

	private final RobotsQueryService robotsQuerySrv;

	/**
	 * Return a list of available robots.
	 * @return
	 */
	@Operation(operationId = "listRobots", description = "List all robots of the system", tags = {"Robots"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "List Robots")
	})
	@GetMapping
	public List<RobotOutput> get(){
		return robotsQuerySrv.getRobots();
	}

	/**
	 * Return robot information (Retrieved by ID)
	 * @param robotId
	 * @return
	 */
	@Operation(operationId = "getRobot", description = "Get a specific robot", tags = {"Robots"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Get Robot"),
			@ApiResponse(
					responseCode = "404",
					description = "Error: Robot not found",
					content = @Content(
							array = @ArraySchema(
									schema = @Schema( implementation = ErrorOutput.class)
							)
					)
			)
	})
	@GetMapping(value = "/{robotId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Parameter(name = "robotId", in = ParameterIn.PATH, description = "The robot ID to request", example = "1", schema = @Schema(implementation = Integer.class))
	public RobotOutput getById(@PathVariable int robotId){
		return robotsQuerySrv.getRobot(robotId).orElseThrow(() -> {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.ROBOT_NOT_EXIST.getMessage());
		});
	}
	
}
