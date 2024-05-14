package gr.esdalab.atlas.devices.adapters.persitence.entities;

import gr.esdalab.atlas.devices.adapters.persitence.entities.embeddable.VehicleCompartmentPrimary;
import gr.esdalab.atlas.devices.core.domain.Device;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="vehicle_compartments")
public class VehicleCompartmentJpa {

    @EmbeddedId
    private VehicleCompartmentPrimary vehicleCompartmentPK;

    @ManyToOne(fetch= FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="vehicleId", nullable = false, referencedColumnName = "id", insertable = false, updatable= false, foreignKey = @ForeignKey(name ="VehicleCompartments_vehicleId_FK"))
    private VehicleJpa vehicle;

    @ManyToOne(fetch= FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="deviceId", nullable = false, referencedColumnName = "id", insertable = false, updatable= false, foreignKey = @ForeignKey(name ="VehicleCompartments_deviceId_FK"))
    private DeviceJpa device;

    /**
     *
     * @param vehicleJpa
     * @param compartmentId
     * @param deviceJpa
     * @return
     */
    public static VehicleCompartmentJpa from(@NonNull final VehicleJpa vehicleJpa, final int compartmentId, final DeviceJpa deviceJpa) {
        return new VehicleCompartmentJpa(new VehicleCompartmentPrimary(vehicleJpa.getId(), compartmentId, deviceJpa.getId()), vehicleJpa, deviceJpa);
    }
}
