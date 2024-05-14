package gr.esdalab.atlas.devices.core.domain.sensors;

import gr.esdalab.atlas.devices.core.domain.Coordinates;
import gr.esdalab.atlas.devices.core.domain.Device;
import gr.esdalab.atlas.devices.core.domain.Health;
import gr.esdalab.atlas.devices.core.domain.common.Appendage;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

public interface SensorDevice{

	/**
	 * Abstract Class of a Sensing Device.
	 */
	@Getter
	abstract class SensingDevice extends Device {

		/**
	 		* The coordinates related to the Location the device exist.
	 	*/
		public SensingDevice(@NonNull final String urn,
							 @NonNull final DeviceType type,
							 @NonNull final DeviceMode mode,
							 @NonNull final Appendage appendage,
							 @NonNull final List<Coordinates> coordinates,
							 final Health.Device health) {
			super(urn, type, mode, appendage, coordinates, health);
		}

	}

}
