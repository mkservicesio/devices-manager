package gr.esdalab.atlas.devices.core.application.validators.impl;

import gr.esdalab.atlas.devices.common.errors.ErrorMessage;
import gr.esdalab.atlas.devices.core.application.validators.DomainValidator;
import gr.esdalab.atlas.devices.core.domain.DomainError;
import gr.esdalab.atlas.devices.core.domain.repositories.VehiclesRepository;
import gr.esdalab.atlas.devices.core.domain.vehicles.Vehicle;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class VehiclesValidator implements DomainValidator<Vehicle.VehicleInfo> {

    private final VehiclesRepository vehiclesRepo;

    @Override
    public List<DomainError> validate(@NonNull final Vehicle.VehicleInfo vehicleInfo) {
        return (List<DomainError>) vehiclesRepo.getVehicle(vehicleInfo.getLabel())
                .map(vehicle -> {
                    if( vehicle.getId() == vehicleInfo.getVehicleId() ){
                        return Collections.emptyList();
                    }
                    return List.of(new DomainError.RuleViolationDomainError(ErrorMessage.VEHICLE_ALREADY_EXIST.getMessage()));
                }).orElseGet(() -> Collections.emptyList());
    }
}
