package com.careerbooker.server;

import com.careerbooker.server.entity.SystemUser;
import com.careerbooker.server.entity.SystemUser_;
import com.careerbooker.server.entity.UserRole;
import com.careerbooker.server.repository.UserRepository;
import com.careerbooker.server.repository.UserRoleRepository;
import com.careerbooker.server.util.enums.Status;
import org.assertj.core.api.Assertions;
import org.h2.engine.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserRoleRepository userRoleRepository;
    private PasswordEncoder passwordEncoder;
    @Test
    @Order(1)
    @Rollback(value = false)
    public void saveAdminUser(){

        UserRole userRole = userRoleRepository.findById(1L).get();

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
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    @Order(2)
    public void getAdminUserById(){
        SystemUser systemUser = userRepository.findById(1L).get();
        Assertions.assertThat(systemUser.getId()).isEqualTo(1L);
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
        SystemUser systemUser = userRepository.findById(1L).get();
        systemUser.setMobileNo("0784568222");
        SystemUser updatedUser =  userRepository.save(systemUser);
        Assertions.assertThat(updatedUser.getMobileNo()).isEqualTo("0784568222");
    }

    @Test
    @Order(5)
    @Rollback(value = false)
    public void deleteUser(){
        SystemUser systemUser = userRepository.findById(1L).get();
        systemUser.setStatus(Status.deleted);
        SystemUser updatedUser =  userRepository.save(systemUser);
        Assertions.assertThat(updatedUser.getStatus()).isEqualTo(Status.deleted);
    }
}
