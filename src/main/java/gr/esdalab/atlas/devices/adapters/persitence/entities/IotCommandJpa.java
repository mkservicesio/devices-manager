package gr.esdalab.atlas.devices.adapters.persitence.entities;

import gr.esdalab.atlas.devices.common.IotCommandStatus;
import gr.esdalab.atlas.devices.adapters.persitence.entities.listeners.AbstractTimestampEntity;
import lombok.*;

import javax.persistence.*;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="iot_commands")
@EqualsAndHashCode(callSuper=false)
@Inheritance(strategy = InheritanceType.JOINED)
public class IotCommandJpa extends AbstractTimestampEntity {
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    protected int commandId;

	@NonNull
	@Enumerated(EnumType.STRING)
	protected IotCommandStatus status;

}

