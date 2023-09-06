package com.careerbooker.server;

import com.careerbooker.server.entity.Days;
import com.careerbooker.server.entity.SpecializationType;
import com.careerbooker.server.repository.DayRepository;
import com.careerbooker.server.repository.SpecializationRepository;
import com.careerbooker.server.util.enums.Status;
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
public class DayRepositoryTest {

    @Autowired
    DayRepository dayRepository;

    @Test
    @Order(1)
    @Rollback(value = false)
    public void saveDay(){

        Days days = new Days();
        days.setDate(new Date());
        days.setStatusCode(Status.active);

        Days savedDay = dayRepository.save(days);

        Assertions.assertThat(savedDay).isNotNull();
        Assertions.assertThat(savedDay.getDay_id()).isGreaterThan(0);
    }

    @Test
    @Order(2)
    public void getDayById(){
        Days days = dayRepository.findById(1L).get();
        Assertions.assertThat(days.getDay_id()).isEqualTo(1L);
    }

    @Test
    @Order(3)
    public void getDayList(){
        List<Days> daysList = dayRepository.findAll();
        Assertions.assertThat(daysList.size()).isGreaterThan(0);
    }

    @Test
    @Order(4)
    @Rollback(value = false)
    public void updateDay(){
        Days days = dayRepository.findById(1L).get();
        days.setStatusCode(Status.hold);
        Days updatedDay =  dayRepository.save(days);
        Assertions.assertThat(updatedDay.getStatusCode()).isEqualTo(Status.hold);
    }


}
