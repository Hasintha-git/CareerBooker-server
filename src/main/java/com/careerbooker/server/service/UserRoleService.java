package com.careerbooker.server.service;

import com.careerbooker.server.dto.request.SpecializationDTO;
import com.careerbooker.server.dto.request.UserRoleDTO;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

public interface UserRoleService {

    Object getReferenceData();
    ResponseEntity<Object> getUserRoleFilterList(UserRoleDTO userRoleDTO, Locale locale) throws Exception;

    ResponseEntity<Object> findUserRoleById(UserRoleDTO userRoleDTO, Locale locale) throws Exception;

    ResponseEntity<Object> saveUserRole(UserRoleDTO userRoleDTO, Locale locale) throws Exception;

    ResponseEntity<Object> editUserRole(UserRoleDTO userRoleDTO, Locale locale);

    ResponseEntity<Object> deleteUserRole(UserRoleDTO userRoleDTO, Locale locale);
}
