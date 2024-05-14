package gr.esdalab.atlas.devices.adapters.persitence.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import gr.esdalab.atlas.devices.adapters.persitence.entities.listeners.AbstractTimestampEntity;
import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="datatypes")
@EqualsAndHashCode(callSuper=false)
public class DatatypeJpa extends AbstractTimestampEntity {
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
	
	@Column(unique = true)
	@NotNull
	private String datatype;

	@Column(name = "default_threshold")
	@NotNull
	private String threshold;

	/**
	 * Set datatype threshold
	 * @param threshold
	 * @return
	 */
	public DatatypeJpa withThreshold(@NonNull final String threshold) {
		this.threshold = threshold;
		return this;
	}

}
