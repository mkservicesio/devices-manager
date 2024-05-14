package gr.esdalab.atlas.devices.adapters.persitence;

import gr.esdalab.atlas.devices.adapters.persitence.entities.VehicleCommandJpa;
import gr.esdalab.atlas.devices.common.IotCommandStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleCommandsJpaRepository extends JpaRepository<VehicleCommandJpa, Integer> {

    /**
     *
     */
    List<VehicleCommandJpa> findByVehicleJpaIdAndStatus(int vehicleId, IotCommandStatus status);

    /**
     *
     * @return
     */
    Optional<VehicleCommandJpa> findByCommandIdAndVehicleJpaId(int commandId, int vehicleId);

}
