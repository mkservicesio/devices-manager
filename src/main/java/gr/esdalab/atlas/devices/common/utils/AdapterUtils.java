package gr.esdalab.atlas.devices.common.utils;

import lombok.NonNull;
import org.apache.commons.lang3.RandomStringUtils;

/**
 *  General Utilities class for adapters.
 */
public class AdapterUtils {

    private static final int DEFAULT_RANDOM_CHARACTERS_LETTER_COUNT = 20;
    private static final boolean RANDOM_CHARACTERS_INCLUDE_LETTERS = true;
    private static final boolean RANDOM_CHARACTERS_INCLUDE_NUMBERS = true;

    /**
     * Utilities related to MQTT Adapter.
     */
    public static class MQTTAdapterUtils{

        private static final String MQTT_CLIENTID_SEPARATOR = "@";

        /**
         *  Generate clientId for a MQTT Connection
         * @return
         */
        public static String generateClientId(@NonNull final String prefix){
            return prefix +
                    MQTT_CLIENTID_SEPARATOR +
                    RandomStringUtils.random(DEFAULT_RANDOM_CHARACTERS_LETTER_COUNT, RANDOM_CHARACTERS_INCLUDE_LETTERS, RANDOM_CHARACTERS_INCLUDE_NUMBERS);
        }

    }

}
