package com.careerbooker.server;

import com.careerbooker.server.entity.*;
import com.careerbooker.server.repository.*;
import com.careerbooker.server.util.enums.Status;
import com.careerbooker.server.util.enums.TimeSlot;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan("com.careerbooker.server")
@EntityScan("com.careerbooker.server.entity")
@SpringBootTest
@Disabled
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

        SystemUser user = userRepository.findById(10L).get();
        ConsultantDays consultantDays = consultantDaysRepository.findById(9L).get();
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
        Appointments appointments = appointmentRepository.findById(3L).get();
        Assertions.assertThat(appointments.getAppointmentId()).isEqualTo(3L);
    }

    @Test
    @Order(3)
    public void getAppointmentList(){
        List<Appointments> appointmentsList = appointmentRepository.findAll();
        Assertions.assertThat(appointmentsList.size()).isGreaterThan(0);
    }

}
