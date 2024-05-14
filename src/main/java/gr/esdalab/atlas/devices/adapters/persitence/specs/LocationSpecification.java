package gr.esdalab.atlas.devices.adapters.persitence.specs;

import gr.esdalab.atlas.devices.adapters.persitence.entities.LocationJpa;
import gr.esdalab.atlas.devices.adapters.rest.resources.in.LocationInput;
import gr.esdalab.atlas.devices.core.application.query.LocationsQueryService;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Query Specification
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationSpecification implements Specification<LocationJpa> {

    private final Optional<LocationInput.LocationType> indoor;
    private final Optional<String> identifier;
    private final Optional<String> label;

    /**
     *
     * @param root must not be {@literal null}.
     * @param query must not be {@literal null}.
     * @param criteriaBuilder must not be {@literal null}.
     * @return
     */
    @Override
    public Predicate toPredicate(Root<LocationJpa> root,
                                 CriteriaQuery<?> query,
                                 CriteriaBuilder criteriaBuilder) {
        final List<Predicate> predicates = new ArrayList<>();
        identifier.ifPresent(it -> predicates.add(criteriaBuilder.like(root.get("identity"),"%"+it+"%")));
        label.ifPresent(it -> predicates.add(criteriaBuilder.like(root.get("friendlyName"),"%"+it+"%")));
        indoor.ifPresent(it -> {
            predicates.add(criteriaBuilder.equal(root.get("indoor"), (it == LocationInput.LocationType.INDOOR)));
        });
        predicates.add(criteriaBuilder.equal(root.get("visible"), true));
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    /**
     * Generate the query filters.
     * @param query
     * @return
     */
    public static LocationSpecification from(@NonNull final LocationsQueryService.LocationQuery query) {
        return new LocationSpecification(
                query.getType(),
                query.getIdentifier(),
                query.getLabel()
        );
    }

}
