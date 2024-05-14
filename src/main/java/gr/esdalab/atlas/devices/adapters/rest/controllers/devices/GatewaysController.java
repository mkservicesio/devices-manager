package gr.esdalab.atlas.devices.adapters.rest.controllers.devices;

import gr.esdalab.atlas.devices.adapters.rest.RestEndpointsConfig;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.errors.ErrorOutput;
import gr.esdalab.atlas.devices.common.errors.ErrorMessage;
import gr.esdalab.atlas.devices.core.application.query.GatewaysQueryService;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.devices.GatewayOutput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(RestEndpointsConfig.DEVICES_MANAGER_GATEWAYS_API)
@RequiredArgsConstructor
public class GatewaysController {

	private final GatewaysQueryService gatewaysQuerySrv;

	@Operation(operationId = "listGateways", description = "List all gateways of the system", tags = {"Gateways"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "List Gateways")
	})
	@GetMapping
	public List<GatewayOutput> get(){
		return gatewaysQuerySrv.getGateways();
	}

	@Operation(operationId = "getGateway", description = "Get a specific gateway", tags = {"Gateways"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Get gateway"),
			@ApiResponse(
					responseCode = "404",
					description = "Error: Gateway not found",
					content = @Content(
							array = @ArraySchema(
									schema = @Schema( implementation = ErrorOutput.class)
							)
					)
			)
	})
	@GetMapping("/{gwId}")
	public GatewayOutput getById(@PathVariable int gwId){
		return gatewaysQuerySrv.getGateway(gwId).orElseThrow(() -> {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.GATEWAY_NOT_EXIST.getMessage());
		});
	}

}
