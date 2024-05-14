package gr.esdalab.atlas.devices.adapters.persitence.impl;

import gr.esdalab.atlas.devices.adapters.persitence.*;
import gr.esdalab.atlas.devices.adapters.persitence.entities.*;
import gr.esdalab.atlas.devices.adapters.persitence.entities.embeddable.NetworkLayerPrimary;
import gr.esdalab.atlas.devices.common.utils.ApplicationUtils;
import gr.esdalab.atlas.devices.core.domain.common.Appendage;
import gr.esdalab.atlas.devices.core.domain.gateways.Gateway;
import gr.esdalab.atlas.devices.core.domain.repositories.GatewaysRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GatewaysRepositoryImpl implements GatewaysRepository{

    private final GatewaysJpaRepository gatewaysJpaRepo;
    private final NetworkInterfacesJpaRepository networkIfsJpaRepo;
    private final NetworkLayersJpaRepository networkLayersJpaRepo;
    private final AppendagesJpaRepository appendagesJpaRepo;

    @Override
    public List<Gateway.CreatedGateway> list() {
        return gatewaysJpaRepo.findAll().stream()
                .map(it -> {
                    final List<NetworkLayerInterfaceJpa> commInterfaces = networkIfsJpaRepo.findByNetworkLayer(it.getNetwork());
                    return Gateway.CreatedGateway.from(it, commInterfaces, it.getAppendage());
                })
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Gateway.CreatedGateway> getGateway(final int gatewayId) {
        return gatewaysJpaRepo.findById(gatewayId)
                .map(it -> {
                    final List<NetworkLayerInterfaceJpa> commInterfaces = networkIfsJpaRepo.findByNetworkLayer(it.getNetwork());
                    return Gateway.CreatedGateway.from(it, commInterfaces, it.getAppendage());
                });
    }

    @Override
    public Optional<Gateway.CreatedGateway> getGateway(@NonNull final String gatewayId) {
        return gatewaysJpaRepo.findByIdentity(gatewayId)
                .map(it -> {
                    final List<NetworkLayerInterfaceJpa> commInterfaces = networkIfsJpaRepo.findByNetworkLayer(it.getNetwork());
                    return Gateway.CreatedGateway.from(it, commInterfaces, it.getAppendage());
                });
    }

    @Override
    @Transactional
    public void online(@NonNull final Gateway.OnlineGateway onlineGw) {
        gatewaysJpaRepo.findById(onlineGw.getId()).ifPresent(it -> {
            final IotGatewayJpa updatedGw = IotGatewayJpa.online(it, onlineGw.getVersion());
            networkIfsJpaRepo.deleteByNetworkLayerId(it.getNetwork().getId());
            final List<NetworkLayerInterfaceJpa> networkInterfaces = onlineGw.getAdapters()
                    .stream()
                    .map(interf -> new NetworkLayerInterfaceJpa(
                            new NetworkLayerPrimary(NetworkLayerPrimary.NetworkTechnologyType.valueOf(interf.getTechnology()), updatedGw.getNetwork().getId()),
                            updatedGw.getNetwork(),
                            interf.getAddress()
                    ))
                    .collect(Collectors.toList());
            networkIfsJpaRepo.saveAll(networkInterfaces);
            gatewaysJpaRepo.save(updatedGw);
        });
    }

    @Override
    @Transactional
    public Gateway.CreatedGateway createDefaultGateway(@NonNull final Appendage appendage) {
        final NetworkLayerJpa networkLayer = networkLayersJpaRepo.save(new NetworkLayerJpa());
        final IotGatewayJpa iotGatewayJpa = gatewaysJpaRepo.save(IotGatewayJpa.draft(ApplicationUtils.getAtlasIdentifier(), networkLayer, appendagesJpaRepo.getOne(appendage.getAppendageId()), "-"));
        return getGateway(iotGatewayJpa.getId()).get();
    }

    @Override
    public List<Gateway.CreatedGateway> listByAppendageId(final int appendageId) {
        return gatewaysJpaRepo.findByAppendageId(appendageId).stream()
                .map(it -> {
                    final List<NetworkLayerInterfaceJpa> commInterfaces = networkIfsJpaRepo.findByNetworkLayer(it.getNetwork());
                    return Gateway.CreatedGateway.from(it, commInterfaces, it.getAppendage());
                })
                .collect(Collectors.toList());
    }

}
