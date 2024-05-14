package gr.esdalab.atlas.devices.core.domain.repositories;

import gr.esdalab.atlas.devices.core.domain.robots.Robot;

import java.util.List;
import java.util.Optional;

/**
 * Re-presenting a repository for robots.
 */
public interface RobotsRepository {

    /**
     * List Robots
     * @return
     */
    List<Robot.CreatedRobot> list();

    /**
     * Get Robot
     * @return
     */
    Optional<Robot.CreatedRobot> getRobot(int robotId);

    /**
     * Get Robot by Identity
     * @return
     */
    Optional<Robot.CreatedRobot> getRobot(String robotId);

    /**
     * Set robot online.
     * @param robotId
     */
    void online(int robotId);
}
