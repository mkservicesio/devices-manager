package gr.esdalab.atlas.devices.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Utils for data transformation.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationUtils {

    public static final String THRESHOLD_SEPARATOR = ",";
    public static final int THRESHOLD_COMPARISON_PART = 0;
    public static final int THRESHOLD_VALUE_PART = 1;
    public static final String UUID_SEPARATOR = "-";
    public static final String UUID_REPLACER = "";

    /**
     * JSON Mapper
     */
    private static ObjectMapper jsonMapper;

    /**
     * Static initialization
     */
    static {
        jsonMapper = new ObjectMapper();
    }

    /**
     * Get Json Mapper
     * @return
     */
    public static ObjectMapper getJsonMapper(){
        return jsonMapper;
    }

    /**
     * Atlas Gateway identifier.
     * @return
     */
    public static String getAtlasIdentifier(){
        return "atlas.gw"+ UUID.randomUUID().toString().replace(UUID_SEPARATOR,UUID_REPLACER);
    }

    /**
     * Unique identifier based on UUID.
     * @return
     */
    public static String getUniqueIdentifier(){
        return UUID.randomUUID().toString().replace(UUID_SEPARATOR, UUID_REPLACER);
    }



}
