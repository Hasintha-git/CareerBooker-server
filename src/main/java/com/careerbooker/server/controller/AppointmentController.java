package com.careerbooker.server.controller;

import com.careerbooker.server.dto.request.AppointmentDTO;
import com.careerbooker.server.dto.request.AppointmentSearchDTO;
import com.careerbooker.server.service.AppointmentService;
import com.careerbooker.server.util.EndPoint;
import com.careerbooker.server.validators.DeleteValidation;
import com.careerbooker.server.validators.FindValidation;
import com.careerbooker.server.validators.InsertValidation;
import com.careerbooker.server.validators.UpdateValidation;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Locale;
import java.util.Objects;

@RestController
@RequestMapping("/appointment")
@Log4j2
@CrossOrigin
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AppointmentController {

    private AppointmentService appointmentService;

    @GetMapping(value = EndPoint.APPOINTMENT_REQUEST_SEARCH_DATA)
    public ResponseEntity<Object> getReferenceData() {
        log.info("Received Appointment Search Reference Data Request {} -");
        return ResponseEntity.status(HttpStatus.OK).body(appointmentService.getReferenceData());
    }
    @GetMapping(value = {EndPoint.APPOINTMENT_REQUEST_FILTER_LIST}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> getAppointmentFilteredList(
            @RequestParam(required = false) Integer start,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Date date,
            @RequestParam(required = false) String consultantName,
            @RequestParam(required = false) Long spe_id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String slotId,
            @RequestParam(required = false) Boolean full,
            @RequestParam(required = false) Boolean dataTable,
            @RequestParam(required = false) Integer draw, Locale locale) throws Exception {

        AppointmentDTO appointmentDTO = new AppointmentDTO();
        AppointmentSearchDTO appointmentSearchDTO= new AppointmentSearchDTO();
        appointmentSearchDTO.setConsultantName(consultantName);
        appointmentSearchDTO.setSpecialization(spe_id);
        appointmentSearchDTO.setBookedDate(date);
        appointmentSearchDTO.setUserName(username);
        appointmentSearchDTO.setSlotId(slotId);
        appointmentSearchDTO.setStatus(status);

        appointmentDTO.setAppointmentSearchDTO(appointmentSearchDTO);

        appointmentDTO.setPageNumber(start);
        appointmentDTO.setPageSize(limit);
        appointmentDTO.setSortColumn(sortBy);
        if (Objects.nonNull(order) && !order.isEmpty()) {
            appointmentDTO.setSortDirection(order.toUpperCase());
        }
        log.info("Received Appointment get Filtered List Request {} -", appointmentDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(appointmentService.getAppointmentFilterList(appointmentDTO, locale));
    }

    @PostMapping(value = {EndPoint.CONSULTANT_REQUEST_FIND_ID}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> findAppointment(@Validated({ FindValidation.class})
            @RequestBody AppointmentDTO appointmentDTO, Locale locale) throws Exception {
        log.info("Received Appointment find List Request {} -", appointmentDTO);
        return appointmentService.findAppointmentById(appointmentDTO, locale);
    }

    @PostMapping(value = {EndPoint.APPOINTMENT_CREATE_REQUEST_MGT}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> addAppointment( @Validated({InsertValidation.class})
            @RequestBody AppointmentDTO appointmentDTO, Locale locale) throws Exception {
        log.info("Received Appointment add List Request {} -", appointmentDTO);
        return appointmentService.saveAppointment(appointmentDTO, locale);
    }

    @PutMapping(value = {EndPoint.APPOINTMENT_REQUEST_MGT}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> updateAppointment(@Validated({ UpdateValidation.class})
            @RequestBody AppointmentDTO appointmentDTO, Locale locale) throws Exception {
        log.info("Received Appointment update List Request {} -", appointmentDTO);
        return appointmentService.editAppointment(appointmentDTO, locale);
    }

    @DeleteMapping(value = {EndPoint.APPOINTMENT_REQUEST_MGT}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> deleteAppointment(@Validated({ DeleteValidation.class})
            @RequestBody AppointmentDTO appointmentDTO, Locale locale) throws Exception {
        log.info("Received Consultant delete List Request {} -", appointmentDTO);
        return appointmentService.deleteAppointment(appointmentDTO, locale);
    }

    @PostMapping(value = EndPoint.CONSULTANT_SPE_REQUEST_MGT + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> findConsultantBySpecialization(
            @PathVariable Long id, Locale locale
    ) throws Exception {
        log.info("Received Appointment find List Request {} -", id);
        return appointmentService.findConsultantBySpeId(id, locale);
    }
}
