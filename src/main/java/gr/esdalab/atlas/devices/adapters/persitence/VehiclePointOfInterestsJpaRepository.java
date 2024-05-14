package gr.esdalab.atlas.devices.adapters.persitence;

import gr.esdalab.atlas.devices.adapters.persitence.entities.LocationJpa;
import gr.esdalab.atlas.devices.adapters.persitence.entities.VehicleJpa;
import gr.esdalab.atlas.devices.adapters.persitence.entities.VehiclePOIJpa;
import gr.esdalab.atlas.devices.adapters.persitence.entities.embeddable.VehicleCompartmentPrimary;
import gr.esdalab.atlas.devices.adapters.persitence.entities.embeddable.VehicleLocationPrimary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehiclePointOfInterestsJpaRepository extends JpaRepository<VehiclePOIJpa, VehicleLocationPrimary> {

    /**
     *
     * @param locationJpa
     * @return
     */
    List<VehiclePOIJpa> findByLocation(LocationJpa locationJpa);

    /**
     *
     * @param vehicleJpa
     * @return
     */
    List<VehiclePOIJpa> findByVehicle(VehicleJpa vehicleJpa);

}
