package gr.esdalab.atlas.devices.core.application.cases.impl;

import gr.esdalab.atlas.devices.adapters.rest.resources.in.LocationInput;
import gr.esdalab.atlas.devices.adapters.rest.resources.in.vehicles.VehicleSensorThresholdInput;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.LocationOutput;
import gr.esdalab.atlas.devices.common.errors.ErrorMessage;
import gr.esdalab.atlas.devices.config.VehiclesConfig;
import gr.esdalab.atlas.devices.core.application.cases.LocationsUseCase;
import gr.esdalab.atlas.devices.core.application.cases.VehiclesCreationUseCase;
import gr.esdalab.atlas.devices.core.application.exceptions.AtlasException;
import gr.esdalab.atlas.devices.adapters.rest.resources.in.vehicles.VehicleInput;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.vehicles.VehicleOutput;
import gr.esdalab.atlas.devices.core.application.factories.vehicles.VehicleFactory;
import gr.esdalab.atlas.devices.core.application.query.VehiclesQueryService;
import gr.esdalab.atlas.devices.core.application.validators.impl.VehiclesValidator;
import gr.esdalab.atlas.devices.core.domain.common.Appendage;
import gr.esdalab.atlas.devices.core.domain.common.Datatype;
import gr.esdalab.atlas.devices.core.domain.DomainError;
import gr.esdalab.atlas.devices.core.domain.events.AssociationEvent;
import gr.esdalab.atlas.devices.core.domain.repositories.CommonRepository;
import gr.esdalab.atlas.devices.core.domain.repositories.DevicesRepository;
import gr.esdalab.atlas.devices.core.domain.repositories.VehiclesRepository;
import gr.esdalab.atlas.devices.core.domain.sensors.impl.DataSensorDevice;
import gr.esdalab.atlas.devices.core.domain.vehicles.impl.OilVehicle;
import gr.esdalab.atlas.devices.core.domain.vehicles.Vehicle;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleCreationUseCaseImpl implements VehiclesCreationUseCase {

    private final VehiclesConfig vehiclesConfig;
    private final VehiclesRepository vehiclesRepo;
    private final DevicesRepository devicesRepo;
    private final LocationsUseCase locationsUseCase;
    private final CommonRepository commonRepo;
    private final VehicleFactory vehicleFactory;
    private final VehiclesQueryService vehiclesQueryServ;
    private final ApplicationEventPublisher eventPublisher;
    private final VehiclesValidator validator;

    @Override
    @Transactional
    public Optional<VehicleOutput> create(@NonNull final VehicleInput.VehicleCreationInput vehicle) {
        log.info("Generating common information for vehicle [Appendage, Gateway]...");
        if( vehicle.getCompartments() > vehiclesConfig.getCompartmentsLimit() ){
            throw new AtlasException.DomainViolationException(Collections.singletonList(new DomainError.RuleViolationDomainError(ErrorMessage.VEHICLES_COMPARTMENTS_LIMIT_REACHED.getMessage())));
        }

        final List<DomainError> errors = validator.validate(new Vehicle.VehicleInfo(-1, vehicle.getOwner(), vehicle.getLabel()));
        if( !errors.isEmpty() ){
            throw new AtlasException.DomainViolationException(errors);
        }

        final Appendage appendage = commonRepo.getCommonAppendage();
        final OilVehicle.CreatedOilVehicle createdOilVehicle = vehiclesRepo.addVehicle(appendage, vehicleFactory.create(appendage, vehicle));
        return vehiclesQueryServ.getVehicle(createdOilVehicle.getId());
    }

    @Override
    public void activate(final int vehicleId) {
        vehiclesRepo.process(new Vehicle.ActiveVehicle(vehicleId));
    }

    @Override
    public void deactivate(final int vehicleId) {
        vehiclesRepo.process(new Vehicle.InActiveVehicle(vehicleId));
    }

    @Override
    public void setThreshold(final int vehicleId,
                             @NonNull final VehicleSensorThresholdInput thresholdInput) {
        vehiclesRepo.getVehicle(vehicleId).ifPresentOrElse(vehicle -> {
            if( vehicle.getCompartments().isEmpty() || thresholdInput.getCompartment() >= vehicle.getCompartments().size() ){
                throw new AtlasException.ResourceNotExistException(new DomainError.NotFoundDomainError(ErrorMessage.VEHICLE_COMPARTMENT_NOT_EXIST.getMessage()));
            }

            final OilVehicle.Compartment compartment = vehicle.getCompartments().get(thresholdInput.getCompartment());
            compartment.getSensors().stream()
                    .filter(it -> it.getId() == thresholdInput.getDevice())
                    .findFirst()
                    .ifPresentOrElse(device -> {
                        device.getDatatypes().stream().filter(it -> it.getId() == thresholdInput.getDatatype())
                                .findFirst()
                                .ifPresentOrElse(
                                        datatype -> {
                                           final DataSensorDevice.CreatedDataSensorDevice forUpdateDevice = DataSensorDevice.CreatedDataSensorDevice
                                                   .withThresholdOnDatatype(
                                                           device,
                                                           Datatype.Threshold.from(thresholdInput.getThreshold()),
                                                           thresholdInput.getDatatype()
                                                   );
                                            devicesRepo.updateThresholds(forUpdateDevice);
                                        },
                                        () -> {
                                            throw new AtlasException.ResourceNotExistException(new DomainError.NotFoundDomainError(ErrorMessage.VEHICLE_COMPARTMENT_DEVICE_DATATYPE_NOT_EXIST.getMessage()));
                                        }
                                );
                    }, () -> {
                        throw new AtlasException.ResourceNotExistException(new DomainError.NotFoundDomainError(ErrorMessage.VEHICLE_COMPARTMENT_DEVICE_NOT_EXIST.getMessage()));
                    });

        }, () -> {
            throw new AtlasException.ResourceNotExistException(new DomainError.NotFoundDomainError(ErrorMessage.VEHICLE_NOT_EXIST.getMessage()));
        });
    }

    @Override
    @Transactional
    public void update(final int vehicleId,
                       @NonNull final VehicleInput.VehicleUpdateInput vehicleInput) {
        final List<DomainError> errors = validator.validate(new Vehicle.VehicleInfo(vehicleId, vehicleInput.getOwner(), vehicleInput.getLabel()));
        if( !errors.isEmpty() ){
            throw new AtlasException.DomainViolationException(errors);
        }
        vehiclesRepo.update(new Vehicle.VehicleInfo(vehicleId, vehicleInput.getOwner(), vehicleInput.getLabel()));
    }

    @Override
    @Transactional
    public void addPointOfInterest(final int vehicleId,
                                   @NonNull final LocationInput.DraftLocationInput locationInput) {
        vehiclesRepo.getVehicle(vehicleId)
                .ifPresentOrElse(vehicle -> {
                    final Optional<LocationOutput> locationOutput = locationsUseCase.create(locationInput.withVehicleAssociation(vehicleId));
                    locationOutput.ifPresent(it -> log.info("New location {} [{}] associated with vehicle{} created successfully!", it.getIdentity(), it.getLocationId(), vehicleId));
                }, () -> {
                    throw new AtlasException.ResourceNotExistException(new DomainError.NotFoundDomainError(ErrorMessage.VEHICLE_NOT_EXIST.getMessage()));
                });
    }

    @Override
    @Transactional
    public void removePointOfInterest(final int vehicleId,
                                      final int locationId) {
        eventPublisher.publishEvent(new AssociationEvent.LocationDiAssociationEvent<>(this, locationId, new Vehicle.ActiveVehicle(vehicleId)));
    }

}
