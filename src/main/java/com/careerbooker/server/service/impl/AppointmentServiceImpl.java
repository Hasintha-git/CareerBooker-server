package com.careerbooker.server.service.impl;

import com.careerbooker.server.dto.request.AppointmentDTO;
import com.careerbooker.server.dto.request.TimeSlotDTO;
import com.careerbooker.server.entity.*;
import com.careerbooker.server.mapper.DtoToEntityMapper;
import com.careerbooker.server.mapper.ResponseGenerator;
import com.careerbooker.server.repository.*;
import com.careerbooker.server.service.AppointmentService;
import com.careerbooker.server.util.MessageConstant;
import com.careerbooker.server.util.ResponseCode;
import com.careerbooker.server.util.enums.Status;
import com.careerbooker.server.util.enums.TimeSlot;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Service
@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AppointmentServiceImpl implements AppointmentService {

    private AppointmentRepository appointmentRepository;
    private ConsultantDaysRepository consultantDaysRepository;
    private ConsultantRepository consultantRepository;
    private UserRepository userRepository;
    private DayRepository dayRepository;
    private ResponseGenerator responseGenerator;

    @Override
    public Object getReferenceData() {
        return null;
    }

    @Override
    public ResponseEntity<Object> getAppointmentFilterList(AppointmentDTO appointmentDTO, Locale locale) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<Object> findAppointmentById(AppointmentDTO appointmentDTO, Locale locale) throws Exception {
        return null;
    }

    @Override
    @Transactional
    public ResponseEntity<Object> saveAppointment(AppointmentDTO appointmentDTO, Locale locale) throws Exception {
        try {

            SystemUser systemUser = Optional.ofNullable(userRepository.findByIdAndStatusNot(appointmentDTO.getUserId(), Status.deleted))
                                    .orElseThrow(() -> new EntityNotFoundException(MessageConstant.USER_NOT_FOUND));

            Consultants consultants = Optional.ofNullable(consultantRepository
                            .findByConsultantIdAndStatusNot(appointmentDTO.getConsultantId(), Status.deleted))
                            .orElseThrow(() -> new EntityNotFoundException(MessageConstant.CONSULTANT_NOT_FOUND));
            System.out.println(systemUser);
            System.out.println(consultants);
            System.out.println(appointmentDTO.getBookedDate());
            Days days = Optional.ofNullable(dayRepository
                            .findByDate(appointmentDTO.getBookedDate()))
                    .orElseThrow(() -> new EntityNotFoundException(MessageConstant.CONSULTANT_DAYS_UNAVAILABLE));


            System.out.println(days);
            System.out.println(TimeSlot.getTimeSlot(appointmentDTO.getSlotId()));

            ConsultantDays consultantDays = consultantDaysRepository.findByConsultantAndDaysAndSlotAndStatus(consultants, days, TimeSlot.getTimeSlot(appointmentDTO.getSlotId()), Status.active);

            log.info(consultantDays);

            if (Objects.isNull(consultantDays)) {
                return responseGenerator.generateErrorResponse(appointmentDTO, HttpStatus.CONFLICT,
                        ResponseCode.NOT_FOUND, MessageConstant.CONSULTANT_UNAVAILABLE, new
                                Object[]{appointmentDTO.getConsultantId()}, locale);
            }

            Appointments appointments = DtoToEntityMapper.mapAppoinment(consultantDays, systemUser);
            appointments.setStatus(Status.pending);
            appointments.setCreatedUser(appointmentDTO.getActiveUserName());
            appointments.setModifiedUser(appointmentDTO.getActiveUserName());
            appointments.setCreatedDate(new Date());
            appointments.setModifiedDate(new Date());

            appointmentRepository.save(appointments);

            consultantDays.setStatus(Status.booked);
            consultantDaysRepository.save(consultantDays);

            return responseGenerator
                    .generateSuccessResponse(appointmentDTO, HttpStatus.OK, ResponseCode.APPOINTMENT_SAVED_SUCCESS,
                            MessageConstant.APPOINTMENT_SUCCESSFULLY_SAVE, locale, appointmentDTO);

        }catch (EntityNotFoundException en) {
            log.error(en.getMessage());
            return responseGenerator.generateErrorResponse(appointmentDTO, HttpStatus.NOT_FOUND,
                    ResponseCode.NOT_FOUND, en.getMessage(), locale);
        }catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<Object> editAppointment(AppointmentDTO appointmentDTO, Locale locale) {
        return null;
    }

    @Override
    public ResponseEntity<Object> deleteAppointment(AppointmentDTO appointmentDTO, Locale locale) {
        return null;
    }
}
