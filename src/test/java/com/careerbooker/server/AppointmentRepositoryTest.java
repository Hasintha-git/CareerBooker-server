package com.careerbooker.server;

import com.careerbooker.server.entity.*;
import com.careerbooker.server.repository.*;
import com.careerbooker.server.util.enums.Status;
import com.careerbooker.server.util.enums.TimeSlot;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppointmentRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ConsultantDaysRepository consultantDaysRepository;

    @Autowired
    AppointmentRepository appointmentRepository;

    @Test
    @Order(1)
    @Rollback(value = false)
    public void createAppointment(){

        SystemUser user = userRepository.findById(1L).get();
        ConsultantDays consultantDays = consultantDaysRepository.findById(1L).get();
        Appointments appointments = new Appointments();
        appointments.setConsultantDays(consultantDays);
        appointments.setSystemUser(user);
        appointments.setStatus(Status.active);

        appointments.setCreatedUser("test");
        appointments.setModifiedUser("test");
        appointments.setCreatedDate(new Date());
        appointments.setModifiedDate(new Date());

        Appointments savedAppointments = appointmentRepository.save(appointments);

        Assertions.assertThat(savedAppointments).isNotNull();
        Assertions.assertThat(savedAppointments.getAppointmentId()).isGreaterThan(0);
    }

    @Test
    @Order(2)
    public void getAppointmentById(){
        Appointments appointments = appointmentRepository.findById(1L).get();
        Assertions.assertThat(appointments.getAppointmentId()).isEqualTo(1L);
    }

    @Test
    @Order(3)
    public void getAppointmentList(){
        List<Appointments> appointmentsList = appointmentRepository.findAll();
        Assertions.assertThat(appointmentsList.size()).isGreaterThan(0);
    }

}
