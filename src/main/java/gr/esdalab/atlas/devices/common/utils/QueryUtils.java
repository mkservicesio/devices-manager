package gr.esdalab.atlas.devices.common.utils;

import lombok.NonNull;

public class QueryUtils {

    /**
     *
     * @return
     */
    public static String[] getSort(@NonNull final String sort){
        return sort.split(",");
    }

}
