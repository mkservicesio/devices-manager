package gr.esdalab.atlas.devices.core.application.query.impl;

import gr.esdalab.atlas.devices.adapters.rest.resources.out.BasicVehicleOutput;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.PageableOutput;
import gr.esdalab.atlas.devices.core.application.query.VehiclesQueryService;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.vehicles.VehicleOutput;
import gr.esdalab.atlas.devices.core.domain.common.PageableOf;
import gr.esdalab.atlas.devices.core.domain.repositories.VehiclesRepository;
import gr.esdalab.atlas.devices.core.domain.vehicles.impl.OilVehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of {@DevicesQueryService}
 */
@Service
@RequiredArgsConstructor
public class VehiclesQueryServiceImpl implements VehiclesQueryService {

    private final VehiclesRepository vehiclesRepo;

    @Override
    public PageableOutput<BasicVehicleOutput> listVehicles(final Map<String, String> query) {
        PageableOf<OilVehicle.CreatedOilVehicle> vehicles = vehiclesRepo.getVehicles(Optional.ofNullable(query).map(VehicleQuery::new).orElseGet(VehicleQuery::new));
        return new PageableOutput<>(vehicles.getTotal(), vehicles.getData().stream().map(BasicVehicleOutput::from).collect(Collectors.toList()));
    }

    @Override
    public Optional<VehicleOutput> getVehicle(int vehicleId) {
        return vehiclesRepo.getVehicle(vehicleId).map(VehicleOutput::from);
    }
}
