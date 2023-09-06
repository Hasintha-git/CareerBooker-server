package com.careerbooker.server.service.impl;

import com.careerbooker.server.dto.DataTableDTO;
import com.careerbooker.server.dto.SimpleBaseDTO;
import com.careerbooker.server.dto.request.ConsultantsDTO;
import com.careerbooker.server.dto.response.ConsultantResponseDTO;
import com.careerbooker.server.entity.Consultants;
import com.careerbooker.server.entity.SpecializationType;
import com.careerbooker.server.entity.SystemUser;
import com.careerbooker.server.mapper.DtoToEntityMapper;
import com.careerbooker.server.mapper.EntityToDtoMapper;
import com.careerbooker.server.mapper.ResponseGenerator;
import com.careerbooker.server.repository.ConsultantDaysRepository;
import com.careerbooker.server.repository.ConsultantRepository;
import com.careerbooker.server.repository.SpecializationRepository;
import com.careerbooker.server.repository.UserRepository;
import com.careerbooker.server.repository.specifications.ConsultantSpecification;
import com.careerbooker.server.service.ConsultantService;
import com.careerbooker.server.util.MessageConstant;
import com.careerbooker.server.util.ResponseCode;
import com.careerbooker.server.util.enums.ClientStatusEnum;
import com.careerbooker.server.util.enums.Status;
import com.careerbooker.server.util.enums.TimeSlot;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
public class ConsultantServiceImpl implements ConsultantService {

    private ConsultantRepository consultantRepository;
    private ConsultantSpecification consultantSpecification;
    private ModelMapper modelMapper;
    private ResponseGenerator responseGenerator;
    private SpecializationRepository specializationRepository;
    private ConsultantDaysRepository consultantDaysRepository;
    private UserRepository userRepository;

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
    @Transactional
    public Object getConsultantFilterList(ConsultantsDTO consultantsDTO, Locale locale) throws Exception {
        try {
            PageRequest pageRequest;

            if (Objects.nonNull(consultantsDTO.getSortColumn()) && Objects.nonNull(consultantsDTO.getSortDirection()) &&
                    !consultantsDTO.getSortColumn().isEmpty() && !consultantsDTO.getSortDirection().isEmpty()) {
                pageRequest = PageRequest.of(
                        consultantsDTO.getPageNumber(), consultantsDTO.getPageSize(),
                        Sort.by(Sort.Direction.valueOf(consultantsDTO.getSortDirection()), consultantsDTO.getSortColumn())
                );
            }else{
                pageRequest = PageRequest.of(consultantsDTO.getPageNumber(), consultantsDTO.getPageSize());
            }

            List<Consultants> consultantsList = ((Objects.isNull(consultantsDTO.getConsultantSearchDTO())) ? consultantRepository.findAll
                    (consultantSpecification.generateSearchCriteria(Status.deleted), pageRequest) :
                    consultantRepository.findAll(consultantSpecification.generateSearchCriteria(consultantsDTO.getConsultantSearchDTO()), pageRequest))
                    .getContent();

            Long fullCount = (Objects.isNull(consultantsDTO.getConsultantSearchDTO())) ? consultantRepository.count
                    (consultantSpecification.generateSearchCriteria(Status.deleted)) :
                    consultantRepository.count(consultantSpecification.generateSearchCriteria(consultantsDTO.getConsultantSearchDTO()));

            List<ConsultantResponseDTO> collect = consultantsList.stream()
                    .map(con -> EntityToDtoMapper.mapConsultant(con))
                    .collect(Collectors.toList());

            return new DataTableDTO<>(fullCount, (long) collect.size(), collect, null);
//            return responseGenerator
//                    .generateSuccessResponse(consultantsDTO, HttpStatus.OK, ResponseCode.CONSULTANT_GET_SUCCESS
//                            , MessageConstant.SUCCESSFULLY_GET, locale, collect, fullCount);

        } catch (EntityNotFoundException ex) {
            log.info(ex.getMessage());
            throw ex;
        }catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Object> findConsultantById(ConsultantsDTO consultantsDTO, Locale locale) throws Exception {
        try {
            Consultants consultants = Optional.ofNullable(consultantRepository.findByConsultantIdAndStatusNot(consultantsDTO.getCon_id(), Status.deleted)).orElse(
                    null
            );

            if (Objects.isNull(consultants)) {
                return responseGenerator.generateErrorResponse(consultantsDTO, HttpStatus.NOT_FOUND,
                        ResponseCode.NOT_FOUND, MessageConstant.CONSULTANT_NOT_FOUND, new
                                Object[]{consultantsDTO.getCon_id()},locale);
            }

            ConsultantResponseDTO consultantResponseDTO = EntityToDtoMapper.mapConsultant(consultants);
            return responseGenerator
                    .generateSuccessResponse(consultantsDTO, HttpStatus.OK, ResponseCode.CONSULTANT_GET_SUCCESS,
                            MessageConstant.SUCCESSFULLY_GET, locale, consultantResponseDTO);
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
    public ResponseEntity<Object> saveConsultant(ConsultantsDTO consultantsDTO, Locale locale) throws Exception {
        try {

            SpecializationType specializationType = Optional.ofNullable(specializationRepository.findBySpecializationIdAndStatusNot
                    (consultantsDTO.getSpe_id(), Status.deleted)).orElse(
                    null
            );

            if (Objects.isNull(specializationType)) {
                return responseGenerator.generateErrorResponse(consultantsDTO, HttpStatus.CONFLICT,
                        ResponseCode.NOT_FOUND, MessageConstant.SPECIALIZATION_NOT_FOUND, new
                                Object[]{consultantsDTO.getSpe_id()},locale);
            }

            SystemUser user = Optional.ofNullable(userRepository.findByIdAndStatusNot(consultantsDTO.getUserId(), Status.deleted)).orElse(
                    null
            );

            if (Objects.isNull(specializationType)) {
                return responseGenerator.generateErrorResponse(consultantsDTO, HttpStatus.CONFLICT,
                        ResponseCode.NOT_FOUND, MessageConstant.USER_NOT_FOUND, new
                                Object[]{consultantsDTO.getUserId()},locale);
            }

            Consultants consultants = new Consultants();

            DtoToEntityMapper.mapConsultant(consultants,consultantsDTO);
            Date date = new Date();
            consultants.setCreatedDate(date);
            consultants.setModifiedDate(date);
            consultants.setModifiedUser(consultantsDTO.getLastUpdatedUser());
            consultants.setCreatedUser(consultantsDTO.getCreatedUser());
            consultants.setSpecializations(specializationType);
            consultants.setSystemUser(user);

            consultantRepository.save(consultants);

            return responseGenerator
                    .generateSuccessResponse(consultantsDTO, HttpStatus.OK, ResponseCode.CONSULTANT_SAVED_SUCCESS,
                            MessageConstant.SPECIALIZATION_SUCCESSFULLY_SAVE, locale, consultantsDTO);
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
    public ResponseEntity<Object> editConsultant(ConsultantsDTO consultantsDTO, Locale locale) {
        try {
            Consultants consultants = Optional.ofNullable(consultantRepository.findByConsultantIdAndStatusNot(consultantsDTO.getCon_id(), Status.deleted)).orElse(null);

            if (Objects.isNull(consultants)) {
                return responseGenerator.generateErrorResponse(consultantsDTO, HttpStatus.CONFLICT,
                        ResponseCode.NOT_FOUND, MessageConstant.CONSULTANT_NOT_FOUND, new
                                Object[]{consultantsDTO.getCon_id()},locale);
            }

            SpecializationType specializationType = Optional.ofNullable(specializationRepository.findBySpecializationIdAndStatusNot
                    (consultantsDTO.getSpe_id(), Status.deleted)).orElse(
                    null
            );

            if (Objects.isNull(specializationType)) {
                return responseGenerator.generateErrorResponse(consultantsDTO, HttpStatus.CONFLICT,
                        ResponseCode.NOT_FOUND, MessageConstant.SPECIALIZATION_NOT_FOUND, new
                                Object[]{consultantsDTO.getSpe_id()},locale);
            }


            SystemUser user = Optional.ofNullable(userRepository.findByIdAndStatusNot(consultantsDTO.getUserId(), Status.deleted)).orElse(
                    null
            );

            if (Objects.isNull(user)) {
                return responseGenerator.generateErrorResponse(consultantsDTO, HttpStatus.CONFLICT,
                        ResponseCode.NOT_FOUND, MessageConstant.USER_NOT_FOUND, new
                                Object[]{consultantsDTO.getUserId()},locale);
            }

            consultants.setSpecializations(specializationType);
            consultants.setStatus(Status.valueOf(consultantsDTO.getStatus()));
            consultants.setModifiedDate(new Date());
            consultants.setModifiedUser(consultantsDTO.getLastUpdatedUser());


            consultantRepository.save(consultants);
            return responseGenerator
                    .generateSuccessResponse(consultantsDTO, HttpStatus.OK, ResponseCode.CONSULTANT_UPDATE_SUCCESS,
                            MessageConstant.CONSULTANT_SUCCESSFULLY_UPDATE, locale, consultantsDTO);
        }catch (EntityNotFoundException ex) {
            log.error(ex.getMessage());
            throw ex;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Object> deleteConsultant(ConsultantsDTO consultantsDTO, Locale locale) {
        try {
            Consultants consultants = Optional.ofNullable(consultantRepository.findByConsultantIdAndStatusNot(consultantsDTO.getCon_id(), Status.deleted)).orElse(null);

            if (Objects.isNull(consultants)) {
                return responseGenerator.generateErrorResponse(consultantsDTO, HttpStatus.CONFLICT,
                        ResponseCode.NOT_FOUND, MessageConstant.CONSULTANT_NOT_FOUND, new
                                Object[]{consultantsDTO.getCon_id()},locale);
            }

            consultants.setStatus(Status.deleted);

            consultantRepository.save(consultants);
            return responseGenerator
                    .generateSuccessResponse(consultantsDTO, HttpStatus.OK, ResponseCode.CONSULTANT_DELETE_SUCCESS,
                            MessageConstant.CONSULTANT_SUCCESSFULLY_DELETE, locale, consultantsDTO);
        }catch (EntityNotFoundException ex) {
            log.error(ex.getMessage());
            throw ex;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }


}
