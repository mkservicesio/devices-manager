package gr.esdalab.atlas.devices.common;

import lombok.RequiredArgsConstructor;

/**
 * Marker interface for Devices Status
 */
public interface DeviceStatus {

    int INITIAL_STATUS_NUMERIC_VALUE = 1;
    int DELETED_NUMERIC_VALUE = 99;

    /**
     * Get the numeric value of the status.
     * @return the numeric representation of the Device.
     */
    int getStatus();

    @RequiredArgsConstructor
    enum GenericDeviceStatus implements DeviceStatus {

        /**
         *  Device is registered but is not connected.
         */
        NOT_CONNECTED(INITIAL_STATUS_NUMERIC_VALUE),

        /**
         *  Device is connected.
         */
        CONNECTED(39),

        /**
         * Devices is deleted.
         */
        DELETED(DELETED_NUMERIC_VALUE);

        private final int id;

        @Override
        public int getStatus() {
            return id;
        }
    }

    @RequiredArgsConstructor
    enum GatewayStatus implements DeviceStatus{


        INSTALLED(INITIAL_STATUS_NUMERIC_VALUE),

        ONLINE(19),

        DELETED(DELETED_NUMERIC_VALUE);

        private final int id;

        @Override
        public int getStatus() {
            return id;
        }

    }

    @RequiredArgsConstructor
    enum RobotStatus implements DeviceStatus{

        INSTALLED(INITIAL_STATUS_NUMERIC_VALUE),

        ONLINE(19),

        IS_MOVING(39),

        OFFLINE(59),

        DELETED(DELETED_NUMERIC_VALUE);

        private final int id;

        @Override
        public int getStatus() {
            return id;
        }

    }

}
