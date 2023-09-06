package com.careerbooker.server.repository;

import com.careerbooker.server.entity.ConsultantDays;
import com.careerbooker.server.entity.Consultants;
import com.careerbooker.server.entity.Days;
import com.careerbooker.server.util.enums.Status;
import com.careerbooker.server.util.enums.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ConsultantDaysRepository extends JpaRepository<ConsultantDays, Long>, JpaSpecificationExecutor<Consultants> {
    List<Consultants> findAllByStatus(Status status);
    List<Consultants> findAllByConsultant(Consultants consultants);
    ConsultantDays findConsultantDaysByIdAndStatus(Long id, Status status);
    List<ConsultantDays> findAllByConsultantAndStatus(Consultants consultants, Status status);
    List<ConsultantDays> findAllByConsultantAndDaysAndStatus(Consultants consultants, Days day, Status status);
    Long countAllByConsultantAndDaysAndSlotAndStatusNot(Consultants consultants, Days day, TimeSlot slot, Status status);
    Long countAllByConsultantAndDaysAndSlotAndStatus(Consultants consultants, Days day, TimeSlot slot, Status status);
    ConsultantDays findByConsultantAndDaysAndSlotAndStatus(Consultants consultants, Days day, TimeSlot slot, Status status);


}
