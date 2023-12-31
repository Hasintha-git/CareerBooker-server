package com.careerbooker.server;

import com.careerbooker.server.entity.SystemUser;
import com.careerbooker.server.entity.UserRole;
import com.careerbooker.server.repository.UserRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan("com.careerbooker.server")
@EntityScan("com.careerbooker.server.entity")
@SpringBootTest
@Disabled
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserRoleRepository userRoleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Test
    @Order(1)
    @Rollback(value = false)
    public void saveAdminUser(){

        UserRole userRole = userRoleRepository.findById(8L).get();

        SystemUser user = new SystemUser();
        user.setUserRole(userRole);
        user.setAttempt(0);
        String encode = passwordEncoder.encode("12345678");
        user.setPassword(encode);
        user.setPwStatus(Status.active);
        user.setUsername("test");
        user.setAddress("Colombo");
        user.setCity("Mathugama");
        user.setEmail("test@gmail.com");
        user.setFullName("test user");
        user.setDateOfBirth(new Date());
        user.setMobileNo("0786833903");
        user.setNic("199932310971");

        user.setStatus(Status.active);
        user.setCreatedUser("test");
        user.setLastUpdatedUser("test");
        user.setCreatedTime(new Date());
        user.setLastUpdatedTime(new Date());
        user.setLastLoggedDate(new Date());
        user.setPasswordExpireDate(new Date());

        SystemUser savedUser = userRepository.save(user);
        System.out.println(savedUser);
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    @Order(2)
    public void getAdminUserById(){
        SystemUser systemUser = userRepository.findById(10L).get();
        Assertions.assertThat(systemUser.getId()).isEqualTo(10L);
    }

    @Test
    @Order(3)
    public void getUserList(){
        List<SystemUser> systemUserList = userRepository.findAll();
        Assertions.assertThat(systemUserList.size()).isGreaterThan(0);
    }

    @Test
    @Order(4)
    @Rollback(value = false)
    public void updateUser(){
        SystemUser systemUser = userRepository.findById(10L).get();
        systemUser.setMobileNo("0784568222");
        SystemUser updatedUser =  userRepository.save(systemUser);
        Assertions.assertThat(updatedUser.getMobileNo()).isEqualTo("0784568222");
    }


}
