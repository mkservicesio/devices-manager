package gr.esdalab.atlas.devices.adapters.persitence.entities;

import javax.persistence.*;

import gr.esdalab.atlas.devices.adapters.persitence.entities.converters.IotGatewayStatusConverter;
import gr.esdalab.atlas.devices.adapters.persitence.entities.listeners.AbstractTimestampEntity;
import gr.esdalab.atlas.devices.common.DeviceStatus;
import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="gateways")
@EqualsAndHashCode(callSuper=false)
public class IotGatewayJpa extends AbstractTimestampEntity {
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

	@NonNull
	@Column(unique = true)
	private String identity;
	
	@Convert(converter = IotGatewayStatusConverter.class)
	@Builder.Default
	private DeviceStatus.GatewayStatus status = DeviceStatus.GatewayStatus.INSTALLED;
	
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "network_layerid")
	private NetworkLayerJpa network;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "appentage_id", nullable = false, referencedColumnName = "id", updatable= false)
	private AppendageJpa appendage;

	@NonNull
	private String version;

	/**
	 *
	 * @return
	 */
	public static IotGatewayJpa draft(@NonNull final String identity,
									  @NonNull final NetworkLayerJpa network,
									  @NonNull final AppendageJpa appentage,
									  @NonNull final String version){
		return IotGatewayJpa.builder()
				.identity(identity)
				.status(DeviceStatus.GatewayStatus.INSTALLED)
				.network(network)
				.appendage(appentage)
				.version(version)
				.build();
	}

	/**
	 *
	 * @return
	 */
	public static IotGatewayJpa online(@NonNull final IotGatewayJpa iotGatewayJpa,
									   @NonNull final String version){
		return new IotGatewayJpa(iotGatewayJpa.getId(),
				iotGatewayJpa.getIdentity(),
				DeviceStatus.GatewayStatus.ONLINE,
				iotGatewayJpa.getNetwork(),
				iotGatewayJpa.getAppendage(),
				version);
	}

}
