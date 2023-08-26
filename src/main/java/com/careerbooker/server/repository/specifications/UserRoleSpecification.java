package com.careerbooker.server.repository.specifications;

import com.careerbooker.server.dto.request.UserRoleSearchDTO;
import com.careerbooker.server.entity.UserRole;
import com.careerbooker.server.entity.UserRole_;
import com.careerbooker.server.util.enums.Status;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class UserRoleSpecification {

    public Specification<UserRole> generateSearchCriteria(UserRoleSearchDTO userRoleSearchDTO) {
        return (Specification<UserRole>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(userRoleSearchDTO.getDescription()) )
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(UserRole_.description)),  "%"
                        .concat( userRoleSearchDTO.getDescription()).concat("%")));

            if (Objects.nonNull(userRoleSearchDTO.getStatusCode()) && !userRoleSearchDTO.getStatusCode().isEmpty())
                predicates.add(criteriaBuilder.equal(root.get(UserRole_.STATUS_CODE), Status.valueOf(userRoleSearchDTO.getStatusCode())));

            predicates.add(criteriaBuilder.notEqual(root.get(UserRole_.STATUS_CODE), Status.deleted));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
    public Specification<UserRole> generateSearchCriteria(Status status) {
        return (Specification<UserRole>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.notEqual(root.get(UserRole_.STATUS_CODE), status));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
