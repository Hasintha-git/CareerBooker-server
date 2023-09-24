package com.careerbooker.server.controller;

import com.careerbooker.server.dto.request.UserRoleDTO;
import com.careerbooker.server.service.UserRoleService;
import com.careerbooker.server.util.EndPoint;
import com.careerbooker.server.validators.*;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/userrole")
@Log4j2
@CrossOrigin
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserRoleController {

    private UserRoleService userRoleService;

    @GetMapping(value = EndPoint.USERROLE_REQUEST_SEARCH_DATA)
    public ResponseEntity<Object> getReferenceData() {
        log.debug("Received User Role Search Reference Data Request {} -");
        return ResponseEntity.status(HttpStatus.OK).body(userRoleService.getReferenceData());
    }
    @PostMapping(value = {EndPoint.USERROLE_REQUEST_FILTER_LIST}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> getUserRoleFilteredList(@Validated({GetValidation.class})
            @RequestBody UserRoleDTO userRoleDTO, Locale locale) throws Exception {
        log.debug("Received User Role get Filtered List Request {} -", userRoleDTO);
        return userRoleService.getUserRoleFilterList(userRoleDTO, locale);
    }

    @PostMapping(value = {EndPoint.USERROLE_REQUEST_FIND_ID}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> findUserRole(@Validated({ FindValidation.class})
            @RequestBody UserRoleDTO userRoleDTO, Locale locale) throws Exception {
        log.debug("Received User Role find List Request {} -", userRoleDTO);
        return userRoleService.findUserRoleById(userRoleDTO, locale);
    }

    @PostMapping(value = {EndPoint.USERROLE_REQUEST_MGT}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> addUserRole( @Validated({InsertValidation.class})
            @RequestBody UserRoleDTO userRoleDTO, Locale locale) throws Exception {
        log.debug("Received User Role add List Request {} -", userRoleDTO);
        return userRoleService.saveUserRole(userRoleDTO, locale);
    }

    @PutMapping(value = {EndPoint.USERROLE_REQUEST_MGT}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> updateUserRole(@Validated({ UpdateValidation.class})
            @RequestBody UserRoleDTO userRoleDTO, Locale locale) throws Exception {
        log.debug("Received User Role update List Request {} -", userRoleDTO);
        return userRoleService.editUserRole(userRoleDTO, locale);
    }

    @DeleteMapping(value = {EndPoint.USERROLE_REQUEST_MGT}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> deleteUserRole(@Validated({ DeleteValidation.class})
            @RequestBody UserRoleDTO userRoleDTO, Locale locale) throws Exception {
        log.debug("Received User Role delete List Request {} -", userRoleDTO);
        return userRoleService.deleteUserRole(userRoleDTO, locale);
    }
}
