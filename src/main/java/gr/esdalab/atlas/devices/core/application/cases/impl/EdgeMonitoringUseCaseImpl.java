package gr.esdalab.atlas.devices.core.application.cases.impl;

import gr.esdalab.atlas.devices.core.application.cases.EdgeMonitoringUseCase;
import gr.esdalab.atlas.devices.common.resources.in.HealthInput;
import gr.esdalab.atlas.devices.core.domain.gateways.Gateway;
import gr.esdalab.atlas.devices.core.domain.NetworkAdapter;
import gr.esdalab.atlas.devices.core.domain.repositories.DevicesRepository;
import gr.esdalab.atlas.devices.core.domain.repositories.GatewaysRepository;
import gr.esdalab.atlas.devices.core.domain.repositories.RobotsRepository;
import gr.esdalab.atlas.devices.core.domain.sensors.impl.DataSensorDevice;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EdgeMonitoringUseCaseImpl implements EdgeMonitoringUseCase {

    private final GatewaysRepository gatewaysRepo;
    private final DevicesRepository devicesRepo;
    private final RobotsRepository robotsRepo;
    private static final String ATLAS_GW_PREFIX = "atlas";
    private static final String GENERIC_DEVICE_PREFIX = "urn:esda:atlas";

    @Override
    public void healthCheck(@NonNull final String deviceUrn,
                            @NonNull final HealthInput healthInput) {
        if( deviceUrn.startsWith(ATLAS_GW_PREFIX) ){
            gatewayHealthCheck(deviceUrn, healthInput);
        }else if( deviceUrn.startsWith(GENERIC_DEVICE_PREFIX) ){
            deviceHealthCheck(deviceUrn, healthInput);
        }
    }

    @Override
    public void deviceHealthCheck(final int deviceId) {
        log.info("Device health check received for device with ID: {}", deviceId);
        devicesRepo.online(deviceId);
    }

    @Override
    public void robotHealthCheck(final int robotId) {
        log.info("Robot health check received for robot with ID: {}", robotId);
        robotsRepo.online(robotId);
    }

    /**
     *
     * @param gwIdentity
     * @param healthInput
     */
    private void gatewayHealthCheck(@NonNull final String gwIdentity,
                                    @NonNull final HealthInput healthInput){
        gatewaysRepo.getGateway(gwIdentity).ifPresentOrElse((gateway) -> {
                    final Gateway.OnlineGateway onlineGw = new Gateway.OnlineGateway(
                            gateway.getId(),
                            gateway.getUrn(),
                            healthInput.getVersion(),
                            healthInput.getInterfaces().stream().map(NetworkAdapter.AdapterFactory::from).collect(Collectors.toList()),
                            gateway.getAppendage().getAppendageId()
                    );
                    gatewaysRepo.online(onlineGw);
                },
                ()-> log.warn("Gateway with identity {}, not found. Skipping!", gwIdentity));
    }

    private void deviceHealthCheck(@NonNull final String deviceUrn,
                                    @NonNull final HealthInput healthInput){
        devicesRepo.getDeviceByUrn(deviceUrn).ifPresentOrElse(device -> {
            devicesRepo.online(DataSensorDevice.OnlineDataSensorDevice.from(
                        (DataSensorDevice.CreatedDataSensorDevice)device,
                        healthInput.getInterfaces().stream().map(NetworkAdapter.AdapterFactory::from).collect(Collectors.toList())
                    )
            );
        }, () -> {
            log.warn("Device with  URN {} not found. Skipping!!", deviceUrn);
        });
    }

}
