package gr.esdalab.atlas.devices.adapters.rest.resources.out.networking;

import gr.esdalab.atlas.devices.common.networking.AddressType;
import gr.esdalab.atlas.devices.common.utils.NetworkingUtils;
import gr.esdalab.atlas.devices.core.domain.NetworkAdapter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class NetworkAdapterOutput {

    /**
     * Communication Technology
     */
    private final String technology;

    /**
     * The address per-technology.
     */
    private final List<NetworkAddressType> adapters;

    /**
     * Mapper
     * @param networkAdapter
     * @return
     */
    public static NetworkAdapterOutput from(@NonNull final NetworkAdapter networkAdapter) {
        return new NetworkAdapterOutput(
                networkAdapter.getTechnology(),
                NetworkAddressType.from(networkAdapter.getAddress())
        );
    }


    /**
     * Re-Presents a network address type.
     */
    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NetworkAddressType {

        /**
         * The type of the address
         */
        private final AddressType type;

        /**
         * The address of the
         */
        private final List<String> addresses;

        /**
         * Mapper class.
         * List for future use.
         * @param address
         * @return
         */
        public static List<NetworkAddressType> from(@NonNull final String address) {
            return Collections.singletonList(new NetworkAddressType(
                        address.isEmpty() ? AddressType.NO_ADDRESS : (address.contains(".") ? AddressType.IP : AddressType.MAC),
                        NetworkingUtils.getAddresses(address)
                    )
            );
        }
    }

}
