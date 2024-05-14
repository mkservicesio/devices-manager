package gr.esdalab.atlas.devices.adapters.rest.resources.out.vehicles;

import gr.esdalab.atlas.devices.adapters.rest.resources.inout.ThresholdInputOutput;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.IdOutput;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.PointOfInterestOutput;
import gr.esdalab.atlas.devices.core.domain.sensors.impl.DataSensorDevice;
import gr.esdalab.atlas.devices.core.domain.vehicles.impl.OilVehicle;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class VehicleOutput {

    /**
     * The ATLAS ID
     */
    private final int id;

    /**
     * The Owner ID
     */
    private final int ownerId;

    /**
     * The label of the vehicle.
     */
    private final String label;

    /**
     * Vehicle Status
     */
    private final String status;

    /**
     * Vehicles Compartment
     */
    private final List<CompartmentOutput> compartments;

    /**
     * Point of interests for the vehicle.
     */
    private final List<PointOfInterestOutput> pointsOfInterests;

    /**
     *
     * @param oilVehicle
     * @return
     */
    public static VehicleOutput from(@NonNull final OilVehicle.CreatedOilVehicle oilVehicle) {
        return new VehicleOutput(oilVehicle.getId(),
                oilVehicle.getOwner().getOwnerId(),
                oilVehicle.getLabel(),
                oilVehicle.getStatus().name(),
                oilVehicle.getCompartments().stream().map(CompartmentOutput::from).collect(Collectors.toList()),
                oilVehicle.getLocations().stream().map(PointOfInterestOutput::from).collect(Collectors.toList())
        );
    }

    /**
     * Compartment Output
     */
    @Getter
    @RequiredArgsConstructor
    public static class CompartmentOutput{
        private final int idx; //The index of the compartment.
        private final List<VehicleSensorOutput> sensors; //The sensors of the compartment.

        public static CompartmentOutput from(@NonNull final OilVehicle.Compartment compartment) {
            return new CompartmentOutput(compartment.getIdx(),
                    compartment.getSensors().stream().map(VehicleSensorOutput::from).collect(Collectors.toList()));
        }
    }

    /**
     * The Sensor Vehicle output.
     */
    @Getter
    @RequiredArgsConstructor
    public static class VehicleSensorOutput{
        private final int id;
        private final String urn;
        private final List<IdOutput.DatatypeThresholdOutput> datatypes;

        public static VehicleSensorOutput from(@NonNull final DataSensorDevice.CreatedDataSensorDevice device) {
            return new VehicleSensorOutput(
                    device.getId(),
                    device.getUrn(),
                    device.getDatatypes()
                            .stream()
                            .map(datatype -> new IdOutput.DatatypeThresholdOutput(datatype.getId(), datatype.getLabel(), ThresholdInputOutput.from(datatype.getThreshold())))
                            .collect(Collectors.toList())
            );
        }
    }

}
