package gr.esdalab.atlas.devices.adapters.persitence;

import gr.esdalab.atlas.devices.adapters.persitence.entities.AppendageJpa;
import gr.esdalab.atlas.devices.adapters.persitence.entities.LocationJpa;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationsJpaRepository extends PagingAndSortingRepository<LocationJpa, Integer>, JpaSpecificationExecutor<LocationJpa> {

    /**
     * Find by identity.
     * @param identity
     * @return
     */
    Optional<LocationJpa> findByIdentity(String identity);

    /**
     *
     * @param appendage
     * @return
     */
    Optional<LocationJpa> findByAppendage(AppendageJpa appendage);

    /**
     * 
     * @param isIndoor
     * @return
     */
    List<LocationJpa> findByIndoor(boolean isIndoor);

}
