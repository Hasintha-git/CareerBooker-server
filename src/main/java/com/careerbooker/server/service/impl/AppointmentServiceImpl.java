package com.careerbooker.server.service.impl;

import com.careerbooker.server.dto.SimpleBaseDTO;
import com.careerbooker.server.dto.request.AppointmentDTO;
import com.careerbooker.server.dto.request.ConsultantsDTO;
import com.careerbooker.server.dto.request.TimeSlotDTO;
import com.careerbooker.server.dto.response.ConsultantResponseDTO;
import com.careerbooker.server.entity.*;
import com.careerbooker.server.mapper.DtoToEntityMapper;
import com.careerbooker.server.mapper.EntityToDtoMapper;
import com.careerbooker.server.mapper.ResponseGenerator;
import com.careerbooker.server.repository.*;
import com.careerbooker.server.service.AppointmentService;
import com.careerbooker.server.util.MessageConstant;
import com.careerbooker.server.util.ResponseCode;
import com.careerbooker.server.util.enums.ClientStatusEnum;
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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private SpecializationRepository specializationRepository;

    @Override
    @Transactional
    public Object getReferenceData() {
        try {
            Map<String, Object> refData = new HashMap<>();

            //get status
            List<SimpleBaseDTO> defaultStatus = Stream.of(ClientStatusEnum.values()).map(statusEnum -> new SimpleBaseDTO(statusEnum.getCode(), statusEnum.getDescription())).collect(Collectors.toList());
            //get user role list
            List<SpecializationType> data = specializationRepository.findAllByStatus(Status.active);
            List<SimpleBaseDTO> simpleBaseDTOList = data.stream().map(specialization -> {
                SimpleBaseDTO simpleBaseDTO = new SimpleBaseDTO();
                return EntityToDtoMapper.mapSpecializationDropdown(simpleBaseDTO,specialization);
            }).collect(Collectors.toList());

            List<SimpleBaseDTO> timeSlotList = Stream.of(TimeSlot.values()).map(statusEnum -> new SimpleBaseDTO(statusEnum.getCode(), statusEnum.getDescription())).collect(Collectors.toList());


            //set data
            refData.put("statusList", defaultStatus);
            refData.put("specializationList", simpleBaseDTOList);
            refData.put("timeSlotList", timeSlotList);

            return refData;

        } catch (Exception e) {
            log.error("Exception : ", e);
            throw e;
        }
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

            System.out.println(">>>>>>>>>>>**********");
            System.out.println("======"+consultants);
            System.out.println("======"+days);
            System.out.println("======"+TimeSlot.getTimeSlot(appointmentDTO.getSlotId()));
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

    @Override
    @Transactional
    public ResponseEntity<Object> findConsultantBySpeId(Long speId, Locale locale) throws Exception {
        try {
            Consultants consultants = Optional.ofNullable(consultantRepository.findBySpecializations_SpecializationIdAndStatusNot(speId, Status.deleted)).orElse(
                    null
            );

            if (Objects.isNull(consultants)) {

                System.out.println(">"+speId+">>"+HttpStatus.NOT_FOUND+">>>"+ ResponseCode.NOT_FOUND+">>>>"+MessageConstant.CONSULTANT_NOT_FOUND);
                return responseGenerator.generateErrorResponse(null, HttpStatus.NOT_FOUND,
                        ResponseCode.NOT_FOUND, MessageConstant.CONSULTANT_NOT_FOUND, new
                                Object[]{speId},locale);
            }

            ConsultantResponseDTO consultantResponseDTO = EntityToDtoMapper.mapConsultant(consultants);
            return responseGenerator
                    .generateSuccessResponse(speId, HttpStatus.OK, ResponseCode.CONSULTANT_GET_SUCCESS,
                            MessageConstant.SUCCESSFULLY_GET, locale, consultantResponseDTO);
        } catch (EntityNotFoundException ex) {
            log.info(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }
}
