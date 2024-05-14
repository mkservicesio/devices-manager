package gr.esdalab.atlas.devices.adapters.persitence.entities;

import gr.esdalab.atlas.devices.adapters.persitence.entities.listeners.AbstractTimestampEntity;
import gr.esdalab.atlas.devices.core.domain.vehicles.Vehicle;
import lombok.*;

import javax.persistence.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="vehicles")
@EqualsAndHashCode(callSuper=false)
public class VehicleJpa extends AbstractTimestampEntity {
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

	@NonNull
	@Column(name = "label")
	private String label;

	@Column(name = "owner_id")
	private int owner;
	
	@Enumerated(EnumType.STRING)
	private VehicleStatus status;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "appentage_id", nullable = false, referencedColumnName = "id", updatable= false)
	private AppendageJpa appentage;

	@Column(name = "compartments")
	private int compartments;

	/**
	 *	Create a draft vehicle.
	 * @return
	 */
	public static VehicleJpa draft(final int owner,
								   @NonNull final String label,
								   @NonNull final AppendageJpa appentage,
								   final int compartments){
		return VehicleJpa.builder()
				.label(label)
				.owner(owner)
				.status(VehicleStatus.INACTIVE)
				.appentage(appentage)
				.compartments(compartments)
				.build();
	}

	/**
	 *
	 * @return
	 */
	public VehicleJpa active() {
		this.status = VehicleStatus.ACTIVE;
		return this;
	}

	/**
	 *
	 * @return
	 */
	public VehicleJpa inactive() {
		this.status = VehicleStatus.INACTIVE;
		return this;
	}

    public VehicleJpa info(@NonNull final Vehicle.VehicleInfo vehicle) {
		this.owner = vehicle.getOwnerId();
		this.label = vehicle.getLabel();
		return this;
    }

    /**
	 * Vehicle Status
	 */
	public enum VehicleStatus{
		INACTIVE,
		ACTIVE;
	}

}
