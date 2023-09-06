package com.careerbooker.server;

import com.careerbooker.server.entity.SpecializationType;
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
public class SpecializationRepositoryTest {

    @Autowired
    SpecializationRepository specializationRepository;

    @Test
    @Order(1)
    @Rollback(value = false)
    public void saveSpecialization(){

        //given - precondition or setup
        SpecializationType specialization = new SpecializationType();
        specialization.setName("IT Engineer");
        specialization.setStatus(Status.active);
        specialization.setCreatedUser("test");
        specialization.setModifiedUser("test");
        specialization.setCreatedDate(new Date());
        specialization.setModifiedDate(new Date());

        SpecializationType savedSpecialization = specializationRepository.save(specialization);

        // then - verify the output
        Assertions.assertThat(savedSpecialization).isNotNull();
        Assertions.assertThat(savedSpecialization.getSpecializationId()).isGreaterThan(0);
    }

    @Test
    @Order(2)
    public void getSpecializationById(){
        SpecializationType specializationType = specializationRepository.findById(1L).get();
        Assertions.assertThat(specializationType.getSpecializationId()).isEqualTo(1L);
    }

    @Test
    @Order(3)
    public void getSpecializationList(){
        List<SpecializationType> specializationTypeList = specializationRepository.findAll();
        Assertions.assertThat(specializationTypeList.size()).isGreaterThan(0);
    }

    @Test
    @Order(4)
    @Rollback(value = false)
    public void updateSpecialization(){
        SpecializationType specializationType = specializationRepository.findById(1L).get();
        specializationType.setName("Auto Mobile");
        SpecializationType updatedSpecialization =  specializationRepository.save(specializationType);
        Assertions.assertThat(updatedSpecialization.getName()).isEqualTo("Auto Mobile");
    }


}
