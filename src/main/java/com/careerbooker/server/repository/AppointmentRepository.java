package com.careerbooker.server.repository;

import com.careerbooker.server.entity.*;
import com.careerbooker.server.util.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Date;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointments, Long>, JpaSpecificationExecutor<Appointments> {
    Appointments findAppointmentsByAppointmentIdAndStatusNot(Long id, Status status);
    List<Appointments> findAllBySystemUserAndStatusNot(SystemUser systemUser, Status status);
    Long countAllByConsultantDays_ConsultantAndStatusNot(Consultants consultant, Status status);
    Long countAllByStatus(Status status);

}
