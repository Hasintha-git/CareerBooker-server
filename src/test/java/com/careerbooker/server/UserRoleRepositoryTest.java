package com.careerbooker.server;

import com.careerbooker.server.entity.UserRole;
import com.careerbooker.server.repository.UserRoleRepository;
import com.careerbooker.server.util.enums.Status;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRoleRepositoryTest {

    @Autowired
    UserRoleRepository userRoleRepository;


    @Test
    @Order(1)
    @Rollback(value = false)
    public void saveAdminUserRole(){

        //given - precondition or setup
        UserRole userRole = new UserRole();
        userRole.setCode("ADMIN");
        userRole.setDescription("Admin");
        userRole.setStatusCode(Status.active);
        userRole.setCreatedUser("test");
        userRole.setLastUpdatedUser("test");
        userRole.setCreatedTime(new Date());
        userRole.setLastUpdatedTime(new Date());

        UserRole savedUserRole = userRoleRepository.save(userRole);

        // then - verify the output
        Assertions.assertThat(savedUserRole).isNotNull();
        Assertions.assertThat(savedUserRole.getId()).isGreaterThan(0);
    }

    @Test
    @Order(2)
    public void getAdminUserRoleById(){
        UserRole userRole = userRoleRepository.findById(1L).get();
        Assertions.assertThat(userRole.getId()).isEqualTo(1L);
    }

    @Test
    @Order(3)
    public void getUserRoleList(){
        List<UserRole> userRoleList = userRoleRepository.findAll();
        Assertions.assertThat(userRoleList.size()).isGreaterThan(0);
    }

    @Test
    @Order(4)
    @Rollback(value = false)
    public void updateUserRole(){
        UserRole userRole = userRoleRepository.findById(1L).get();
        userRole.setDescription("Administration");
        UserRole updatedUserRole =  userRoleRepository.save(userRole);
        Assertions.assertThat(updatedUserRole.getDescription()).isEqualTo("Administration");
    }

    @Test
    @Order(5)
    @Rollback(value = false)
    public void deleteUserRole(){
        UserRole userRole = userRoleRepository.findById(1L).get();
        userRole.setStatusCode(Status.deleted);
        UserRole updatedUserRole =  userRoleRepository.save(userRole);
        Assertions.assertThat(updatedUserRole.getStatusCode()).isEqualTo(Status.deleted);
    }
}
