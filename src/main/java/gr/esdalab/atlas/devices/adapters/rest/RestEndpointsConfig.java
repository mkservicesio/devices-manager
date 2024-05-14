package gr.esdalab.atlas.devices.adapters.rest;

public interface RestEndpointsConfig {

	/**
	 * 	Indicative API Version of the API.
	 */
	String DEVICES_MANAGER_API_VERSION = "/v1";

	/**
	 * Search API
	 */
	String DEVICES_MANAGER_SEARCH_API = DEVICES_MANAGER_API_VERSION + "/search";

	/**
	 * Vehicles API
	 */
	String DEVICES_MANAGER_VEHICLES_API = DEVICES_MANAGER_API_VERSION + "/vehicles";
	String DEVICES_MANAGER_VEHICLES_COMMANDS_API = DEVICES_MANAGER_VEHICLES_API + "/{vehicleId}/commands";

	/**
	 * Robots API
	 */
	String DEVICES_MANAGER_ROBOTS_API = DEVICES_MANAGER_API_VERSION + "/robots";

	/**
	 * Devices API
	 */
	String DEVICES_MANAGER_DEVICES_API = DEVICES_MANAGER_API_VERSION + "/devices";

	/**
	 * Gateways API
	 */
	String DEVICES_MANAGER_GATEWAYS_API = DEVICES_MANAGER_API_VERSION + "/gateways";

	/**
	 * Datatypes API
	 */
	String DEVICES_MANAGER_DATATYPES_API = DEVICES_MANAGER_API_VERSION + "/datatypes";

	/**
	 * Locations API
	 */
	String DEVICES_MANAGER_LOCATIONS_API = DEVICES_MANAGER_API_VERSION + "/locations";

	/**
	 * Appendages API
	 */
	String DEVICES_MANAGER_APPENDAGES_API = DEVICES_MANAGER_API_VERSION + "/appendages";

}
