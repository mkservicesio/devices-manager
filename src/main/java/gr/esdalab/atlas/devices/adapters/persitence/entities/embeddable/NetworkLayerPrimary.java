package gr.esdalab.atlas.devices.adapters.persitence.entities.embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NetworkLayerPrimary implements Serializable {
    private static final long serialVersionUID = 4272458234723L;

    /**
     * Communication Interface
     */
    @Enumerated(EnumType.STRING)
    private NetworkTechnologyType interfaceId;

    /**
     * Network Layer
     */
    private int networkLayerId;

    /**
     * Network Technology Type //TODO This enum must not accessible outside the persistence adapter.
     */
    public enum NetworkTechnologyType {

        /**
         * IPv4 Address
         */
        IPv4,

        /**
         * BLE Address
         */
        BLE,

        /**
         * LoRaWAN Address
         */
        LORAWAN,

        /**
         * Zigbee Address
         */
        ZIGBEE,

        /**
         * 802.15.4 Address
         */
        IEEE802154,

        /**
         * IPv6
         */
        IPv6;


    }

}
