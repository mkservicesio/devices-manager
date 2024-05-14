package gr.esdalab.atlas.devices.adapters.persitence.entities.converters;

import gr.esdalab.atlas.devices.common.DeviceStatus;
import lombok.NonNull;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;

@Converter
public class IotGatewayStatusConverter implements AttributeConverter<DeviceStatus.GatewayStatus, Integer>{

	@Override
	public Integer convertToDatabaseColumn(@NonNull final DeviceStatus.GatewayStatus attribute) {
		return attribute.getStatus();
	}

	@Override
	public DeviceStatus.GatewayStatus convertToEntityAttribute(@NonNull final Integer dbData) {
		return Arrays.stream(DeviceStatus.GatewayStatus.values())
				.filter(it -> it.getStatus()==dbData)
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("No value for Gateway Status found."));
	}
	
}
