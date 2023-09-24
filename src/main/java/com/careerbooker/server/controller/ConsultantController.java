package com.careerbooker.server.controller;

import com.careerbooker.server.dto.request.*;
import com.careerbooker.server.service.ConsultantService;
import com.careerbooker.server.service.SpecializationService;
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
@RequestMapping("/consultant")
@Log4j2
@CrossOrigin
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ConsultantController {

    private ConsultantService consultantService;

    @GetMapping(value = EndPoint.CONSULTANT_REQUEST_SEARCH_DATA)
    public ResponseEntity<Object> getReferenceData() {
        log.info("Received Consultant Search Reference Data Request {} -");
        return ResponseEntity.status(HttpStatus.OK).body(consultantService.getReferenceData());
    }
    @GetMapping(value = {EndPoint.CONSULTANT_REQUEST_FILTER_LIST}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> getConsultantFilteredList(
            @RequestParam(required = false) Integer start,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String nic,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Long spe_id,
            @RequestParam(required = false) Boolean full,
            @RequestParam(required = false) Boolean dataTable,
            @RequestParam(required = false) Integer draw, Locale locale) throws Exception {

        ConsultantsDTO consultantsDTO = new ConsultantsDTO();
        ConsultantSearchDTO consultantSearchDTO= new ConsultantSearchDTO();
        consultantSearchDTO.setUsername(username);
        consultantSearchDTO.setNic(nic);
        consultantSearchDTO.setSpe_id(spe_id);
        consultantSearchDTO.setStatus(status);

        consultantsDTO.setConsultantSearchDTO(consultantSearchDTO);

        consultantsDTO.setPageNumber(start);
        consultantsDTO.setPageSize(limit);
        consultantsDTO.setSortColumn(sortBy);
        if (Objects.nonNull(order) && !order.isEmpty()) {
            consultantsDTO.setSortDirection(order.toUpperCase());
        }
        log.info("Received Consultant get Filtered List Request {} -", consultantsDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(consultantService.getConsultantFilterList(consultantsDTO, locale));
    }

    @PostMapping(value = {EndPoint.CONSULTANT_REQUEST_FIND_ID}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> findConsultant(@Validated({ FindValidation.class})
            @RequestBody ConsultantsDTO consultantsDTO, Locale locale) throws Exception {
        log.info("Received Consultant find List Request {} -", consultantsDTO);
        return consultantService.findConsultantById(consultantsDTO, locale);
    }

    @PostMapping(value = {EndPoint.CONSULTANT_REQUEST_MGT}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> addConsultant( @Validated({InsertValidation.class})
            @RequestBody ConsultantsDTO consultantsDTO, Locale locale) throws Exception {
        log.info("Received Consultant add List Request {} -", consultantsDTO);
        return consultantService.saveConsultant(consultantsDTO, locale);
    }

    @PutMapping(value = {EndPoint.CONSULTANT_REQUEST_MGT}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> updateConsultant(@Validated({ UpdateValidation.class})
            @RequestBody ConsultantsDTO consultantsDTO, Locale locale) throws Exception {
        log.info("Received Consultant update List Request {} -", consultantsDTO);
        return consultantService.editConsultant(consultantsDTO, locale);
    }

    @DeleteMapping(value = {EndPoint.CONSULTANT_REQUEST_MGT+ "/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> deleteConsultant(@Validated({ DeleteValidation.class})
                                                       @PathVariable Long id, Locale locale) throws Exception {
        log.info("Received Consultant delete List Request {} -", id);
        ConsultantsDTO consultantsDTO = new ConsultantsDTO();
        consultantsDTO.setCon_id(id);
        return consultantService.deleteConsultant(consultantsDTO, locale);
    }
}
