package com.careerbooker.server.repository.specifications;

import com.careerbooker.server.dto.request.AppointmentSearchDTO;
import com.careerbooker.server.dto.request.ConsultantSearchDTO;
import com.careerbooker.server.entity.*;
import com.careerbooker.server.util.enums.Status;
import com.careerbooker.server.util.enums.TimeSlot;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class AppointmentSpecification {

    public Specification<Appointments> generateSearchCriteria(AppointmentSearchDTO appointmentSearchDTO) {
        return (Specification<Appointments>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(appointmentSearchDTO.getConsultantName()) )
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(Appointments_.CONSULTANT_DAYS).get(ConsultantDays_.CONSULTANT).get(Consultants_.SYSTEM_USER).get(SystemUser_.USERNAME)),  "%"
                        .concat( appointmentSearchDTO.getConsultantName()).concat("%")));

            if (Objects.nonNull(appointmentSearchDTO.getUserName()) )
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(Appointments_.SYSTEM_USER).get(SystemUser_.USERNAME)),  "%"
                        .concat( appointmentSearchDTO.getUserName()).concat("%")));

            if (Objects.nonNull(appointmentSearchDTO.getSpecialization()) )
                predicates.add(criteriaBuilder.equal(root.get(Appointments_.CONSULTANT_DAYS).get(ConsultantDays_.CONSULTANT).get(Consultants_.SPECIALIZATIONS).get(SpecializationType_.SPECIALIZATION_ID),
                        appointmentSearchDTO.getSpecialization()));

            if (Objects.nonNull(appointmentSearchDTO.getSlotId()) )
                predicates.add(criteriaBuilder.equal(root.get(Appointments_.CONSULTANT_DAYS).get(ConsultantDays_.SLOT),
                        TimeSlot.getTimeSlot(appointmentSearchDTO.getSlotId())));

            if (Objects.nonNull(appointmentSearchDTO.getStatus()) && !appointmentSearchDTO.getStatus().isEmpty())
                predicates.add(criteriaBuilder.equal(root.get(Appointments_.STATUS), Status.valueOf(appointmentSearchDTO.getStatus())));

            predicates.add(criteriaBuilder.notEqual(root.get(Appointments_.STATUS), Status.deleted));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Specification<Appointments> generateSearchCriteria(Status status) {
        return (Specification<Appointments>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.notEqual(root.get(Appointments_.STATUS), status));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
