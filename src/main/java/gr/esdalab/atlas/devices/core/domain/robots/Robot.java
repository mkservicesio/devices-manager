package gr.esdalab.atlas.devices.core.domain.robots;

import gr.esdalab.atlas.devices.adapters.persitence.entities.RobotJpa;
import gr.esdalab.atlas.devices.core.domain.Device;
import gr.esdalab.atlas.devices.core.domain.Health;
import gr.esdalab.atlas.devices.core.domain.common.Appendage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

import java.util.Collections;
import java.util.Optional;

@Getter(value = AccessLevel.PUBLIC)
public abstract class Robot extends Device {

	/**
	 * Required Arguments Constructor
	 * @param urn
	 * @param type
	 * @param mode
	 */
	public Robot(@NonNull final String urn,
				 @NonNull final DeviceType type,
				 @NonNull final DeviceMode mode,
				 @NonNull final Appendage appendage,
				 final Health.Device health) {
		super(urn, type, mode, appendage, Collections.emptyList(), health);
	}

	@Getter
	public static class CreatedRobot extends Robot{

		private final int id;

		public CreatedRobot(final int id,
							@NonNull final String urn,
							@NonNull final DeviceType type,
							@NonNull final DeviceMode mode,
							@NonNull final Appendage appendage,
							final Health.Device health) {
			super(urn, type, mode, appendage, health);
			this.id = id;
		}

		/**
		 * Transform JPA object to domain object.
		 * @param robotJpa
		 * @return
		 */
		public static CreatedRobot from(@NonNull final RobotJpa robotJpa) {
			return new CreatedRobot(robotJpa.getId(),
					robotJpa.getIdentity(),
					robotJpa.getMode().getType(),
					robotJpa.getMode().getMode(),
					Appendage.from(robotJpa.getAppentages()),
					Optional.ofNullable(robotJpa.getOnlineAt()).map(it -> new Health.Device(it, robotJpa.getStatus())).orElse(null)
			);
		}

	}
}
