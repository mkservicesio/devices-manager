package gr.esdalab.atlas.devices.core.application.query.impl;

import gr.esdalab.atlas.devices.core.application.query.GatewaysQueryService;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.devices.GatewayOutput;
import gr.esdalab.atlas.devices.core.domain.repositories.GatewaysRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of {@GatewaysQueryService}
 */
@Service
@RequiredArgsConstructor
public class GatewaysQueryServiceImpl implements GatewaysQueryService {

    private final GatewaysRepository gatewaysRepo;

    @Override
    public List<GatewayOutput> getGateways() {
        return gatewaysRepo.list().stream().map(GatewayOutput::from).collect(Collectors.toList());
    }

    @Override
    public Optional<GatewayOutput> getGateway(final int gatewayId) {
        return gatewaysRepo.getGateway(gatewayId).map(GatewayOutput::from);
    }

    @Override
    public Optional<GatewayOutput> getGateway(@NonNull final String gwIdentity) {
        return gatewaysRepo.getGateway(gwIdentity).map(GatewayOutput::from);
    }

    @Override
    public List<GatewayOutput> getGatewaysByAppendageId(final int appendageId) {
        return gatewaysRepo.listByAppendageId(appendageId).stream().map(GatewayOutput::from).collect(Collectors.toList());
    }
}
