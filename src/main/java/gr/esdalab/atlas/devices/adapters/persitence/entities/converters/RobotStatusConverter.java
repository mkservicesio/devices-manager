package gr.esdalab.atlas.devices.adapters.persitence.entities.converters;

import gr.esdalab.atlas.devices.common.DeviceStatus;
import lombok.NonNull;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;

@Converter
public class RobotStatusConverter implements AttributeConverter<DeviceStatus.RobotStatus, Integer>{

	@Override
	public Integer convertToDatabaseColumn(@NonNull final DeviceStatus.RobotStatus attribute) {
		return attribute.getStatus();
	}

	@Override
	public DeviceStatus.RobotStatus convertToEntityAttribute(@NonNull final Integer dbData) {
		return Arrays.stream(DeviceStatus.RobotStatus.values())
				.filter(it -> it.getStatus()==dbData)
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("No value for RobotStatus found."));
	}
	
}
