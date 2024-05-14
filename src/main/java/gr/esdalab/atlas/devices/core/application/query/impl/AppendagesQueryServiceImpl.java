package gr.esdalab.atlas.devices.core.application.query.impl;

import gr.esdalab.atlas.devices.adapters.persitence.AppendagesJpaRepository;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.AppendageOutput;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.devices.DeviceOutput;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.devices.GatewayOutput;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.LocationOutput;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.devices.RobotOutput;
import gr.esdalab.atlas.devices.core.application.query.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@AppendagesQueryService}
 */
@Service
@RequiredArgsConstructor
public class AppendagesQueryServiceImpl implements AppendagesQueryService {

    private final AppendagesJpaRepository appendagesJpaRepo;
    private final LocationsQueryService locationsQueryServ;
    private final GatewaysQueryService gatewaysQueryServ;
    private final RobotsQueryService robotsQueryServ;
    private final DevicesQueryService devicesQueryServ;

    @Override
    public Optional<AppendageOutput> getAppendage(final int appendageId) {
        return appendagesJpaRepo.findById(appendageId)
                .map(it -> {
                    Optional<LocationOutput> location = locationsQueryServ.getLocationByAppendage(it.getId());
                    final List<GatewayOutput> gateways = gatewaysQueryServ.getGatewaysByAppendageId(it.getId());
                    final List<RobotOutput> robots = robotsQueryServ.getRobotsByAppendageId(it.getId());
                    final List<DeviceOutput> devices = devicesQueryServ.getDevicesByAppendageId(it.getId());
                    return AppendageOutput.from(it, location.orElse(null), gateways, robots, devices);
                });
    }
}
