package com.careerbooker.server.repository.specifications;

import com.careerbooker.server.dto.request.SpecializationSearchDTO;
import com.careerbooker.server.entity.SpecializationType;
import com.careerbooker.server.entity.SpecializationType_;
import com.careerbooker.server.util.enums.Status;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class SpecializationSpecification {

    public Specification<SpecializationType> generateSearchCriteria(SpecializationSearchDTO specializationSearchDTO) {
        return (Specification<SpecializationType>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(specializationSearchDTO.getName()) )
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(SpecializationType_.NAME)),  "%"
                        .concat( specializationSearchDTO.getName().toLowerCase()).concat("%")));

            if (Objects.nonNull(specializationSearchDTO.getStatusCode()) && !specializationSearchDTO.getStatusCode().isEmpty())
                predicates.add(criteriaBuilder.equal(root.get(SpecializationType_.STATUS), Status.valueOf(specializationSearchDTO.getStatusCode())));

            predicates.add(criteriaBuilder.notEqual(root.get(SpecializationType_.STATUS), Status.deleted));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
    public Specification<SpecializationType> generateSearchCriteria(Status status) {
        return (Specification<SpecializationType>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.notEqual(root.get(SpecializationType_.STATUS), status));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
