package gr.esdalab.atlas.devices.adapters.persitence.entities.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import gr.esdalab.atlas.devices.common.utils.ApplicationUtils;
import gr.esdalab.atlas.devices.core.application.exceptions.AtlasException;
import gr.esdalab.atlas.devices.core.domain.Coordinates;
import gr.esdalab.atlas.devices.core.domain.DomainError;

import javax.persistence.AttributeConverter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static gr.esdalab.atlas.devices.core.domain.Coordinates.*;

/**
 * Database Coordinates Converter.
 */
public class CoordinatesConverter implements AttributeConverter<List<Coordinates>, String> {

    @Override
    public String convertToDatabaseColumn(final List<Coordinates> input) {
        try {
            return ApplicationUtils.getJsonMapper().writeValueAsString(input.stream().collect(Collectors.groupingBy(Coordinates::getType)));
        } catch (JsonProcessingException e) {
            throw new AtlasException.MisConfigurationException(new DomainError.MisConfiguredDomainError("Unable to generate coordinates JSON string."));
        }
    }

    @Override
    public List<Coordinates> convertToEntityAttribute(String dbData) {
        Map<String, List<Object>> data;
        try {
            data = ApplicationUtils.getJsonMapper().readValue(dbData, new TypeReference<Map<String, List<Object>>>() {});
            final List<Coordinates> coordinates = new ArrayList<>();
            for (Map.Entry<String,List<Object>> entry : data.entrySet()) {
                if( entry.getKey().equals(GPS_COORDINATES_IDENTIFIER) ){
                    coordinates.addAll(ApplicationUtils.getJsonMapper().convertValue(entry.getValue(), new TypeReference<List<Coordinates.GPSCoordinates>>() {}));
                }else if( entry.getKey().equals(CARTESIAN_COORDINATES_IDENTIFIER) ){
                    coordinates.addAll(ApplicationUtils.getJsonMapper().convertValue(entry.getValue(), new TypeReference<List<Coordinates.CartesianCoordinates>>() {}));
                }
            }
            return coordinates;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
