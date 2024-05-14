package gr.esdalab.atlas.devices.adapters.persitence;

import gr.esdalab.atlas.devices.adapters.persitence.entities.DeviceDatatypeJpa;
import gr.esdalab.atlas.devices.adapters.persitence.entities.DeviceJpa;
import gr.esdalab.atlas.devices.adapters.persitence.entities.embeddable.DeviceDatatypePrimary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceDatatypeJpaRepository extends JpaRepository<DeviceDatatypeJpa, DeviceDatatypePrimary> {

    /**
     *
     * @param deviceJpa
     * @return
     */
    List<DeviceDatatypeJpa> findByDevice(DeviceJpa deviceJpa);

}
