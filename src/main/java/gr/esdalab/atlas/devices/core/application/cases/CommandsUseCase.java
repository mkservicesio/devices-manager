package gr.esdalab.atlas.devices.core.application.cases;

import java.util.List;

/**
 * General Class for IoT Commands use-cases.
 */
public interface CommandsUseCase<T>{

    /**
     * Apply the command
     * @param command
     */
    void apply(T command);

    /**
     * Request & return a list of commands for execute.
     * @param vehicleId
     * @return
     */
    List<T> request(int vehicleId);

    /**
     *  Set the status of the command as in progress.
     * @param vehicleId
     * @param commandId
     */
    void processed(int vehicleId, int commandId);

    /**
     * Delete a specific command (only when in state pending).
     * @param vehicleId
     * @param commandId
     */
    void delete(int vehicleId, int commandId);
}
