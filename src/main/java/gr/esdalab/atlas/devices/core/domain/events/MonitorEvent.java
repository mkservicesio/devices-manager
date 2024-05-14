package gr.esdalab.atlas.devices.core.domain.events;

import gr.esdalab.atlas.devices.core.domain.robots.Robot;
import gr.esdalab.atlas.devices.core.domain.sensors.SensorDevice;
import gr.esdalab.atlas.devices.core.domain.sensors.impl.DataSensorDevice;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.context.ApplicationEvent;

/**
 * An abstract class for Monitoring Events
 */
@Getter
public abstract class MonitorEvent extends ApplicationEvent {

    /**
     *
     * @param source
     */
    protected MonitorEvent(Object source) {
        super(source);
    }

    /**
     * Monitor event for Robots.
     */
    @Getter
    public static class RobotMonitorEvent extends MonitorEvent{

        private final Robot.CreatedRobot robot;

        /**
         * @param source
         */
        public RobotMonitorEvent(@NonNull final Object source,
                                    @NonNull final Robot.CreatedRobot robot) {
            super(source);
            this.robot = robot;
        }
    }

    /**
     * Monitor event for Devices.
     */
    @Getter
    public static class DeviceMonitorEvent<T extends SensorDevice.SensingDevice> extends MonitorEvent{

        private final T device;

        /**
         * @param source
         */
        public DeviceMonitorEvent(@NonNull final Object source,
                                     @NonNull final T device) {
            super(source);
            this.device = device;
        }
    }

}
