package gr.esdalab.atlas.devices.adapters.persitence.entities.converters;

import gr.esdalab.atlas.devices.common.DeviceStatus;
import lombok.NonNull;

import javax.persistence.AttributeConverter;
import java.util.Arrays;

public class DevicesStatusConverter implements AttributeConverter<DeviceStatus.GenericDeviceStatus, Integer>{

	@Override
	public Integer convertToDatabaseColumn(@NonNull final DeviceStatus.GenericDeviceStatus attribute) {
		return attribute.getStatus();
    }

	@Override
	public DeviceStatus.GenericDeviceStatus convertToEntityAttribute(@NonNull final Integer dbData) {
        return Arrays.stream(DeviceStatus.GenericDeviceStatus.values())
                .filter(it -> it.getStatus()==dbData)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No value for Device Status found."));
    }

}
