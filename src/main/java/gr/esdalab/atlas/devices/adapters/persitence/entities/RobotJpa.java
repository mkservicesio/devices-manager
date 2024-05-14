package gr.esdalab.atlas.devices.adapters.persitence.entities;

import javax.persistence.*;

import gr.esdalab.atlas.devices.adapters.persitence.entities.listeners.AbstractTimestampEntity;
import gr.esdalab.atlas.devices.adapters.persitence.entities.embeddable.DeviceModeIdentity;
import gr.esdalab.atlas.devices.common.DeviceStatus;
import lombok.*;
import gr.esdalab.atlas.devices.adapters.persitence.entities.converters.RobotStatusConverter;

import java.util.Date;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="robots")
@EqualsAndHashCode(callSuper=false)
public class RobotJpa extends AbstractTimestampEntity {
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
	
	@NonNull
	@Column(unique = true)
    private String identity;

	@Convert(converter = RobotStatusConverter.class)
	@NonNull
	@Builder.Default
	private DeviceStatus.RobotStatus status = DeviceStatus.RobotStatus.INSTALLED;
	
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "network_layerid")
	private NetworkLayerJpa network;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appentage_id")
	private AppendageJpa appentages;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride( name = "type", column = @Column(name = "robot_type")),
			@AttributeOverride( name = "mode", column = @Column(name = "robot_mode"))
	})
	private DeviceModeIdentity mode;

	@Column(name = "online_at")
	private Date onlineAt;

	/**
	 *
	 * @return
	 */
	public RobotJpa online() {
		this.onlineAt = new Date();
		return this;
	}

}

