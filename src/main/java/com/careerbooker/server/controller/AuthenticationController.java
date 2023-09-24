package com.careerbooker.server.controller;

import com.careerbooker.server.dto.request.RegistrationDTO;
import com.careerbooker.server.dto.request.UserRequestDTO;
import com.careerbooker.server.service.UserService;
import com.careerbooker.server.service.impl.AuthenticationServiceImpl;
import com.careerbooker.server.validators.InsertValidation;
import com.careerbooker.server.validators.LoginValidation;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationController {

    private AuthenticationServiceImpl authenticationServiceImpl;

    private UserService userService;

    @PostMapping(value = {"/register"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin("*")
    public ResponseEntity<Object> registerUser(@Validated({InsertValidation.class}) @RequestBody UserRequestDTO userRequestDTO, Locale locale) throws Exception {
        log.debug("Received Auth Register Request {} -", userRequestDTO);
        return userService.saveUser(userRequestDTO,locale);
    }

    @PostMapping(value = {"/login"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin("*")
    public ResponseEntity<Object> loginUser(@Validated({LoginValidation.class}) @RequestBody RegistrationDTO registrationDTO, Locale locale) {
        log.debug("Received Auth Login Request {} -", registrationDTO);
        return authenticationServiceImpl.loginUser(registrationDTO,locale);
    }

}
