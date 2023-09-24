package com.careerbooker.server.service;

import com.careerbooker.server.dto.request.SpecializationDTO;
import com.careerbooker.server.dto.request.UserRequestDTO;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

public interface SpecializationService {

    Object getReferenceData();
    Object getSpecializationFilterList(SpecializationDTO specializationDTO, Locale locale) throws Exception;

    ResponseEntity<Object> findSpecializationById(SpecializationDTO specializationDTO, Locale locale) throws Exception;

    ResponseEntity<Object> saveSpecialization(SpecializationDTO specializationDTO, Locale locale) throws Exception;

    ResponseEntity<Object> editSpecialization(SpecializationDTO specializationDTO, Locale locale);

    ResponseEntity<Object> deleteSpecialization(SpecializationDTO specializationDTO, Locale locale);
}
