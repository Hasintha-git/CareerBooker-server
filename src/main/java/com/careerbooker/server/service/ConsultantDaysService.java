package com.careerbooker.server.service;

import com.careerbooker.server.dto.request.TimeSlotDTO;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

public interface ConsultantDaysService {

    Object getReferenceData();
    ResponseEntity<Object> getTimeSlotFilterList(TimeSlotDTO timeSlotDTO, Locale locale) throws Exception;

    ResponseEntity<Object> findTimeSlotById(TimeSlotDTO timeSlotDTO, Locale locale) throws Exception;
    ResponseEntity<Object> findTimeSlotByCon(TimeSlotDTO timeSlotDTO, Locale locale) throws Exception;
    ResponseEntity<Object> findTimeSlotByConAndDay(TimeSlotDTO timeSlotDTO, Locale locale) throws Exception;

    ResponseEntity<Object> saveTimeSlot(TimeSlotDTO timeSlotDTO, Locale locale) throws Exception;

    ResponseEntity<Object> editTimeSlot(TimeSlotDTO timeSlotDTO, Locale locale);

    ResponseEntity<Object> deleteTimeSlot(TimeSlotDTO timeSlotDTO, Locale locale);
}
