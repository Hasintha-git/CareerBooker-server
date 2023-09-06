package com.careerbooker.server.controller;

import com.careerbooker.server.dto.request.TimeSlotDTO;
import com.careerbooker.server.service.ConsultantDaysService;
import com.careerbooker.server.util.EndPoint;
import com.careerbooker.server.validators.*;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/consultant-days")
@Log4j2
@CrossOrigin
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ConsultantDaysController {

    private ConsultantDaysService consultantDaysService;


    @PostMapping(value = {EndPoint.CONSULTANT_DAYS_REQUEST_MGT}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> addConsultantDays(@Validated({InsertValidation.class})
            @RequestBody TimeSlotDTO timeSlotDTO, Locale locale) throws Exception {
        log.debug("Received Consultant Days add List Request {} -", timeSlotDTO);
        return consultantDaysService.saveTimeSlot(timeSlotDTO, locale);
    }

    @PostMapping(value = {EndPoint.CONSULTANT_DAYS_REQUEST_FIND_ID}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> findConsultantDays(@RequestBody TimeSlotDTO timeSlotDTO,
                                                     Locale locale) throws Exception {
        log.debug("Received Consultant Days find Id List Request {} -", timeSlotDTO);
        return consultantDaysService.findTimeSlotById(timeSlotDTO, locale);
    }

    @PostMapping(value = {EndPoint.CONSULTANT_DAYS_REQUEST_FIND_CON}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> findConsultantDaysByCon(@RequestBody TimeSlotDTO timeSlotDTO, Locale locale) throws Exception {
        log.debug("Received Consultant Days find Con List Request {} -", timeSlotDTO);
        return consultantDaysService.findTimeSlotByCon(timeSlotDTO, locale);
    }

    @PostMapping(value = {EndPoint.CONSULTANT_DAYS_REQUEST_FIND_CON_DAY}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> findConsultantDaysByConDay(@RequestBody TimeSlotDTO timeSlotDTO, Locale locale) throws Exception {
        log.debug("Received Consultant Days find Con and Day List Request {} -", timeSlotDTO);
        return consultantDaysService.findTimeSlotByConAndDay(timeSlotDTO, locale);
    }
}
