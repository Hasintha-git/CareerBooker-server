package com.careerbooker.server.service.impl;

import com.careerbooker.server.dto.request.TimeSlotDTO;
import com.careerbooker.server.dto.response.*;
import com.careerbooker.server.entity.ConsultantDays;
import com.careerbooker.server.entity.Consultants;
import com.careerbooker.server.entity.Days;
import com.careerbooker.server.mapper.EntityToDtoMapper;
import com.careerbooker.server.mapper.ResponseGenerator;
import com.careerbooker.server.repository.ConsultantDaysRepository;
import com.careerbooker.server.repository.ConsultantRepository;
import com.careerbooker.server.repository.DayRepository;
import com.careerbooker.server.service.ConsultantDaysService;
import com.careerbooker.server.util.MessageConstant;
import com.careerbooker.server.util.ResponseCode;
import com.careerbooker.server.util.enums.ClientStatusEnum;
import com.careerbooker.server.util.enums.Status;
import com.careerbooker.server.util.enums.TimeSlot;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ConsultantDaysServiceImpl implements ConsultantDaysService {

    private ConsultantRepository consultantRepository;
    private ConsultantDaysRepository consultantDaysRepository;
    private DayRepository dayRepository;
    private ModelMapper modelMapper;
    private ResponseGenerator responseGenerator;

    @Override
    public Object getReferenceData() {
        return null;
    }

    @Override
    public ResponseEntity<Object> getTimeSlotFilterList(TimeSlotDTO timeSlotDTO, Locale locale) throws Exception {
        return null;
    }

    @Override
    @Transactional
    public ResponseEntity<Object> findTimeSlotById(TimeSlotDTO timeSlotDTO, Locale locale) throws Exception {
        try {
            ConsultantDays consultantDays = Optional.ofNullable(consultantDaysRepository.findConsultantDaysByIdAndStatus
                    (timeSlotDTO.getId(), Status.valueOf(timeSlotDTO.getStatus()))).orElse(null);

            if (Objects.isNull(consultantDays)) {
                return responseGenerator.generateErrorResponse(timeSlotDTO, HttpStatus.CONFLICT,
                        ResponseCode.NOT_FOUND, MessageConstant.CONSULTANT_DAYS_NOT_FOUND, new
                                Object[]{timeSlotDTO.getCon_id()}, locale);
            }

            return responseGenerator
                    .generateSuccessResponse(timeSlotDTO, HttpStatus.OK, ResponseCode.CONSULTANT_DAYS_GET_SUCCESS,
                            MessageConstant.SUCCESSFULLY_GET, locale, consultantDays);
        } catch (EntityNotFoundException ex) {
            log.info(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Object> findTimeSlotByCon(TimeSlotDTO timeSlotDTO, Locale locale) throws Exception {
        try {
            Consultants consultants = Optional.ofNullable(consultantRepository.findByConsultantIdAndStatusNot
                    (timeSlotDTO.getCon_id(), Status.deleted)).orElse(null);

            if (Objects.isNull(consultants)) {
                return responseGenerator.generateErrorResponse(timeSlotDTO, HttpStatus.CONFLICT,
                        ResponseCode.NOT_FOUND, MessageConstant.CONSULTANT_NOT_FOUND, new
                                Object[]{timeSlotDTO.getCon_id()}, locale);
            }

            List<ConsultantDays> consultantDays = Optional.ofNullable(consultantDaysRepository.findAllByConsultantAndStatus
                    (consultants, Status.valueOf(timeSlotDTO.getStatus()))).orElse(null);

            if (Objects.isNull(consultantDays) || consultantDays.size() == 0) {
                return responseGenerator.generateErrorResponse(timeSlotDTO, HttpStatus.CONFLICT,
                        ResponseCode.NOT_FOUND, MessageConstant.CONSULTANT_DAYS_NOT_FOUND, new
                                Object[]{timeSlotDTO.getCon_id()}, locale);
            }

            DaysList daysList= EntityToDtoMapper.mapConsultantDays(consultantDays);

            ConsultantDaysResponseDTO consultantDaysResponseDTO = new ConsultantDaysResponseDTO();

            consultantDaysResponseDTO.setAvailability(daysList);

            consultantDaysResponseDTO.setId(consultantDays.get(0).getId());
            consultantDaysResponseDTO.setConsultant_id(consultantDays.get(0).getConsultant().getConsultantId());
            consultantDaysResponseDTO.setSpe_id(consultantDays.get(0).getConsultant().getSpecializations().getSpecializationId());
            consultantDaysResponseDTO.setSpeDescription(consultantDays.get(0).getConsultant().getSpecializations().getName());
            consultantDaysResponseDTO.setStatus(String.valueOf(ClientStatusEnum.getEnum(String.valueOf(consultantDays.get(0).getStatus())).getCode()));
            consultantDaysResponseDTO.setStatusDescription(String.valueOf(ClientStatusEnum.getEnum(String.valueOf(consultantDays.get(0).getStatus())).getDescription()));

            return responseGenerator
                    .generateSuccessResponse(timeSlotDTO, HttpStatus.OK, ResponseCode.CONSULTANT_DAYS_GET_SUCCESS,
                            MessageConstant.CONSULTANT_DAYS_SUCCESSFULLY_FIND, locale, consultantDaysResponseDTO);
        } catch (EntityNotFoundException ex) {
            log.info(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }

    private Days createDaysList(Date date) {
        try {
        Days days= new Days();

        Days days1 = Optional.ofNullable(dayRepository.findByDate(date)).orElse(null);
        if (Objects.nonNull(days1)) {
            return days1;
        }
        days.setStatusCode(Status.active);
        days.setDate(date);
        dayRepository.save(days);
        return days;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }



    @Override
    @Transactional
    public ResponseEntity<Object> findTimeSlotByConAndDay(TimeSlotDTO timeSlotDTO, Locale locale) throws Exception {
        try {
            System.out.println(">>>"+timeSlotDTO);
            Consultants consultants = Optional.ofNullable(consultantRepository.findByConsultantIdAndStatusNot
                    (timeSlotDTO.getCon_id(), Status.deleted)).orElse(null);

            if (Objects.isNull(consultants)) {
                return responseGenerator.generateErrorResponse(timeSlotDTO, HttpStatus.CONFLICT,
                        ResponseCode.NOT_FOUND, MessageConstant.CONSULTANT_NOT_FOUND, new
                                Object[]{timeSlotDTO.getCon_id()}, locale);
            }

            Days byDate = dayRepository.findByDate(timeSlotDTO.getDay());
            List<ConsultantDays> consultantDays = Optional.ofNullable(consultantDaysRepository.findAllByConsultantAndDaysAndStatus
                    (consultants,byDate, Status.valueOf(timeSlotDTO.getStatus()))).orElse(null);

            if (Objects.isNull(consultants)) {
                return responseGenerator.generateErrorResponse(timeSlotDTO, HttpStatus.CONFLICT,
                        ResponseCode.NOT_FOUND, MessageConstant.CONSULTANT_DAYS_NOT_FOUND, new
                                Object[]{timeSlotDTO.getCon_id()}, locale);
            }

            DaysList daysList= EntityToDtoMapper.mapConsultantDays(consultantDays);

            ConsultantDaysResponseDTO consultantDaysResponseDTO = new ConsultantDaysResponseDTO();

            consultantDaysResponseDTO.setAvailability(daysList);

            consultantDaysResponseDTO.setId(consultantDays.get(0).getId());
            consultantDaysResponseDTO.setConsultant_id(consultantDays.get(0).getConsultant().getConsultantId());
            consultantDaysResponseDTO.setSpe_id(consultantDays.get(0).getConsultant().getSpecializations().getSpecializationId());
            consultantDaysResponseDTO.setSpeDescription(consultantDays.get(0).getConsultant().getSpecializations().getName());
            consultantDaysResponseDTO.setStatus(String.valueOf(ClientStatusEnum.getEnum(String.valueOf(consultantDays.get(0).getStatus())).getCode()));
            consultantDaysResponseDTO.setStatusDescription(String.valueOf(ClientStatusEnum.getEnum(String.valueOf(consultantDays.get(0).getStatus())).getDescription()));

            return responseGenerator
                    .generateSuccessResponse(timeSlotDTO, HttpStatus.OK, ResponseCode.CONSULTANT_DAYS_GET_SUCCESS,
                            MessageConstant.CONSULTANT_DAYS_SUCCESSFULLY_FIND, locale, consultantDaysResponseDTO);
        } catch (EntityNotFoundException ex) {
            log.info(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Object> saveTimeSlot(TimeSlotDTO timeSlotDTO, Locale locale) throws Exception {
        try {
            Consultants consultants = Optional.ofNullable(consultantRepository.findByConsultantIdAndStatusNot(timeSlotDTO.getCon_id(), Status.deleted)).orElse(null);

            if (Objects.isNull(consultants)) {
                return responseGenerator.generateErrorResponse(timeSlotDTO, HttpStatus.CONFLICT,
                        ResponseCode.NOT_FOUND, MessageConstant.CONSULTANT_NOT_FOUND, new
                                Object[]{timeSlotDTO.getCon_id()}, locale);
            }

            for (int i = 0; i < timeSlotDTO.getSlotDtoList().size(); i++) {

                Days days = createDaysList(timeSlotDTO.getSlotDtoList().get(i).getDay());
                for (TimeSlot timeSlot : timeSlotDTO.getSlotDtoList().get(i).getTimeSlots()) {

                    Date day = timeSlotDTO.getSlotDtoList().get(i).getDay();

                    Days byDate = dayRepository.findByDate(day);

                    Long existingEntryCount = consultantDaysRepository.countAllByConsultantAndDaysAndSlotAndStatusNot(
                            consultants, byDate, timeSlot, Status.deleted);

                    if (existingEntryCount > 0) {
                        continue;
                    }



                    ConsultantDays consultantDays = new ConsultantDays();
                    consultantDays.setCreatedUser(timeSlotDTO.getCreatedUser());
                    consultantDays.setCreatedDate(new Date());
                    consultantDays.setModifiedDate(new Date());
                    consultantDays.setModifiedUser(timeSlotDTO.getLastUpdatedUser());
                    consultantDays.setStatus(Status.valueOf(timeSlotDTO.getStatus()));
                    consultantDays.setDays(days);
                    consultantDays.setConsultant(consultants);
                    consultantDays.setSlot(timeSlot);

                    consultantDaysRepository.save(consultantDays);
                }
            }

            return responseGenerator
                    .generateSuccessResponse(timeSlotDTO, HttpStatus.OK, ResponseCode.CONSULTANT_DAYS_SAVED_SUCCESS,
                            MessageConstant.CONSULTANT_DAYS_SUCCESSFULLY_SAVE, locale, timeSlotDTO);

        } catch (EntityNotFoundException ex) {
            log.info(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }


    @Override
    public ResponseEntity<Object> editTimeSlot(TimeSlotDTO timeSlotDTO, Locale locale) {
        return null;
    }

    @Override
    public ResponseEntity<Object> deleteTimeSlot(TimeSlotDTO timeSlotDTO, Locale locale) {
        return null;
    }
}
