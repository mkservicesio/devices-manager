package gr.esdalab.atlas.devices.adapters.persitence.entities;

import gr.esdalab.atlas.devices.adapters.persitence.entities.converters.IotCommandPartsConverter;
import gr.esdalab.atlas.devices.common.IotCommandStatus;
import gr.esdalab.atlas.devices.core.domain.iot.commands.IotCommand;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.List;


@Getter
@NoArgsConstructor
@Entity
@Table(name="vehicle_commands")
@EqualsAndHashCode(callSuper=false)
public class VehicleCommandJpa extends IotCommandJpa {

	@Column(name = "command_type")
	private String type;

	@ManyToOne
	@JoinColumn(name="vehicleId")
	private VehicleJpa vehicleJpa;

	@Convert(converter = IotCommandPartsConverter.class)
	private List<IotCommand.Part<String>> parts;


	/**
	 * Required Arguments constructor.
	 */
	public VehicleCommandJpa(@NonNull final IotCommandStatus status,
							 @NonNull final String type,
							 @NonNull final VehicleJpa vehicleJpa,
							 @NonNull final List<IotCommand.Part<String>> parts){
		super();
		super.status = status;
		this.type = type;
		this.vehicleJpa = vehicleJpa;
		this.parts = parts;
	}

	/**
	 *	Switch Command to state.
	 * @return
	 */
	public VehicleCommandJpa onState(@NonNull final IotCommandStatus status){
		this.status = status;
		return this;
	}

}

