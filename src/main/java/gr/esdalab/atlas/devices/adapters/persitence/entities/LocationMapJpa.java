package gr.esdalab.atlas.devices.adapters.persitence.entities;

import gr.esdalab.atlas.devices.adapters.persitence.entities.listeners.AbstractTimestampEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="location_maps")
public class LocationMapJpa extends AbstractTimestampEntity {

    @Id
    private int id;

    @OneToOne
    @JoinColumn(name = "location_id")
    @MapsId
    private LocationJpa location;

    private int width;
    private int height;
    private double resolution;


}
