package gr.esdalab.atlas.devices.core.application.query;

import gr.esdalab.atlas.devices.adapters.rest.resources.in.LocationInput;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.LocationMapOutput;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.LocationOutput;
import gr.esdalab.atlas.devices.adapters.rest.resources.out.PageableOutput;
import gr.esdalab.atlas.devices.common.utils.QueryUtils;
import gr.esdalab.atlas.devices.core.domain.vehicles.Vehicle;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service for query gateways information.
 */
public interface LocationsQueryService {

    /**
     * Return all available locations
     * @return
     */
    PageableOutput<LocationOutput> getLocations(Map<String, String> query);

    /**
     * Get Specific Location by its ID
     * @param locationId
     * @return
     */
    Optional<LocationOutput> getLocation(int locationId);

    /**
     *
     * @param locationId
     * @return
     */
    Optional<LocationMapOutput> getMapOf(int locationId);

    /**
     *
     * @param appendageId
     * @return
     */
    Optional<LocationOutput> getLocationByAppendage(int appendageId);

    /**
     * Class Re-presenting a query for locations
     */
    @Getter
    class LocationQuery{

        private final int pageIndex;
        private final int pageSize;
        private final Sort sort;
        private final Optional<String> identifier;
        private final Optional<LocationInput.LocationType> type;
        private final Optional<String> label;

        /**
         * Default Constructor
         */
        public LocationQuery(){
            this.pageIndex = 0;
            this.pageSize = 50;
            this.sort = Sort.by(Sort.Direction.ASC, "friendlyName");
            this.identifier = Optional.empty();
            this.type = Optional.empty();
            this.label = Optional.empty();
        }

        /**
         * Query-based Constructor
         * @param query
         */
        public LocationQuery(@NonNull final Map<String, String> query){
            this.pageIndex = Integer.parseInt(Optional.ofNullable(query.get("page")).orElse("0"));
            this.pageSize = Integer.parseInt(Optional.ofNullable(query.get("pageSize")).orElse("50"));
            this.sort = Optional.ofNullable(query.get("sort"))
                    .map(it -> {
                        final String[] sorting = QueryUtils.getSort(it);
                        return Sort.by(Sort.Direction.valueOf(sorting[1].toUpperCase()), overrideOf(sorting[0]));
                    })
                    .orElseGet(() -> Sort.by(Sort.Direction.ASC, "friendlyName"));
            this.identifier = Optional.ofNullable(query.get("identity"));
            this.type = Optional.ofNullable(query.get("type")).map(it -> LocationInput.LocationType.valueOf(it.toUpperCase()));
            this.label = Optional.ofNullable(query.get("friendlyName"));
        }

        /**
         * Overrides from API to Database.
         * @return
         */
        private String overrideOf(final String sortBy) {
            return Optional.ofNullable(sortBy).map(it -> {
                if( it.equals("label") ) return "friendlyName";
                if( it.equals("locationId") ) return "id";
                return it;
            }).orElse("friendlyName");
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
