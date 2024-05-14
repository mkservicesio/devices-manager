package gr.esdalab.atlas.devices.adapters.persitence.specs;

import gr.esdalab.atlas.devices.adapters.persitence.entities.VehicleJpa;
import gr.esdalab.atlas.devices.core.application.query.VehiclesQueryService;
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
public class VehicleSpecification implements Specification<VehicleJpa> {

    private final Optional<VehicleJpa.VehicleStatus> status;
    private final Optional<Integer> owner;
    private final Optional<String> label;

    /**
     *
     * @param root must not be {@literal null}.
     * @param query must not be {@literal null}.
     * @param criteriaBuilder must not be {@literal null}.
     * @return
     */
    @Override
    public Predicate toPredicate(Root<VehicleJpa> root,
                                 CriteriaQuery<?> query,
                                 CriteriaBuilder criteriaBuilder) {
        final List<Predicate> predicates = new ArrayList<>();
        owner.ifPresent(it -> predicates.add(criteriaBuilder.equal(root.get("owner"),it)));
        label.ifPresent(it -> predicates.add(criteriaBuilder.like(root.get("label"),"%"+it+"%")));
        status.ifPresent(it -> predicates.add(criteriaBuilder.equal(root.get("status"), it)));
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    /**
     * Generate the query filters.
     * @param query
     * @return
     */
    public static VehicleSpecification from(@NonNull final VehiclesQueryService.VehicleQuery query) {
        return new VehicleSpecification(
                query.getStatus().map(it -> VehicleJpa.VehicleStatus.valueOf(it.name())),
                query.getOwner(),
                query.getLabel()
        );
    }

}
