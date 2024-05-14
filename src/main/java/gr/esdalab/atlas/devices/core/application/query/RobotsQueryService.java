package gr.esdalab.atlas.devices.core.application.query;

import gr.esdalab.atlas.devices.adapters.rest.resources.out.devices.RobotOutput;

import java.util.List;
import java.util.Optional;

/**
 * Service for query robots information.
 */
public interface RobotsQueryService {

    /**
     * Return all robots
     * @return
     */
    List<RobotOutput> getRobots();

    /**
     * Get Specific Robot by its ID
     * @param robotId
     * @return
     */
    Optional<RobotOutput> getRobot(int robotId);

    /**
     * Get Specific Robot by its identity
     * @param robotId
     * @return
     */
    Optional<RobotOutput> getRobot(String robotId);

    /**
     * Find all the robots that is on the appendage ID.
     * @param appandageId - The appendage ID
     * @return
     */
    List<RobotOutput> getRobotsByAppendageId(int appandageId);
}
