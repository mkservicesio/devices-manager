package gr.esdalab.atlas.devices.adapters.rest.resources.out.devices;

import com.fasterxml.jackson.annotation.JsonInclude;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.IdOutput;
import gr.esdalab.atlas.devices.core.domain.Health;
import gr.esdalab.atlas.devices.core.domain.robots.Robot;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RobotOutput {

    /**
     * Robot ID
     */
    @Schema(example = "1", required = true, description = "Unique identifier of the robot")
    private final int robotId;

    /**
     * Robot Identity
     */
    private final String urn;

    /**
     * Robot Device Type
     */
    private final String type;

    /**
     * Robot Device Mode
     */
    private final String mode;

    /**
     * The appendage identifier of the device.
     */
    private final RobotAppendageOutput appendage;

    /**
     * Last online.
     */
    private final Date onlineAt;

    /**
     * Simplified version of appendage for device.
     */
    public static class RobotAppendageOutput extends IdOutput<Integer> {

        /**
         *  Required arguments constructor.
         */
        public RobotAppendageOutput(final int appendageId){
            super(appendageId, "-");
        }
    }

    /**
     * Generated a robot
     * @param robot
     * @return
     */
    public static RobotOutput from(@NonNull final Robot.CreatedRobot robot){
        return RobotOutput.builder()
                .robotId(robot.getId())
                .urn(robot.getUrn())
                .type(robot.getType().name())
                .mode(robot.getMode().name())
                .appendage(new RobotAppendageOutput(robot.getAppendage().getAppendageId()))
                .onlineAt(Optional.ofNullable(robot.getHealth()).map(Health.Device::getOnlineAt).orElse(null))
                .build();
    }

}
