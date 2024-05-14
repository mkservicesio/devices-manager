package gr.esdalab.atlas.devices.core.domain.gateways;

import gr.esdalab.atlas.devices.adapters.persitence.entities.AppendageJpa;
import gr.esdalab.atlas.devices.adapters.persitence.entities.IotGatewayJpa;
import gr.esdalab.atlas.devices.adapters.persitence.entities.NetworkLayerInterfaceJpa;
import gr.esdalab.atlas.devices.common.DeviceStatus;
import gr.esdalab.atlas.devices.core.domain.Device;
import gr.esdalab.atlas.devices.core.domain.Health;
import gr.esdalab.atlas.devices.core.domain.NetworkAdapter;
import gr.esdalab.atlas.devices.core.domain.common.Appendage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter(value = AccessLevel.PUBLIC)
public abstract class Gateway extends Device {

	/**
	 * Required Arguments Constructor
	 * @param urn
	 */
	protected Gateway(@NonNull final String urn,
					  @NonNull final Appendage appendage,
					  final Health.Device health) {
		super(urn, DeviceType.REAL, DeviceMode.PRODUCTION, appendage , Collections.emptyList(), health);
	}

	/**
	 *
	 */
	@Getter
	public static class CreatedGateway extends Gateway {

		private final int id;
		private final String version;
		private final List<NetworkAdapter> adapters;

		public CreatedGateway(final int id,
							  @NonNull final String urn,
							  @NonNull final String version,
							  @NonNull final List<NetworkAdapter> adapters,
							  @NonNull final Appendage appendage,
							  final Health.Device health) {
			super(urn, appendage,health);
			this.id = id;
			this.version = version;
			this.adapters = adapters;
		}

		/**
		 * Transform JPA object to domain object.
		 * @param gatewayJpa
		 * @return
		 */
		public static CreatedGateway from(@NonNull final IotGatewayJpa gatewayJpa,
										  @NonNull final List<NetworkLayerInterfaceJpa> interfaces,
										  @NonNull final AppendageJpa appendageJpa) {
			return new CreatedGateway(gatewayJpa.getId(),
					gatewayJpa.getIdentity(),
					gatewayJpa.getVersion(),
					interfaces.stream().map(NetworkAdapter.AdapterFactory::of).collect(Collectors.toList()),
					Appendage.from(appendageJpa),
					Optional.ofNullable(gatewayJpa.getUpdatedAt())
							.map(it -> new Health.Device(it, gatewayJpa.getStatus()))
							.orElse(null)
			);
		}

	}

	/**
	 * Re-presenting an online gateway.
	 */
	public static class OnlineGateway extends CreatedGateway{

		public OnlineGateway(final int id,
							 @NonNull final String urn,
							 @NonNull final String version,
							 @NonNull final List<NetworkAdapter> adapters,
							 final int appendageId) {
			super(id, urn, version, adapters, new Appendage(appendageId), new Health.Device(new Date(), DeviceStatus.GatewayStatus.ONLINE));
		}
	}
}
