package com.careerbooker.server.service.impl;

import com.careerbooker.server.dto.request.RegistrationDTO;
import com.careerbooker.server.dto.response.LoginResponseDTO;
import com.careerbooker.server.entity.SystemUser;
import com.careerbooker.server.mapper.ResponseGenerator;
import com.careerbooker.server.repository.UserRepository;
import com.careerbooker.server.util.MessageConstant;
import com.careerbooker.server.util.ResponseCode;
import com.careerbooker.server.util.enums.Status;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationServiceImpl {

    private UserRepository userRepository;

    private AuthenticationManager authenticationManager;

    private TokenServiceImpl tokenServiceImpl;

    private ResponseGenerator responseGenerator;


    public ResponseEntity<Object> loginUser(RegistrationDTO registrationDTO, Locale locale) {
        try {
            SystemUser systemUser = Optional.ofNullable(userRepository.findByUsernameAndStatus(registrationDTO.getUsername(), Status.active)).orElse(null);

            if (Objects.isNull(systemUser)) {
                return responseGenerator.generateErrorResponse(registrationDTO, HttpStatus.NOT_FOUND,
                        ResponseCode.USER_GET_SUCCESS, MessageConstant.USER_NOT_FOUND, new Object[]{registrationDTO.getUsername()}, locale);
            }

            UsernamePasswordAuthenticationToken us = new UsernamePasswordAuthenticationToken(registrationDTO.getUsername(), registrationDTO.getPassword());
            Authentication authentication = authenticationManager.authenticate(us);

            String token = tokenServiceImpl.generateJwtToken(authentication);

            LoginResponseDTO loginResponseDTO = new LoginResponseDTO(systemUser, token);
            return responseGenerator.generateSuccessResponse(registrationDTO, HttpStatus.OK, ResponseCode.USER_GET_SUCCESS, MessageConstant.SUCCESSFULLY_GET, locale, loginResponseDTO);
        } catch (BadCredentialsException e) {
            // Handle incorrect password exception here
            log.error("Incorrect password for user: " + registrationDTO.getUsername());
            return responseGenerator.generateErrorResponse(registrationDTO, HttpStatus.UNAUTHORIZED,
                    ResponseCode.PASSWORD_INCORRECT, MessageConstant.PASSWORD_INCORRECT, null, locale);
        } catch (AuthenticationException e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
