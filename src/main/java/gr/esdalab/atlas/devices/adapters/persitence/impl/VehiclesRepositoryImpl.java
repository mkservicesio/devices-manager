package gr.esdalab.atlas.devices.adapters.persitence.impl;

import gr.esdalab.atlas.devices.adapters.persitence.*;
import gr.esdalab.atlas.devices.adapters.persitence.entities.*;
import gr.esdalab.atlas.devices.adapters.persitence.specs.VehicleSpecification;
import gr.esdalab.atlas.devices.common.errors.ErrorMessage;
import gr.esdalab.atlas.devices.core.application.exceptions.AtlasException;
import gr.esdalab.atlas.devices.core.application.query.VehiclesQueryService;
import gr.esdalab.atlas.devices.core.domain.common.Appendage;
import gr.esdalab.atlas.devices.core.domain.DomainError;
import gr.esdalab.atlas.devices.core.domain.common.PageableOf;
import gr.esdalab.atlas.devices.core.domain.locations.impl.OutdoorLocation;
import gr.esdalab.atlas.devices.core.domain.repositories.VehiclesRepository;
import gr.esdalab.atlas.devices.core.domain.sensors.impl.DataSensorDevice;
import gr.esdalab.atlas.devices.core.domain.vehicles.impl.OilVehicle;
import gr.esdalab.atlas.devices.core.domain.vehicles.Vehicle;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehiclesRepositoryImpl implements VehiclesRepository {

    private static final int COMPARTMENT_START_INDEX = 0;

    private final VehiclesJpaRepository vehiclesJpaRepo;
    private final AppendagesJpaRepository appendagesJpaRepo;
    private final VehicleCompartmentsJpaRepository vehicleCompartmentsJpaRepo;
    private final DevicesJpaRepository devicesJpaRepo;
    private final DevicesRepositoryImpl devicesRepo;
    private final LocationsJpaRepository locationsJpaRepo;
    private final VehiclePointOfInterestsJpaRepository vehiclePoisJpaRepo;

    @Override
    @Transactional
    public OilVehicle.CreatedOilVehicle addVehicle(@NonNull final Appendage appendage,
                                                   @NonNull final OilVehicle vehicle) {
        final AppendageJpa appendageJpa = appendagesJpaRepo.getOne(appendage.getAppendageId());
        final VehicleJpa vehicleJpa = vehiclesJpaRepo.save(VehicleJpa.draft(vehicle.getOwner().getOwnerId(), vehicle.getLabel(), appendageJpa, vehicle.getCompartments().size()));
        vehicle.getCompartments()
                .forEach(compartment -> vehicleCompartmentsJpaRepo.saveAll(compartment.getSensors().stream().map(device -> {
                    final DeviceJpa deviceJpa = devicesJpaRepo.findById(device.getId())
                            .orElseThrow(() -> {
                                throw new AtlasException.MisConfigurationException(new DomainError.MisConfiguredDomainError("Device ["+device.getId()+"], must exist."));
                            });
                    return VehicleCompartmentJpa.from(vehicleJpa, compartment.getIdx(), deviceJpa);
                }).collect(Collectors.toList())));
        return OilVehicle.CreatedOilVehicle.from(vehicleJpa, Collections.emptyList(), vehicle.getCompartments());
    }

    @Override
    public void update(@NonNull final Vehicle.VehicleInfo vehicle) {
        vehiclesJpaRepo.findById(vehicle.getVehicleId()).ifPresentOrElse(vehicleJpa -> {
            vehiclesJpaRepo.save(vehicleJpa.info(vehicle));
        }, () -> {
            throw new AtlasException.ResourceNotExistException(new DomainError.NotFoundDomainError(ErrorMessage.VEHICLE_NOT_EXIST.getMessage()));
        });
    }

    @Override
    @Transactional
    public Optional<OilVehicle.CreatedOilVehicle> getVehicle(final int vehicleId) {
        return vehiclesJpaRepo.findById(vehicleId)
                .map(this::loadVehicle);
    }

    @Override
    @Transactional
    public Optional<OilVehicle.CreatedOilVehicle> getVehicle(@NonNull final String label) {
        return vehiclesJpaRepo.findByLabel(label)
                .map(this::loadVehicle);
    }

    @Override
    public PageableOf<OilVehicle.CreatedOilVehicle> getVehicles(@NonNull final VehiclesQueryService.VehicleQuery query) {
        Page<VehicleJpa> page = vehiclesJpaRepo.findAll(VehicleSpecification.from(query), query.to());
        return new PageableOf<>(page.getTotalElements(), page.get().map(OilVehicle.CreatedOilVehicle::from).collect(Collectors.toList()));
    }

    @Override
    public void process(@NonNull final Vehicle.ActiveVehicle vehicle) {
        vehiclesJpaRepo.findById(vehicle.getVehicleId())
                .ifPresentOrElse(it -> {
                    vehiclesJpaRepo.save(it.active());
                }, () -> {
                    throw new AtlasException.ResourceNotExistException(new DomainError.NotFoundDomainError(ErrorMessage.VEHICLE_NOT_EXIST.getMessage()));
                });
    }

    @Override
    public void process(@NonNull final Vehicle.InActiveVehicle vehicle) {
        vehiclesJpaRepo.findById(vehicle.getVehicleId())
                .ifPresentOrElse(it -> {
                    vehiclesJpaRepo.save(it.inactive());
                }, () -> {
                    throw new AtlasException.ResourceNotExistException(new DomainError.NotFoundDomainError(ErrorMessage.VEHICLE_NOT_EXIST.getMessage()));
                });
    }

    /**
     * Add a point of interest for the vehicle.
     * @param vehicle
     * @param location
     */
    @Override
    public void addPointOfInterest(@NonNull final Vehicle.ActiveVehicle vehicle,
                                   @NonNull final OutdoorLocation.CreatedOutdoorLocation<?> location) {
        final LocationJpa locationJpa = getLocation(location.getLocationId());
        final VehicleJpa vehicleJpa = vehiclesJpaRepo.findById(vehicle.getVehicleId()).orElseThrow(() -> {
            throw new AtlasException.ResourceNotExistException(new DomainError.NotFoundDomainError(ErrorMessage.VEHICLE_NOT_EXIST.getMessage()));
        });
        vehiclePoisJpaRepo.save(VehiclePOIJpa.from(vehicleJpa, locationJpa));
    }

    @Override
    public void cleanAssociationsByLocation(final int locationId) {
        log.info("Clean up association of location {} with vehicles.", locationId);
        final LocationJpa locationJpa = getLocation(locationId);
        vehiclePoisJpaRepo.deleteAll(vehiclePoisJpaRepo.findByLocation(locationJpa));

    }

    @Override
    public void removePointOfInterest(@NonNull final Vehicle.ActiveVehicle vehicle,
                                      @NonNull final OutdoorLocation.CreatedOutdoorLocation<?> location) {
        final LocationJpa locationJpa = getLocation(location.getLocationId());
        final VehicleJpa vehicleJpa = vehiclesJpaRepo.findById(vehicle.getVehicleId()).orElseThrow(() -> {
            throw new AtlasException.ResourceNotExistException(new DomainError.NotFoundDomainError(ErrorMessage.VEHICLE_NOT_EXIST.getMessage()));
        });
        vehiclePoisJpaRepo.delete(VehiclePOIJpa.from(vehicleJpa, locationJpa));
    }

    /**
     * Load information for vehicle.
     * @param vehicleJpa
     * @return
     */
    private OilVehicle.CreatedOilVehicle loadVehicle(@NonNull final VehicleJpa vehicleJpa){
        final int totalCompartments = vehicleCompartmentsJpaRepo.findTotalCompartmentsByVehicle(vehicleJpa);
        log.info("Vehicle {} have {} compartments, loading devices and its data-types.", vehicleJpa.getLabel(), totalCompartments);
        List<OilVehicle.Compartment> compartments = IntStream.range(COMPARTMENT_START_INDEX,totalCompartments)
                .mapToObj(it -> {
                    final List<DataSensorDevice.CreatedDataSensorDevice> devices = vehicleCompartmentsJpaRepo.findByVehicleAndCompartment(vehicleJpa, it)
                            .stream()
                            .map(vc -> {
                                return (DataSensorDevice.CreatedDataSensorDevice) devicesRepo.getDevice(vc.getDevice().getId())
                                        .orElseThrow(() ->  {
                                            throw new AtlasException.MisConfigurationException(new DomainError.MisConfiguredDomainError("Device ["+vc.getDevice().getId()+"], must exist."));
                                        });
                            }).collect(Collectors.toList());
                    return new OilVehicle.Compartment(it, devices);
                }).collect(Collectors.toList());
        final List<OutdoorLocation.CreatedOutdoorLocation<?>> locations = vehiclePoisJpaRepo
                .findByVehicle(vehicleJpa)
                .stream()
                .map(poi -> OutdoorLocation.CreatedOutdoorLocation.from(poi.getLocation()))
                .collect(Collectors.toList());
        return OilVehicle.CreatedOilVehicle.from(vehicleJpa, locations, compartments);
    }

    /**
     * Internal call for retrieving information about the location from database.
     * @param locationId
     * @return location or throws a NotFoundException.
     */
    private LocationJpa getLocation(final int locationId){
        return locationsJpaRepo.findById(locationId).orElseThrow(() -> {
            throw new AtlasException.ResourceNotExistException(new DomainError.NotFoundDomainError(ErrorMessage.LOCATION_NOT_EXIST.getMessage()));
        });
    }

}
