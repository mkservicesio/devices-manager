package gr.esdalab.atlas.devices.core.domain;

import gr.esdalab.atlas.devices.adapters.persitence.entities.NetworkLayerInterfaceJpa;
import gr.esdalab.atlas.devices.common.resources.in.HealthInput;
import gr.esdalab.atlas.devices.core.application.exceptions.AtlasException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * An interface representing a Network adapter of a device.
 */
public interface NetworkAdapter {

    /**
     * Return the communication technology.
     * @return
     */
    String getTechnology();

    /**
     * Returning the address of the adapter for communication.
     * @return
     */
    String getAddress();

    /**
     * Class Representing an IP Network Adapter
     */
    @RequiredArgsConstructor
    class IPNetworkAdapter implements NetworkAdapter{
        private static final String TECHNOLOGY = "IPv4";
        private final String ip;

        @Override
        public String getTechnology() {
            return TECHNOLOGY;
        }

        @Override
        public String getAddress() {
            return ip;
        }

    }

    /**
     * Class Representing an IP Network Adapter
     */
    @RequiredArgsConstructor
    class BLENetworkAdapter implements NetworkAdapter{
        private static final String TECHNOLOGY = "BLE";
        private final String mac;
        @Override
        public String getAddress() {
            return mac;
        }

        @Override
        public String getTechnology() {
            return TECHNOLOGY;
        }

    }

    /**
     * Class Representing an IP Network Adapter
     */
    @RequiredArgsConstructor
    class LoRaNetworkAdapter implements NetworkAdapter{
        private static final String TECHNOLOGY = "LORAWAN";
        private final String mac;
        @Override
        public String getAddress() {
            return mac;
        }
        @Override
        public String getTechnology() {
            return TECHNOLOGY;
        }

    }

    /**
     * Class Adapter Factory
     */
    class AdapterFactory{

        /**
         *
         * @return
         */
        public static NetworkAdapter of(@NonNull final NetworkLayerInterfaceJpa netIf){
            switch (netIf.getNetworkPK().getInterfaceId()){
                case IPv4:
                    return new IPNetworkAdapter(netIf.getUrn());
                case BLE:
                    return new BLENetworkAdapter(netIf.getUrn());
                case LORAWAN:
                    return new LoRaNetworkAdapter(netIf.getUrn());
                case ZIGBEE:
                case IEEE802154:
                case IPv6:
            }
            throw new AtlasException.MisConfigurationException(new DomainError.MisConfiguredDomainError("Not supported network interface ["+netIf.getNetworkPK().getInterfaceId()+"]."));
        }

        /**
         *
         * @return
         */
        public static NetworkAdapter from(@NonNull final HealthInput.NetworkInterfaceInput input){
            switch (input.getType()){
                case IPv4:
                    return new IPNetworkAdapter(String.join(",", input.getAddresses()));
                case BLE:
                    return new BLENetworkAdapter(String.join(",", input.getAddresses()));
                case LORAWAN:
                    return new LoRaNetworkAdapter(String.join(",", input.getAddresses()));
                case ZIGBEE:
                case IEEE802154:
                case IPv6:
            }
            throw new AtlasException.MisConfigurationException(new DomainError.MisConfiguredDomainError("Not supported network interface ["+input.getType()+"]."));
        }

    }

}
