package gr.esdalab.atlas.devices.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NetworkingUtils {

    /**
     * Split and return the address of a device.
     * @param addressStr
     * @return
     */
    public static List<String> getAddresses(@NonNull final String addressStr){
        return Arrays.asList(addressStr.split(","));
    }

}
