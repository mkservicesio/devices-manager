package gr.esdalab.atlas.devices.adapters.persitence.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import gr.esdalab.atlas.devices.adapters.persitence.entities.converters.CoordinatesConverter;
import gr.esdalab.atlas.devices.adapters.persitence.entities.listeners.AbstractTimestampEntity;
import gr.esdalab.atlas.devices.adapters.persitence.entities.converters.DevicesStatusConverter;
import gr.esdalab.atlas.devices.adapters.persitence.entities.embeddable.DeviceModeIdentity;
import gr.esdalab.atlas.devices.common.DeviceStatus;
import gr.esdalab.atlas.devices.core.domain.Coordinates;
import gr.esdalab.atlas.devices.core.domain.sensors.impl.DataSensorDevice;
import lombok.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="devices")
@EqualsAndHashCode(callSuper=false)
public class DeviceJpa extends AbstractTimestampEntity {
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
	
	@Column(unique = true)
	@NotNull
	private String identity;
	
	@Convert(converter = DevicesStatusConverter.class)
	@Builder.Default
	private DeviceStatus.GenericDeviceStatus status = DeviceStatus.GenericDeviceStatus.NOT_CONNECTED;
	
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "network_layerid")
	private NetworkLayerJpa network;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appentage_id")
	private AppendageJpa appendage;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride( name = "type", column = @Column(name = "device_type")),
			@AttributeOverride( name = "mode", column = @Column(name = "device_mode"))
	})
	private DeviceModeIdentity mode;

	@Convert(converter = CoordinatesConverter.class)
	private List<Coordinates> coordinates;

	@Column(name = "online_at")
	private Date onlineAt;

	/**
	 * Draft implementation.
	 * @return
	 */
	public static DeviceJpa draft(@NonNull final AppendageJpa appendageJpa,
								  @NonNull final DataSensorDevice sensorDevice) {
		return DeviceJpa.builder()
				.identity(sensorDevice.getUrn())
				.status(DeviceStatus.GenericDeviceStatus.NOT_CONNECTED)
				.appendage(appendageJpa)
				.mode(new DeviceModeIdentity(sensorDevice.getType(), sensorDevice.getMode()))
				.coordinates(Collections.emptyList())
				.build();
	}

	/**
	 *
	 * @return
	 */
	public DeviceJpa online() {
		this.onlineAt = new Date();
		this.status = DeviceStatus.GenericDeviceStatus.CONNECTED;
		return this;
	}

}
