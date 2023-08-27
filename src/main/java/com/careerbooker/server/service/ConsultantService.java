package com.careerbooker.server.service;

import com.careerbooker.server.dto.request.ConsultantsDTO;
import com.careerbooker.server.dto.request.SpecializationDTO;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

public interface ConsultantService {

    Object getReferenceData();
    ResponseEntity<Object> getConsultantFilterList(ConsultantsDTO consultantsDTO, Locale locale) throws Exception;

    ResponseEntity<Object> findConsultantById(ConsultantsDTO consultantsDTO, Locale locale) throws Exception;

    ResponseEntity<Object> saveConsultant(ConsultantsDTO consultantsDTO, Locale locale) throws Exception;

    ResponseEntity<Object> editConsultant(ConsultantsDTO consultantsDTO, Locale locale);

    ResponseEntity<Object> deleteConsultant(ConsultantsDTO consultantsDTO, Locale locale);
}
