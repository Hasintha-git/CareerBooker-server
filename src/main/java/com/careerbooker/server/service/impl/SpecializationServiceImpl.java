package com.careerbooker.server.service.impl;

import com.careerbooker.server.dto.SimpleBaseDTO;
import com.careerbooker.server.dto.request.SpecializationDTO;
import com.careerbooker.server.dto.request.UserRequestDTO;
import com.careerbooker.server.dto.response.SpecializationResponseDTO;
import com.careerbooker.server.dto.response.UserResponseDTO;
import com.careerbooker.server.entity.SpecializationType;
import com.careerbooker.server.entity.SystemUser;
import com.careerbooker.server.mapper.DtoToEntityMapper;
import com.careerbooker.server.mapper.EntityToDtoMapper;
import com.careerbooker.server.mapper.ResponseGenerator;
import com.careerbooker.server.repository.SpecializationRepository;
import com.careerbooker.server.repository.specifications.SpecializationSpecification;
import com.careerbooker.server.service.SpecializationService;
import com.careerbooker.server.util.MessageConstant;
import com.careerbooker.server.util.ResponseCode;
import com.careerbooker.server.util.enums.ClientStatusEnum;
import com.careerbooker.server.util.enums.Status;
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
public class SpecializationServiceImpl implements SpecializationService {

    private SpecializationRepository specializationRepository;
    private SpecializationSpecification specializationSpecification;

    private ModelMapper modelMapper;

    private ResponseGenerator responseGenerator;
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

            //set data
            refData.put("statusList", defaultStatus);
            refData.put("specializationList", simpleBaseDTOList);

            return refData;

        } catch (Exception e) {
            log.error("Exception : ", e);
            throw e;
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Object> getSpecializationFilterList(SpecializationDTO specializationDTO, Locale locale) throws Exception {
        try {
            PageRequest pageRequest;

            if (Objects.nonNull(specializationDTO.getSortColumn()) && Objects.nonNull(specializationDTO.getSortDirection()) &&
                    !specializationDTO.getSortColumn().isEmpty() && !specializationDTO.getSortDirection().isEmpty()) {
                pageRequest = PageRequest.of(
                        specializationDTO.getPageNumber(), specializationDTO.getPageSize(),
                        Sort.by(Sort.Direction.valueOf(specializationDTO.getSortDirection()), specializationDTO.getSortColumn())
                );
            }else{
                pageRequest = PageRequest.of(specializationDTO.getPageNumber(), specializationDTO.getPageSize());
            }

            List<SpecializationType> specializationTypeList = ((Objects.isNull(specializationDTO.getSpecializationSearchDTO())) ? specializationRepository.findAll
                    (specializationSpecification.generateSearchCriteria(Status.deleted), pageRequest) :
                    specializationRepository.findAll(specializationSpecification.generateSearchCriteria(specializationDTO.getSpecializationSearchDTO()), pageRequest))
                    .getContent();

            Long fullCount = (Objects.isNull(specializationDTO.getSpecializationSearchDTO())) ? specializationRepository.count
                    (specializationSpecification.generateSearchCriteria(Status.deleted)) :
                    specializationRepository.count(specializationSpecification.generateSearchCriteria(specializationDTO.getSpecializationSearchDTO()));

            List<SpecializationResponseDTO> collect = specializationTypeList.stream()
                    .map(faq -> EntityToDtoMapper.mapSpecialization(faq))
                    .collect(Collectors.toList());

            return responseGenerator
                    .generateSuccessResponse(specializationDTO, HttpStatus.OK, ResponseCode.SPECIALIZATION_GET_SUCCESS
                            , MessageConstant.SUCCESSFULLY_GET, locale, collect, fullCount);

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
    public ResponseEntity<Object> findSpecializationById(SpecializationDTO specializationDTO, Locale locale) throws Exception {
        try {
            SpecializationType specializationType = Optional.ofNullable(specializationRepository.findBySpecializationIdAndStatusNot(specializationDTO.getId(), Status.deleted)).orElse(
                    null
            );

            if (Objects.isNull(specializationType)) {
                return responseGenerator.generateErrorResponse(specializationDTO, HttpStatus.NOT_FOUND,
                        ResponseCode.NOT_FOUND, MessageConstant.USER_NOT_FOUND, new
                                Object[]{specializationDTO.getId()},locale);
            }

            SpecializationResponseDTO specializationResponseDTO = EntityToDtoMapper.mapSpecialization(specializationType);
            return responseGenerator
                    .generateSuccessResponse(specializationDTO, HttpStatus.OK, ResponseCode.SPECIALIZATION_GET_SUCCESS,
                            MessageConstant.SUCCESSFULLY_GET, locale, specializationResponseDTO);
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
    public ResponseEntity<Object> saveSpecialization(SpecializationDTO specializationDTO, Locale locale) throws Exception {
        try {
            SpecializationType specializationType = Optional.ofNullable(specializationRepository.findBySpecializationIdAndStatusNot(specializationDTO.getId(), Status.deleted)).orElse(
                    null
            );

            if (Objects.nonNull(specializationType)) {
                return responseGenerator.generateErrorResponse(specializationDTO, HttpStatus.CONFLICT,
                        ResponseCode.ALREADY_EXIST, MessageConstant.SPECIALIZATION_ALREADY_EXIST, new
                                Object[]{specializationDTO.getId()},locale);
            }else {
                specializationType=new SpecializationType();
            }

            DtoToEntityMapper.mapSpecialization(specializationType,specializationDTO);
            Date date = new Date();
            specializationType.setCreatedDate(date);
            specializationType.setModifiedDate(date);
            specializationType.setModifiedUser(specializationDTO.getLastUpdatedUser());
            specializationType.setCreatedUser(specializationDTO.getCreatedUser());

            specializationRepository.save(specializationType);

            return responseGenerator
                    .generateSuccessResponse(specializationDTO, HttpStatus.OK, ResponseCode.SPECIALIZATION_SAVED_SUCCESS,
                            MessageConstant.SPECIALIZATION_SUCCESSFULLY_SAVE, locale, specializationDTO);
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
    public ResponseEntity<Object> editSpecialization(SpecializationDTO specializationDTO, Locale locale) {
        try {
            SpecializationType specializationType = Optional.ofNullable(specializationRepository.findBySpecializationIdAndStatusNot(specializationDTO.getId(), Status.deleted)).orElse(
                    null
            );

            if (Objects.isNull(specializationType)) {
                return responseGenerator.generateErrorResponse(specializationDTO, HttpStatus.CONFLICT,
                        ResponseCode.NOT_FOUND, MessageConstant.SPECIALIZATION_NOT_FOUND, new
                                Object[]{specializationDTO.getId()},locale);
            }

            DtoToEntityMapper.mapSpecialization(specializationType,specializationDTO);
            Date date = new Date();
            specializationType.setModifiedDate(date);
            specializationType.setModifiedUser(specializationDTO.getLastUpdatedUser());

            specializationRepository.save(specializationType);

            return responseGenerator
                    .generateSuccessResponse(specializationDTO, HttpStatus.OK, ResponseCode.SPECIALIZATION_UPDATE_SUCCESS,
                            MessageConstant.SPECIALIZATION_SUCCESSFULLY_UPDATE, locale, specializationDTO);
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
    public ResponseEntity<Object> deleteSpecialization(SpecializationDTO specializationDTO, Locale locale) {
        try {
            SpecializationType specializationType = Optional.ofNullable(specializationRepository.findBySpecializationIdAndStatusNot(specializationDTO.getId(), Status.deleted)).orElse(
                    null
            );

            if (Objects.isNull(specializationType)) {
                return responseGenerator.generateErrorResponse(specializationDTO, HttpStatus.CONFLICT,
                        ResponseCode.NOT_FOUND, MessageConstant.SPECIALIZATION_NOT_FOUND, new
                                Object[]{specializationDTO.getId()},locale);
            }

            specializationType.setStatus(Status.deleted);

            specializationRepository.save(specializationType);

            return responseGenerator
                    .generateSuccessResponse(specializationDTO, HttpStatus.OK, ResponseCode.SPECIALIZATION_DELETE_SUCCESS,
                            MessageConstant.SPECIALIZATION_SUCCESSFULLY_DELETE, locale, specializationDTO);
        } catch (EntityNotFoundException ex) {
            log.info(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }
}
