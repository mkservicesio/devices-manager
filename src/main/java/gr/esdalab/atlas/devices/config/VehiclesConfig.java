package gr.esdalab.atlas.devices.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Main Configurations for devices manager.
 */
@Configuration
@Getter
public class VehiclesConfig {

    @Value("${atlas.vehicles.compartment.limit}")
    private int compartmentsLimit;

    @Value("#{'${atlas.vehicles.compartment.sensors}'.split(',')}")
    private List<String> compartmentSensors;

}
