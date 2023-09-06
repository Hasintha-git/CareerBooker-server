package com.careerbooker.server.controller;

import com.careerbooker.server.dto.request.UserRequestDTO;
import com.careerbooker.server.dto.request.UserRequestSearchDTO;
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
import java.util.Objects;

@RestController
@RequestMapping("/user")
@Log4j2
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private UserService userService;

    @GetMapping(value = EndPoint.USER_REQUEST_SEARCH_DATA)
    public ResponseEntity<Object> getReferenceData() {
        log.debug("Received User Search Reference Data Request {} -");
        return ResponseEntity.status(HttpStatus.OK).body(userService.getReferenceData());
    }
    @GetMapping(value = {EndPoint.USER_REQUEST_FILTER_LIST}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> getUserFilteredList(
            @RequestParam(required = false) Integer start,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String nic,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String userRole,
            @RequestParam(required = false) String mobileNo,
            @RequestParam(required = false) Boolean full,
            @RequestParam(required = false) Boolean dataTable,
            @RequestParam(required = false) Integer draw, Locale locale) throws Exception {

        UserRequestDTO userRequestDTO = new UserRequestDTO();
        UserRequestSearchDTO userRequestSearchDTO= new UserRequestSearchDTO();
        userRequestSearchDTO.setUserName(username);
        userRequestSearchDTO.setNic(nic);
        userRequestSearchDTO.setEmail(email);
        userRequestSearchDTO.setUserRole(userRole);
        userRequestSearchDTO.setMobileNo(mobileNo);
        userRequestSearchDTO.setStatus(status);

        userRequestDTO.setUserRequestSearchDTO(userRequestSearchDTO);

        userRequestDTO.setPageNumber(start);
        userRequestDTO.setPageSize(limit);
        userRequestDTO.setSortColumn(sortBy);
        if (Objects.nonNull(order) && !order.isEmpty()) {
            userRequestDTO.setSortDirection(order.toUpperCase());
        }
        log.debug("Received User get Filtered List Request {} -", userRequestDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getUserFilterList(userRequestDTO, locale));
    }

    @PostMapping(value = {EndPoint.USER_REQUEST_FIND_ID}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> findUserById(@Validated({ FindValidation.class})
            @RequestBody UserRequestDTO userRequestDTO, Locale locale) throws Exception {
        log.debug("Received User find List Request {} -", userRequestDTO);
        return userService.findUserById(userRequestDTO, locale);
    }

    @PostMapping(value = {EndPoint.USER_REQUEST_FIND_NIC}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> findUserByNic(
                                   @RequestBody UserRequestDTO userRequestDTO, Locale locale) throws Exception {
        log.debug("Received User find Nic Request {} -", userRequestDTO);
        return userService.findUserByNic(userRequestDTO, locale);
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
