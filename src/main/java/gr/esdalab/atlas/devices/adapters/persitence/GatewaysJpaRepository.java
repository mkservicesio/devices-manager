package gr.esdalab.atlas.devices.adapters.persitence;

import gr.esdalab.atlas.devices.adapters.persitence.entities.AppendageJpa;
import gr.esdalab.atlas.devices.adapters.persitence.entities.IotGatewayJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GatewaysJpaRepository extends JpaRepository<IotGatewayJpa, Integer>{

    /**
     *
     * @param identity
     * @return
     */
    Optional<IotGatewayJpa> findByIdentity(String identity);

    /**
     *
     * @param appendageId
     * @return
     */
    List<IotGatewayJpa> findByAppendageId(int appendageId);

}
