package com.careerbooker.server.service;

import com.careerbooker.server.dto.request.AppointmentDTO;
import com.careerbooker.server.dto.request.TimeSlotDTO;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

public interface AppointmentService {

    Object getReferenceData();
    ResponseEntity<Object> getAppointmentFilterList(AppointmentDTO appointmentDTO, Locale locale) throws Exception;

    ResponseEntity<Object> findAppointmentById(AppointmentDTO appointmentDTO, Locale locale) throws Exception;

    ResponseEntity<Object> saveAppointment(AppointmentDTO appointmentDTO, Locale locale) throws Exception;

    ResponseEntity<Object> editAppointment(AppointmentDTO appointmentDTO, Locale locale);

    ResponseEntity<Object> deleteAppointment(AppointmentDTO appointmentDTO, Locale locale);
}
