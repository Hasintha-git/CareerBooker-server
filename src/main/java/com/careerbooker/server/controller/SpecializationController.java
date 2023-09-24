package com.careerbooker.server.controller;

import com.careerbooker.server.dto.request.SpecializationDTO;
import com.careerbooker.server.dto.request.SpecializationSearchDTO;
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
@RequestMapping("/specialization")
@Log4j2
@CrossOrigin
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SpecializationController {

    private SpecializationService specializationService;

    @GetMapping(value = EndPoint.SPECIALIZATION_REQUEST_SEARCH_DATA)
    public ResponseEntity<Object> getReferenceData() {
        log.debug("Received Specialization Search Reference Data Request {} -");
        return ResponseEntity.status(HttpStatus.OK).body(specializationService.getReferenceData());
    }
    @GetMapping(value = {EndPoint.SPECIALIZATION_REQUEST_FILTER_LIST}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public Object getSpecializationFilteredList(
            @RequestParam(required = false) Integer start,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean full,
            @RequestParam(required = false) Boolean dataTable,
            @RequestParam(required = false) Integer draw, Locale locale) throws Exception {

        SpecializationDTO specializationDTO = new SpecializationDTO();
        SpecializationSearchDTO specializationSearchDTO= new SpecializationSearchDTO();
        specializationSearchDTO.setName(name);
        specializationSearchDTO.setStatusCode(status);
        specializationDTO.setSpecializationSearchDTO(specializationSearchDTO);
        specializationDTO.setPageNumber(start);
        specializationDTO.setPageSize(limit);
        specializationDTO.setSortColumn(sortBy);
        if (Objects.nonNull(order) && !order.isEmpty()) {
            specializationDTO.setSortDirection(order.toUpperCase());
        }
        log.debug("Received Specialization get Filtered List Request {} -", specializationDTO);
        return specializationService.getSpecializationFilterList(specializationDTO, locale);
    }

    @PostMapping(value = {EndPoint.SPECIALIZATION_REQUEST_FIND_ID}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> findSpecialization(@Validated({ FindValidation.class})
            @RequestBody SpecializationDTO specializationDTO, Locale locale) throws Exception {
        log.debug("Received Specialization find List Request {} -", specializationDTO);
        return specializationService.findSpecializationById(specializationDTO, locale);
    }

    @PostMapping(value = {EndPoint.SPECIALIZATION_REQUEST_MGT}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> addSpecialization( @Validated({InsertValidation.class})
            @RequestBody SpecializationDTO specializationDTO, Locale locale) throws Exception {
        log.debug("Received Specialization add List Request {} -", specializationDTO);
        return specializationService.saveSpecialization(specializationDTO, locale);
    }

    @PutMapping(value = {EndPoint.SPECIALIZATION_REQUEST_MGT}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> updateSpecialization(@Validated({ UpdateValidation.class})
            @RequestBody SpecializationDTO specializationDTO, Locale locale) throws Exception {
        log.debug("Received Specialization update List Request {} -", specializationDTO);
        return specializationService.editSpecialization(specializationDTO, locale);
    }

    @DeleteMapping(value = {EndPoint.SPECIALIZATION_REQUEST_MGT+ "/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> deleteSpecialization(@Validated({ DeleteValidation.class})
                                                           @PathVariable Long id, Locale locale) throws Exception {
        log.debug("Received Specialization delete List Request {} -", id);
        SpecializationDTO specializationDTO= new SpecializationDTO();
        specializationDTO.setId(id);
        return specializationService.deleteSpecialization(specializationDTO, locale);
    }
}
