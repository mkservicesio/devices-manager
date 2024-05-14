package gr.esdalab.atlas.devices.core.application.cases;

import gr.esdalab.atlas.devices.common.resources.in.HealthInput;

/**
 * Edge Monitoring Use Case.
 */
public interface EdgeMonitoringUseCase {

    /**
     *
     * @param deviceUrn
     * @param healthInput
     */
    void healthCheck(String deviceUrn, HealthInput healthInput);

    /**
     * Device Health Check.
     * @param deviceId
     */
    void deviceHealthCheck(int deviceId);

    /**
     * Robot Health Check.
     * @param robotId
     */
    void robotHealthCheck(int robotId);
}
