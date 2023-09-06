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
public class ConsultantDaysRepositoryTest {

    @Autowired
    ConsultantRepository consultantRepository;

    @Autowired
    DayRepository dayRepository;

    @Autowired
    ConsultantDaysRepository consultantDaysRepository;

    @Test
    @Order(1)
    @Rollback(value = false)
    public void saveConsultantDays(){

        Consultants consultants = consultantRepository.findById(1L).get();
        Days days = dayRepository.findById(1L).get();
        ConsultantDays consultantDays = new ConsultantDays();
        consultantDays.setDays(days);
        consultantDays.setConsultant(consultants);
        consultantDays.setSlot(TimeSlot.FOUR);
        consultantDays.setStatus(Status.active);

        consultantDays.setCreatedUser("test");
        consultantDays.setModifiedUser("test");
        consultantDays.setCreatedDate(new Date());
        consultantDays.setModifiedDate(new Date());

        ConsultantDays savedConsultantDays = consultantDaysRepository.save(consultantDays);

        Assertions.assertThat(savedConsultantDays).isNotNull();
        Assertions.assertThat(savedConsultantDays.getId()).isGreaterThan(0);
    }

    @Test
    @Order(2)
    public void getConsultantDaysById(){
        ConsultantDays consultantDays = consultantDaysRepository.findById(1L).get();
        Assertions.assertThat(consultantDays.getId()).isEqualTo(1L);
    }

    @Test
    @Order(3)
    public void getConsultantDaysList(){
        List<ConsultantDays> consultantDaysList = consultantDaysRepository.findAll();
        Assertions.assertThat(consultantDaysList.size()).isGreaterThan(0);
    }

    @Test
    @Order(4)
    @Rollback(value = false)
    public void updateConsultantDays(){
        ConsultantDays consultantDays = consultantDaysRepository.findById(1L).get();
        consultantDays.setSlot(TimeSlot.ONE);
        ConsultantDays updatedConsultantDays =  consultantDaysRepository.save(consultantDays);
        Assertions.assertThat(updatedConsultantDays.getStatus()).isEqualTo(Status.booked);
    }


}
