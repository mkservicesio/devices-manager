package gr.esdalab.atlas.devices.adapters.persitence.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import gr.esdalab.atlas.devices.adapters.persitence.entities.listeners.AbstractTimestampEntity;
import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="appendages")
@EqualsAndHashCode(callSuper=false)
public class AppendageJpa extends AbstractTimestampEntity {
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;	
}
