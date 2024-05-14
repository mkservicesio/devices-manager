package gr.esdalab.atlas.devices.adapters.rest.controllers;

import gr.esdalab.atlas.devices.adapters.rest.RestEndpointsConfig;
import gr.esdalab.atlas.devices.common.errors.ErrorMessage;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.AppendageOutput;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.errors.ErrorOutput;
import gr.esdalab.atlas.devices.core.application.query.AppendagesQueryService;
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

@RestController
@RequestMapping(RestEndpointsConfig.DEVICES_MANAGER_APPENDAGES_API)
@RequiredArgsConstructor
public class AppendagesController {

	private final AppendagesQueryService appendagesQueryServ;

	/**
	 * Return the information about a specific
	 * @param appendageId
	 * @return
	 */
	@Operation(operationId = "getAppendage", description = "Get a specific appendage", tags = {"Appendages"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Get appendage"),
			@ApiResponse(
					responseCode = "404",
					description = "Error: Appendage not found",
					content = @Content(
							array = @ArraySchema(
									schema = @Schema( implementation = ErrorOutput.class)
							)
					)
			)
	})
	@GetMapping("/{appendageId}")
	public AppendageOutput getById(@PathVariable int appendageId){
		return appendagesQueryServ.getAppendage(appendageId).orElseThrow(() -> {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.APPENDAGE_NOT_EXIST.getMessage());
		});
	}
	
}
