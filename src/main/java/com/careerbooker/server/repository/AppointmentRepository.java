package com.careerbooker.server.repository;

import com.careerbooker.server.entity.Appointments;
import com.careerbooker.server.entity.Days;
import com.careerbooker.server.entity.SystemUser;
import com.careerbooker.server.util.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointments, Long>, JpaSpecificationExecutor<Appointments> {
    Appointments findAppointmentsByAppointmentIdAndStatusNot(Long id, Status status);

}
