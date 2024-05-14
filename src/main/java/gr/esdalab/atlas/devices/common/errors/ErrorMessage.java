package gr.esdalab.atlas.devices.common.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Application code error message.
 */
@Getter
@RequiredArgsConstructor
public enum ErrorMessage {

    APPENDAGE_NOT_EXIST("404", "error.appendage.not.found"),
    DATATYPE_NOT_EXIST("404", "error.datatype.not.found"),
    VEHICLE_NOT_EXIST("404", "error.vehicle.not.found"),
    VEHICLE_ALREADY_EXIST("422", "error.vehicle.already.exist"),
    VEHICLE_COMMAND_NOT_EXIST("404", "error.vehicle.command.not.found"),
    LOCATION_NOT_EXIST("404", "error.location.not.found"),
    DEVICE_NOT_EXIST("404", "error.device.not.found"),
    ROBOT_NOT_EXIST("404", "error.robot.not.found"),
    GATEWAY_NOT_EXIST("404", "error.gateway.not.found"),
    VEHICLES_COMPARTMENTS_LIMIT_REACHED("400", "error.vehicles.compartments.limit.reached"),
    VEHICLE_COMPARTMENT_NOT_EXIST("400", "error.vehicles.compartment.not.exist"),
    VEHICLE_COMPARTMENT_DEVICE_NOT_EXIST("400", "error.vehicles.compartment.device.not.exist"),
    VEHICLE_COMPARTMENT_DEVICE_DATATYPE_NOT_EXIST("400", "error.vehicles.compartment.device.datatype.not.exist"),
    INVALID_VEHICLE_COMMAND("422", "error.vehicles.commands.invalid"),
    VEHICLE_COMMAND_IN_WRONG_STATUS("422", "error.vehicles.commands.wrong.status");

    private final String code;
    private final String message;

}
