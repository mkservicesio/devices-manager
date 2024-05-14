package gr.esdalab.atlas.devices.core.application.query;

import gr.esdalab.atlas.devices.adapters.rest.resources.out.BasicVehicleOutput;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.PageableOutput;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.vehicles.VehicleOutput;
import gr.esdalab.atlas.devices.common.utils.QueryUtils;
import gr.esdalab.atlas.devices.core.domain.vehicles.Vehicle;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Map;
import java.util.Optional;

/**
 * Service for query vehicle information.
 */
public interface VehiclesQueryService {

    /**
     * List available vehicles.
     * @return
     */
    PageableOutput<BasicVehicleOutput> listVehicles(Map<String, String> query);

    /**
     * Get Specific Vehicle information
     * @param vehicleId
     * @return
     */
    Optional<VehicleOutput> getVehicle(int vehicleId);


    /**
     * Class Re-presenting a query for vehicles
     */
    @Getter
    class VehicleQuery{

        private final int pageIndex;
        private final int pageSize;
        private final Sort sort;
        private final Optional<Integer> owner;
        private final Optional<Vehicle.VehicleStatus> status;
        private final Optional<String> label;

        /**
         * Default Constructor
         */
        public VehicleQuery(){
            this.pageIndex = 0;
            this.pageSize = 50;
            this.sort = Sort.by(Sort.Direction.ASC, "label");
            this.owner = Optional.empty();
            this.status = Optional.empty();
            this.label = Optional.empty();
        }

        /**
         * Query-based Constructor
         * @param query
         */
        public VehicleQuery(@NonNull final Map<String, String> query){
            this.pageIndex = Integer.parseInt(Optional.ofNullable(query.get("page")).orElse("0"));
            this.pageSize = Integer.parseInt(Optional.ofNullable(query.get("pageSize")).orElse("50"));
            this.sort = Optional.ofNullable(query.get("sort"))
                    .map(it -> {
                        final String[] sorting = QueryUtils.getSort(it);
                        return Sort.by(Sort.Direction.valueOf(sorting[1].toUpperCase()), sorting[0]);
                    })
                    .orElseGet(() -> Sort.by(Sort.Direction.ASC, "label"));
            this.owner = Optional.ofNullable(query.get("owner")).map(Integer::parseInt);
            this.status = Optional.ofNullable(query.get("status")).map(it -> Vehicle.VehicleStatus.valueOf(it.toUpperCase()));
            this.label = Optional.ofNullable(query.get("label"));
        }

        /**
         * Generate a page request.
         * @return
         */
        public PageRequest to(){
            return PageRequest.of(pageIndex, pageSize, sort);
        }

    }

}
