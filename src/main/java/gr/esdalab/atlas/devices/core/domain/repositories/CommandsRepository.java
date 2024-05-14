package gr.esdalab.atlas.devices.core.domain.repositories;

import gr.esdalab.atlas.devices.common.IotCommandStatus;

import java.util.List;

/**
 * Re-presenting a generic interface for persisting commands.
 */
public interface CommandsRepository<T, R> {

    /**
     * Persist a command
     * @param iotObject
     * @param commandType
     * @param payload
     */
    void persist(T iotObject, String commandType, R payload);

    /**
     * Return pending commands for the given object.
     * @return
     */
    List<R> getCommands(T iotObject);

    /**
     * Retutn the command
     * @param iotObject - The object that the command will be applied.
     * @return - The command
     */
    R getCommand(int commandId, T iotObject);

    /**
     * Change the state of the command.
     * @param commandId
     * @param inProgress
     */
    void toState(int commandId, IotCommandStatus inProgress);

    /**
     * Remove Command
     * @param commandId - The comand ID.
     */
    void remove(int commandId);
}
