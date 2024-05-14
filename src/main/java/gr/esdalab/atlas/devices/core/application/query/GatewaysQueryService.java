package gr.esdalab.atlas.devices.core.application.query;

import gr.esdalab.atlas.devices.adapters.rest.resources.out.devices.GatewayOutput;

import java.util.List;
import java.util.Optional;

/**
 * Service for query gateways information.
 */
public interface GatewaysQueryService {

    /**
     * Return all available gateways
     * @return
     */
    List<GatewayOutput> getGateways();

    /**
     * Get Specific Gateway by its ID
     * @param gwId
     * @return
     */
    Optional<GatewayOutput> getGateway(int gwId);

    /**
     * Get Specific Gateway by its identity (String)
     * @param gwIdentity
     * @return
     */
    Optional<GatewayOutput> getGateway(String gwIdentity);

    /**
     * List gateway on the specified appendage.
     * @param appendageId
     * @return
     */
    List<GatewayOutput> getGatewaysByAppendageId(int appendageId);
}
