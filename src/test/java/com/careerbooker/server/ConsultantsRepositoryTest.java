package com.careerbooker.server;

import com.careerbooker.server.entity.*;
import com.careerbooker.server.repository.ConsultantRepository;
import com.careerbooker.server.repository.SpecializationRepository;
import com.careerbooker.server.repository.UserRepository;
import com.careerbooker.server.util.enums.Status;
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
public class ConsultantsRepositoryTest {

    @Autowired
    ConsultantRepository consultantRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SpecializationRepository specializationRepository;

    @Test
    @Order(1)
    @Rollback(value = false)
    public void saveConsultant(){

        SystemUser systemUser = userRepository.findById(10L).get();
        SpecializationType specializationType = specializationRepository.findById(5L).get();
        Consultants consultants = new Consultants();
        consultants.setSpecializations(specializationType);
        consultants.setSystemUser(systemUser);
        consultants.setStatus(Status.active);

        consultants.setCreatedUser("test");
        consultants.setModifiedUser("test");
        consultants.setCreatedDate(new Date());
        consultants.setModifiedDate(new Date());

        Consultants savedConsultants = consultantRepository.save(consultants);

        Assertions.assertThat(savedConsultants).isNotNull();
        Assertions.assertThat(savedConsultants.getConsultantId()).isGreaterThan(0);
    }

    @Test
    @Order(2)
    public void getConsultantById(){
        Consultants consultants = consultantRepository.findById(5L).get();
        Assertions.assertThat(consultants.getConsultantId()).isEqualTo(5L);
    }

    @Test
    @Order(3)
    public void getConsultantList(){
        List<Consultants> consultantsList = consultantRepository.findAll();
        Assertions.assertThat(consultantsList.size()).isGreaterThan(0);
    }

    @Test
    @Order(4)
    @Rollback(value = false)
    public void updateConsultant(){
        Consultants consultants = consultantRepository.findById(5L).get();
        consultants.setStatus(Status.booked);
        Consultants updatedConsultants =  consultantRepository.save(consultants);
        Assertions.assertThat(updatedConsultants.getStatus()).isEqualTo(Status.booked);
    }


}
