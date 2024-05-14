package gr.esdalab.atlas.devices.adapters.persitence.entities;

import gr.esdalab.atlas.devices.adapters.persitence.entities.embeddable.VehicleLocationPrimary;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="vehicle_points_of_interests")
public class VehiclePOIJpa {

    @EmbeddedId
    private VehicleLocationPrimary vehicleLocationPK;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="vehicleId", nullable = false, referencedColumnName = "id", insertable = false, updatable= false, foreignKey = @ForeignKey(name ="vehicle_points_of_interests_vehicleId_FK"))
    private VehicleJpa vehicle;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="locationId", nullable = false, referencedColumnName = "id", insertable = false, updatable= false, foreignKey = @ForeignKey(name ="vehicle_points_of_interests_locationId_FK"))
    private LocationJpa location;

    /**
     *
     * @param vehicleJpa
     * @param locationJpa
     * @return
     */
    public static VehiclePOIJpa from(@NonNull final VehicleJpa vehicleJpa,
                                     @NonNull final LocationJpa locationJpa) {
        return new VehiclePOIJpa(new VehicleLocationPrimary(vehicleJpa.getId(), locationJpa.getId()), vehicleJpa, locationJpa);
    }
}
