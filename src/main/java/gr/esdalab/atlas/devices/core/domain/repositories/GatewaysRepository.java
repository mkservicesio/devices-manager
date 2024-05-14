package gr.esdalab.atlas.devices.core.domain.repositories;

import gr.esdalab.atlas.devices.core.domain.common.Appendage;
import gr.esdalab.atlas.devices.core.domain.gateways.Gateway;

import java.util.List;
import java.util.Optional;

/**
 * Re-presenting a repository for gateways.
 */
public interface GatewaysRepository {

    /**
     * List Gateways
     * @return
     */
    List<Gateway.CreatedGateway> list();

    /**
     * Get Gateway
     * @return
     */
    Optional<Gateway.CreatedGateway> getGateway(int gatewayId);

    /**
     * Get Gateway
     * @return
     */
    Optional<Gateway.CreatedGateway> getGateway(String gatewayId);

    /**
     * Set online a edge gateway
     * @param onlineGw
     */
    void online(Gateway.OnlineGateway onlineGw);

    /**
     * Create a new gateway with default settings.
     * @return
     */
    Gateway.CreatedGateway createDefaultGateway(Appendage appendage);

    /**
     *
     * @param appendageId
     * @return
     */
    List<Gateway.CreatedGateway> listByAppendageId(int appendageId);
}
