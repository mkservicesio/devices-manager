package gr.esdalab.atlas.devices.adapters.rest.controllers;

import gr.esdalab.atlas.devices.adapters.rest.RestEndpointsConfig;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.errors.ErrorOutput;
import gr.esdalab.atlas.devices.common.errors.ErrorMessage;
import gr.esdalab.atlas.devices.core.application.query.DevicesQueryService;
import gr.esdalab.atlas.devices.core.application.query.GatewaysQueryService;
import gr.esdalab.atlas.devices.core.application.query.GenericQueryService;
import gr.esdalab.atlas.devices.core.application.query.RobotsQueryService;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.devices.DeviceOutput;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.devices.GatewayOutput;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.IdOutput;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.devices.RobotOutput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(RestEndpointsConfig.DEVICES_MANAGER_SEARCH_API)
@RequiredArgsConstructor
public class SearchController {

    private final DevicesQueryService devicesQueryServ;
    private final GenericQueryService genericQueryServ;
    private final GatewaysQueryService gatewaysQueryServ;
    private final RobotsQueryService robotQuerySrv;

    /**
     *
     * @param identity
     * @return
     */
    @Operation(operationId = "searchByRobot", description = "Search a robot by its URN", tags = {"Search"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get Robot"),
            @ApiResponse(responseCode = "404", description = "Robot not found")
    })
    @GetMapping("/robots")
    public RobotOutput searchByRobot(@RequestParam(name = "identity") final String identity){
        return robotQuerySrv.getRobot(identity).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Robot with identity["+identity+"] not found!");
        });
    }

    /**
     * Gateway by Identity
     * @param identity
     * @return
     */
    @Operation(operationId = "searchByGateway", description = "Search a gateway by its Identity", tags = {"Search"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return the Gateway"),
            @ApiResponse(responseCode = "404", description = "Gateway not found")
    })
    @GetMapping("/gateways")
    public GatewayOutput searchByGwAddress(@RequestParam(name = "identity") final String identity){
        return gatewaysQueryServ.getGateway(identity).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gateway with identity["+identity+"] not found!");
        });
    }

    /**
     * Datatype information by datatype.
     * @param datatype
     * @return
     */
    @Operation(operationId = "searchByDatatype", description = "Search datatype by its datatype label", tags = {"Search"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return the datatype information"),
            @ApiResponse(responseCode = "404", description = "Datatype not found.")
    })
    @GetMapping("/datatypes")
    public IdOutput.DatatypeOutput searchByDatatype(@RequestParam(name = "datatype") final String datatype){
        return genericQueryServ.getDatatype(datatype).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Datatype["+datatype+"] not found!");
        });
    }

    /**
     * Device information by address.
     * @param address
     * @return
     */
    @Operation(operationId = "searchByDeviceAddress", description = "Search device by its URN", tags = {"Search"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return the device information"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Error: Device not found.",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema( implementation = ErrorOutput.class)
                            )
                    )
            )
    })
    @GetMapping("/devices")
    public DeviceOutput searchByDeviceAddress(@RequestParam(name = "address") String address){
        return devicesQueryServ.getDevice(address).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.DEVICE_NOT_EXIST.getMessage());
        });
    }

}
