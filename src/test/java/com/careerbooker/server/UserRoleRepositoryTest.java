package com.careerbooker.server;

import com.careerbooker.server.entity.UserRole;
import com.careerbooker.server.repository.UserRoleRepository;
import com.careerbooker.server.util.enums.Status;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
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
public class UserRoleRepositoryTest {

    @Autowired
    UserRoleRepository userRoleRepository;

    @Test
    @Order(1)
    @Rollback(value = false)
    public void saveAdminUserRole(){
        UserRole userRole = new UserRole();
        userRole.setCode("ADMIN");
        userRole.setDescription("Admin");
        userRole.setStatusCode(Status.active);
        userRole.setCreatedUser("test");
        userRole.setLastUpdatedUser("test");
        userRole.setCreatedTime(new Date());
        userRole.setLastUpdatedTime(new Date());

        UserRole savedUserRole = userRoleRepository.save(userRole);

        Assertions.assertThat(savedUserRole).isNotNull();
        Assertions.assertThat(savedUserRole.getId()).isGreaterThan(0);
    }
    @Test
    @Order(2)
    public void getAdminUserRoleById(){
        UserRole userRole = userRoleRepository.findById(8L).get();
        Assertions.assertThat(userRole.getId()).isEqualTo(8L);
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
        UserRole userRole = userRoleRepository.findById(8L).get();
        userRole.setDescription("Administration");
        UserRole updatedUserRole =  userRoleRepository.save(userRole);
        Assertions.assertThat(updatedUserRole.getDescription()).isEqualTo("Administration");
    }


}
