package com.careerbooker.server.controller;

import com.careerbooker.server.dto.request.UserRequestDTO;
import com.careerbooker.server.service.UserService;
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
@RequestMapping("/user")
@Log4j2
@CrossOrigin
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private UserService userService;

    @GetMapping(value = EndPoint.USER_REQUEST_SEARCH_DATA)
    public ResponseEntity<Object> getReferenceData() {
        log.debug("Received User Search Reference Data Request {} -");
        return ResponseEntity.status(HttpStatus.OK).body(userService.getReferenceData());
    }
    @PostMapping(value = {EndPoint.USER_REQUEST_FILTER_LIST}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> getUserFilteredList(@Validated({GetValidation.class})
            @RequestBody UserRequestDTO userRequestDTO, Locale locale) throws Exception {
        log.debug("Received User get Filtered List Request {} -", userRequestDTO);
        return userService.getUserFilterList(userRequestDTO, locale);
    }

    @PostMapping(value = {EndPoint.USER_REQUEST_FIND_ID}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> findUser(@Validated({ FindValidation.class})
            @RequestBody UserRequestDTO userRequestDTO, Locale locale) throws Exception {
        log.debug("Received User find List Request {} -", userRequestDTO);
        return userService.findUserById(userRequestDTO, locale);
    }

    @PostMapping(value = {EndPoint.USER_REQUEST_MGT}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> addUser( @Validated({InsertValidation.class})
            @RequestBody UserRequestDTO userRequestDTO, Locale locale) throws Exception {
        log.debug("Received User add List Request {} -", userRequestDTO);
        return userService.saveUser(userRequestDTO, locale);
    }

    @PutMapping(value = {EndPoint.USER_REQUEST_MGT}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> updateUser(@Validated({ UpdateValidation.class})
            @RequestBody UserRequestDTO userRequestDTO, Locale locale) throws Exception {
        log.debug("Received User update List Request {} -", userRequestDTO);
        return userService.editUser(userRequestDTO, locale);
    }

    @DeleteMapping(value = {EndPoint.USER_REQUEST_MGT}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> deleteUser(@Validated({ DeleteValidation.class})
            @RequestBody UserRequestDTO userRequestDTO, Locale locale) throws Exception {
        log.debug("Received User delete List Request {} -", userRequestDTO);
        return userService.deleteUser(userRequestDTO, locale);
    }
}
