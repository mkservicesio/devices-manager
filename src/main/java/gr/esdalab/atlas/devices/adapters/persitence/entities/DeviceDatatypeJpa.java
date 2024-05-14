package gr.esdalab.atlas.devices.adapters.persitence.entities;

import gr.esdalab.atlas.devices.adapters.persitence.entities.embeddable.DeviceDatatypePrimary;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="device_datatypes")
public class DeviceDatatypeJpa {

    @EmbeddedId
    private DeviceDatatypePrimary DeviceDatatypePK;

    @ManyToOne(fetch= FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="deviceId", nullable = false, referencedColumnName = "id", insertable = false, updatable= false)
    private DeviceJpa device;

    @ManyToOne(fetch= FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="datatypeId", nullable = false, referencedColumnName = "id", insertable = false, updatable= false)
    private DatatypeJpa datatype;

    @Column(name = "threshold")
    @NotNull
    private String threshold;

    /**
     *
     * @param deviceJpa
     * @param datatypeJpa
     * @param threshold
     * @return
     */
    public static DeviceDatatypeJpa from(@NonNull final DeviceJpa deviceJpa,
                                         @NonNull final DatatypeJpa datatypeJpa,
                                         @NonNull final String threshold) {
        return new DeviceDatatypeJpa(new DeviceDatatypePrimary(deviceJpa.getId(), datatypeJpa.getId()), deviceJpa, datatypeJpa, threshold);
    }
}
