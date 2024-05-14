package gr.esdalab.atlas.devices.adapters.persitence.impl;

import gr.esdalab.atlas.devices.adapters.persitence.RobotsJpaRepository;
import gr.esdalab.atlas.devices.common.errors.ErrorMessage;
import gr.esdalab.atlas.devices.core.application.exceptions.AtlasException;
import gr.esdalab.atlas.devices.core.domain.DomainError;
import gr.esdalab.atlas.devices.core.domain.robots.Robot;
import gr.esdalab.atlas.devices.core.domain.repositories.RobotsRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RobotsRepositoryImpl implements RobotsRepository {

    private final RobotsJpaRepository robotsJpaRepo;

    @Override
    public List<Robot.CreatedRobot> list() {
        return robotsJpaRepo.findAll().stream().map(Robot.CreatedRobot::from).collect(Collectors.toList());
    }

    @Override
    public Optional<Robot.CreatedRobot> getRobot(final int robotId) {
        return robotsJpaRepo.findById(robotId).map(Robot.CreatedRobot::from);
    }

    @Override
    public Optional<Robot.CreatedRobot> getRobot(@NonNull final String robotId) {
        return robotsJpaRepo.findByIdentity(robotId).map(Robot.CreatedRobot::from);
    }

    @Override
    public void online(final int robotId) {
        robotsJpaRepo.findById(robotId)
                .ifPresentOrElse(robot ->{
                    robotsJpaRepo.save(robot.online());
                }, () -> {
                    throw new AtlasException.ResourceNotExistException(new DomainError.NotFoundDomainError(ErrorMessage.ROBOT_NOT_EXIST.getMessage()));
                });
    }

}
