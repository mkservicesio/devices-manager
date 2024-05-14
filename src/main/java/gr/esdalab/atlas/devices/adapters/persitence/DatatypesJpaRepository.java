package gr.esdalab.atlas.devices.adapters.persitence;

import gr.esdalab.atlas.devices.adapters.persitence.entities.DatatypeJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DatatypesJpaRepository extends JpaRepository<DatatypeJpa, Integer> {

    /**
     *
     * @param identity
     * @return
     */
    Optional<DatatypeJpa> findByDatatype(String identity);

}
