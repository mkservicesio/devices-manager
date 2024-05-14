package gr.esdalab.atlas.devices.adapters.persitence;

import gr.esdalab.atlas.devices.adapters.persitence.entities.VehicleCompartmentJpa;
import gr.esdalab.atlas.devices.adapters.persitence.entities.VehicleJpa;
import gr.esdalab.atlas.devices.adapters.persitence.entities.embeddable.VehicleCompartmentPrimary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleCompartmentsJpaRepository extends JpaRepository<VehicleCompartmentJpa, VehicleCompartmentPrimary> {

    /**
     * Find total compartments of a vehicle.
     * @param vehicleJpa
     * @return
     */
    @Query("SELECT count(distinct vc.vehicleCompartmentPK.compartmentIdx) FROM VehicleCompartmentJpa vc WHERE vc.vehicle = :vehicle")
    int findTotalCompartmentsByVehicle(@Param("vehicle") VehicleJpa vehicleJpa);

    @Query("SELECT vc FROM VehicleCompartmentJpa vc WHERE vc.vehicle = :vehicle AND vc.vehicleCompartmentPK.compartmentIdx = :compartmentIdx")
    List<VehicleCompartmentJpa> findByVehicleAndCompartment(@Param("vehicle") VehicleJpa vehicleJpa,
                                                            @Param("compartmentIdx") int compartmentIdx);

}
