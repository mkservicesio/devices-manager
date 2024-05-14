package gr.esdalab.atlas.devices.adapters.persitence.entities;

import javax.persistence.*;

import gr.esdalab.atlas.devices.adapters.persitence.entities.listeners.AbstractTimestampEntity;
import lombok.*;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="network_layer")
@EqualsAndHashCode(callSuper=false)
public class NetworkLayerJpa extends AbstractTimestampEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

}
