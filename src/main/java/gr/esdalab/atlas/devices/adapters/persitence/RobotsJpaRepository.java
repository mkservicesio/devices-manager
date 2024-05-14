package gr.esdalab.atlas.devices.adapters.persitence;

import gr.esdalab.atlas.devices.adapters.persitence.entities.RobotJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RobotsJpaRepository extends JpaRepository<RobotJpa, Integer> {

    /**
     * Find by Identity
     * @param identity
     * @return
     */
    Optional<RobotJpa> findByIdentity(String identity);

}
