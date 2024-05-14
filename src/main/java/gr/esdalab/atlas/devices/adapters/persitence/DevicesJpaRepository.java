package gr.esdalab.atlas.devices.adapters.persitence;

import gr.esdalab.atlas.devices.adapters.persitence.entities.DeviceJpa;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DevicesJpaRepository extends PagingAndSortingRepository<DeviceJpa, Integer>, JpaSpecificationExecutor<DeviceJpa>{

    /**
     * Find a device by its URN.
     * @param identity
     * @return
     */
    Optional<DeviceJpa> findTop1ByIdentity(String identity);

    /**
     * Get all devices by appendageId
     * @param appendageId
     * @return
     */
    List<DeviceJpa> findByAppendageId(int appendageId);

    /**
     * Get all devices
     * @return
     */
    @Query("SELECT d FROM DeviceJpa d")
    List<DeviceJpa> getAll();

}
