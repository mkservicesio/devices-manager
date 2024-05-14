package gr.esdalab.atlas.devices.adapters.rest.controllers;

import gr.esdalab.atlas.devices.adapters.rest.RestEndpointsConfig;
import gr.esdalab.atlas.devices.adapters.rest.resources.inout.ThresholdInputOutput;
import gr.esdalab.atlas.devices.common.errors.ErrorMessage;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.IdOutput;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.errors.ErrorOutput;
import gr.esdalab.atlas.devices.core.application.cases.DatatypesUseCase;
import gr.esdalab.atlas.devices.core.application.query.DatatypesQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(RestEndpointsConfig.DEVICES_MANAGER_DATATYPES_API)
@RequiredArgsConstructor
public class DatatypesController {

	private final DatatypesQueryService datatypesQueryServ;
	private final DatatypesUseCase datatypesUseCase;

	/**
	 * Return a list with the available datatypes.
	 * @return
	 */
	@Operation(operationId = "listDatatypes", description = "List all available data-types", tags = { "Datatypes" })
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "List all available data-types",
					content = @Content(
							array = @ArraySchema(
									schema = @Schema( implementation = IdOutput.DatatypeThresholdOutput.class)
							)
					)
			)
	})
	@GetMapping
	public List<IdOutput.DatatypeThresholdOutput> list(){
		return datatypesQueryServ.getDatatypes();
	}

	/**
	 * Get Datatype by ID.
	 * @return
	 */
	@Operation(operationId = "getDatatype", description = "Get specific datatype", tags = { "Datatypes" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Get information of specific datatype."),
			@ApiResponse(
					responseCode = "404",
					description = "Error: Datatype not found",
					content = @Content(
							array = @ArraySchema(
									schema = @Schema( implementation = ErrorOutput.class)
							)
					)
			)
	})
	@GetMapping("/{datatypeId}")
	public IdOutput.DatatypeThresholdOutput getDatatypeById(@PathVariable final int datatypeId){
		return datatypesQueryServ.getDatatype(datatypeId).orElseThrow(() -> {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.DATATYPE_NOT_EXIST.getMessage());
		});
	}

	/**
	 * Update datatype default threshold
	 * @return
	 */
	@Operation(operationId = "updateDatatypeThreshold", description = "Update default datatype threshold.", tags = { "Datatypes" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Update default datatype threshold."),
			@ApiResponse(
					responseCode = "404",
					description = "Error: Datatype not found",
					content = @Content(
							array = @ArraySchema(
									schema = @Schema( implementation = ErrorOutput.class)
							)
					)
			)
	})
	@PutMapping("/{datatypeId}/threshold")
	public ResponseEntity<Void> updateDatatypeThresholdById(@PathVariable final int datatypeId,
											 @RequestBody @Valid final ThresholdInputOutput threshold){
		datatypesUseCase.updateThreshold(datatypeId, threshold);
		return ResponseEntity.noContent().build();
	}
	
}
