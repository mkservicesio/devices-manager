package gr.esdalab.atlas.devices.adapters.persitence;

import gr.esdalab.atlas.devices.adapters.persitence.entities.NetworkLayerJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NetworkLayersJpaRepository extends JpaRepository<NetworkLayerJpa, Integer> { }
