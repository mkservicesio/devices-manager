package gr.esdalab.atlas.devices.core.application.query.impl;

import gr.esdalab.atlas.devices.core.application.query.RobotsQueryService;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.devices.RobotOutput;
import gr.esdalab.atlas.devices.core.domain.events.MonitorEvent;
import gr.esdalab.atlas.devices.core.domain.repositories.RobotsRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of {@RobotsQueryService}
 */
@Service
@RequiredArgsConstructor
public class RobotsQueryServiceImpl implements RobotsQueryService {

    private final RobotsRepository robotsRepo;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public List<RobotOutput> getRobots() {
        return robotsRepo.list().stream().map(RobotOutput::from).collect(Collectors.toList());
    }

    @Override
    public Optional<RobotOutput> getRobot(final int robotId) {
        return robotsRepo.getRobot(robotId).map(RobotOutput::from);
    }

    @Override
    public Optional<RobotOutput> getRobot(@NonNull final String robotId) {
        return robotsRepo.getRobot(robotId)
                .map(it -> {
                    eventPublisher.publishEvent(new MonitorEvent.RobotMonitorEvent(this, it));
                    return RobotOutput.from(it);
                });
    }

    @Override
    public List<RobotOutput> getRobotsByAppendageId(final int appandageId) {
        return robotsRepo.list()
                .stream()
                .filter(it -> (it.getAppendage().getAppendageId() == appandageId))
                .map(RobotOutput::from)
                .collect(Collectors.toList());
    }
}
