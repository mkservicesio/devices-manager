package gr.esdalab.atlas.devices.core.domain.iot.commands;

import java.util.List;

/**
 * Marker interface that describing a part of a command.
 */
public interface CommandPart <T> {

    /**
     * Return the type of the part.
     * In many case determines also the type of the command.
     * @return
     */
    String getType();

    /**
     * Return the content
     * @return
     */
    List<IotCommand.Part<T>> getContent();

}
