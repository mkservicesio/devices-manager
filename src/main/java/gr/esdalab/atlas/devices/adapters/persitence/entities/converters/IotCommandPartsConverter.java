package gr.esdalab.atlas.devices.adapters.persitence.entities.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import gr.esdalab.atlas.devices.common.utils.ApplicationUtils;
import gr.esdalab.atlas.devices.core.application.exceptions.AtlasException;
import gr.esdalab.atlas.devices.core.domain.Coordinates;
import gr.esdalab.atlas.devices.core.domain.DomainError;
import gr.esdalab.atlas.devices.core.domain.iot.commands.IotCommand;

import javax.persistence.AttributeConverter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static gr.esdalab.atlas.devices.core.domain.Coordinates.CARTESIAN_COORDINATES_IDENTIFIER;
import static gr.esdalab.atlas.devices.core.domain.Coordinates.GPS_COORDINATES_IDENTIFIER;

/**
 * Database Coordinates Converter.
 */
public class IotCommandPartsConverter implements AttributeConverter<List<IotCommand.Part<String>>, String> {

    @Override
    public String convertToDatabaseColumn(final List<IotCommand.Part<String>> input) {
        try {
            return ApplicationUtils.getJsonMapper().writeValueAsString(input);
        } catch (JsonProcessingException e) {
            throw new AtlasException.MisConfigurationException(new DomainError.MisConfiguredDomainError("Unable to generate commands parts JSON string."));
        }
    }

    @Override
    public List<IotCommand.Part<String>> convertToEntityAttribute(String dbData) {
        try {
            return ApplicationUtils.getJsonMapper().readValue(dbData, new TypeReference<List<IotCommand.Part<String>>>(){});
        } catch (JsonProcessingException e) {
            throw new AtlasException.MisConfigurationException(new DomainError.MisConfiguredDomainError("Unable to read commands parts JSON string."));
        }
    }
}
