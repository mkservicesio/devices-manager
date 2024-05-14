package gr.esdalab.atlas.devices.core.domain.common;

import gr.esdalab.atlas.devices.adapters.persitence.entities.AppendageJpa;
import gr.esdalab.atlas.devices.adapters.persitence.entities.RobotJpa;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


/**
 * In order to connect different component together we need a common identifier.
 */
@Getter
@RequiredArgsConstructor
public class Appendage {
    private final int appendageId;

    /**
     * Map method from Robot.
     * @return
     */
    public static Appendage from(@NonNull final AppendageJpa appendageJpa){
        return new Appendage(appendageJpa.getId());
    }

}
