package gr.esdalab.atlas.devices.adapters.persitence;

import gr.esdalab.atlas.devices.adapters.persitence.entities.VehicleJpa;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehiclesJpaRepository extends PagingAndSortingRepository<VehicleJpa, Integer>, JpaSpecificationExecutor<VehicleJpa> {

    /**
     * Search vehicle by label.
     * @param label
     * @return
     */
    Optional<VehicleJpa> findByLabel(String label);
}
