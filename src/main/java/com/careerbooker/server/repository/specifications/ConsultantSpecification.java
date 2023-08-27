package com.careerbooker.server.repository.specifications;

import com.careerbooker.server.dto.request.ConsultantSearchDTO;
import com.careerbooker.server.entity.*;
import com.careerbooker.server.util.enums.Status;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class ConsultantSpecification {

    public Specification<Consultants> generateSearchCriteria(ConsultantSearchDTO consultantSearchDTO) {
        return (Specification<Consultants>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(consultantSearchDTO.getNic()) )
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(Consultants_.SYSTEM_USER).get(SystemUser_.NIC)),  "%"
                        .concat( consultantSearchDTO.getNic()).concat("%")));

            if (Objects.nonNull(consultantSearchDTO.getUserName()) )
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(Consultants_.SYSTEM_USER).get(SystemUser_.USERNAME)),  "%"
                        .concat( consultantSearchDTO.getUserName()).concat("%")));

            if (Objects.nonNull(consultantSearchDTO.getSpe_id()) )
                predicates.add(criteriaBuilder.equal(root.get(Consultants_.SPECIALIZATIONS).get(SpecializationType_.SPECIALIZATION_ID),
                        consultantSearchDTO.getSpe_id()));

            if (Objects.nonNull(consultantSearchDTO.getStatusCode()) && !consultantSearchDTO.getStatusCode().isEmpty())
                predicates.add(criteriaBuilder.equal(root.get(Consultants_.STATUS), Status.valueOf(consultantSearchDTO.getStatusCode())));

            predicates.add(criteriaBuilder.notEqual(root.get(Consultants_.STATUS), Status.deleted));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
    public Specification<Consultants> generateSearchCriteria(Status status) {
        return (Specification<Consultants>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.notEqual(root.get(Consultants_.STATUS), status));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
