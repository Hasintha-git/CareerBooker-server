package com.careerbooker.server.service;

import com.careerbooker.server.dto.request.UserRequestDTO;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

public interface UserService {

    Object getReferenceData();
    ResponseEntity<Object> getUserFilterList(UserRequestDTO userRequestDTO, Locale locale) throws Exception;

    ResponseEntity<Object> findUserById(UserRequestDTO userRequestDTO, Locale locale) throws Exception;

    ResponseEntity<Object> saveUser(UserRequestDTO userRequestDTO, Locale locale) throws Exception;

    ResponseEntity<Object> editUser(UserRequestDTO userRequestDTO, Locale locale);

    ResponseEntity<Object> deleteUser(UserRequestDTO userRequestDTO, Locale locale);
}
