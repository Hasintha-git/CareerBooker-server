package com.careerbooker.server.service.impl;

import com.careerbooker.server.dto.SimpleBaseDTO;
import com.careerbooker.server.dto.request.UserRoleDTO;
import com.careerbooker.server.dto.response.UserRoleResponseDTO;
import com.careerbooker.server.entity.UserRole;
import com.careerbooker.server.mapper.DtoToEntityMapper;
import com.careerbooker.server.mapper.EntityToDtoMapper;
import com.careerbooker.server.mapper.ResponseGenerator;
import com.careerbooker.server.repository.UserRoleRepository;
import com.careerbooker.server.repository.specifications.UserRoleSpecification;
import com.careerbooker.server.service.UserRoleService;
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
public class UserRoleServiceImpl implements UserRoleService {

    private ModelMapper modelMapper;

    private ResponseGenerator responseGenerator;

    private UserRoleRepository userRoleRepository;

    private UserRoleSpecification userRoleSpecification;
    @Override
    @Transactional
    public Object getReferenceData() {
        try {
            Map<String, Object> refData = new HashMap<>();

            //get status
            List<SimpleBaseDTO> defaultStatus = Stream.of(ClientStatusEnum.values()).map(statusEnum -> new SimpleBaseDTO(statusEnum.getCode(), statusEnum.getDescription())).collect(Collectors.toList());

            //set data
            refData.put("statusList", defaultStatus);

            return refData;

        } catch (Exception e) {
            log.error("Exception : ", e);
            throw e;
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Object> getUserRoleFilterList(UserRoleDTO userRoleDTO, Locale locale) throws Exception {
        try {
            PageRequest pageRequest;

            if (Objects.nonNull(userRoleDTO.getSortColumn()) && Objects.nonNull(userRoleDTO.getSortDirection()) &&
                    !userRoleDTO.getSortColumn().isEmpty() && !userRoleDTO.getSortDirection().isEmpty()) {
                pageRequest = PageRequest.of(
                        userRoleDTO.getPageNumber(), userRoleDTO.getPageSize(),
                        Sort.by(Sort.Direction.valueOf(userRoleDTO.getSortDirection()), userRoleDTO.getSortColumn())
                );
            }else{
                pageRequest = PageRequest.of(userRoleDTO.getPageNumber(), userRoleDTO.getPageSize());
            }

            List<UserRole> userRoleList = ((Objects.isNull(userRoleDTO.getUserRoleSearchDTO())) ? userRoleRepository.findAll
                    (userRoleSpecification.generateSearchCriteria(Status.deleted), pageRequest) :
                    userRoleRepository.findAll(userRoleSpecification.generateSearchCriteria(userRoleDTO.getUserRoleSearchDTO()), pageRequest))
                    .getContent();

            Long fullCount = (Objects.isNull(userRoleDTO.getUserRoleSearchDTO())) ? userRoleRepository.count
                    (userRoleSpecification.generateSearchCriteria(Status.deleted)) :
                    userRoleRepository.count(userRoleSpecification.generateSearchCriteria(userRoleDTO.getUserRoleSearchDTO()));

            List<UserRoleResponseDTO> collect = userRoleList.stream()
                    .map(userRole -> EntityToDtoMapper.mapUserRole(userRole))
                    .collect(Collectors.toList());

            return responseGenerator
                    .generateSuccessResponse(userRoleDTO, HttpStatus.OK, ResponseCode.USER_ROLE_GET_SUCCESS
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
    public ResponseEntity<Object> findUserRoleById(UserRoleDTO userRoleDTO, Locale locale) throws Exception {
        try {
            UserRole userRole = Optional.ofNullable(userRoleRepository.findByIdAndStatusCodeNot(userRoleDTO.getId(), Status.deleted)).orElse(
                    null
            );

            if (Objects.isNull(userRole)) {
                return responseGenerator.generateErrorResponse(userRoleDTO, HttpStatus.NOT_FOUND,
                        ResponseCode.NOT_FOUND, MessageConstant.USER_ROLE_NOT_FOUND, new
                                Object[]{userRoleDTO.getId()},locale);
            }

            UserRoleResponseDTO userRoleResponseDTO = EntityToDtoMapper.mapUserRole(userRole);
            return responseGenerator
                    .generateSuccessResponse(userRoleDTO, HttpStatus.OK, ResponseCode.USER_ROLE_GET_SUCCESS,
                            MessageConstant.SUCCESSFULLY_GET, locale, userRoleResponseDTO);
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
    public ResponseEntity<Object> saveUserRole(UserRoleDTO userRoleDTO, Locale locale) throws Exception {
        try {

            UserRole userRole = Optional.ofNullable(userRoleRepository.findByCodeAndStatusCodeNot(userRoleDTO.getCode(), Status.deleted)).orElse(
                    null
            );

            if (Objects.nonNull(userRole)) {
                return responseGenerator.generateErrorResponse(userRoleDTO, HttpStatus.NOT_FOUND,
                        ResponseCode.ALREADY_EXIST, MessageConstant.USER_ROLE_ALREADY_EXIST, new
                                Object[]{userRoleDTO.getId()},locale);
            }

            userRole = new UserRole();
            DtoToEntityMapper.mapUserRole(userRole,userRoleDTO);
            Date date = new Date();
            userRole.setLastUpdatedTime(date);
            userRole.setCreatedTime(date);
            userRole.setLastUpdatedUser(userRoleDTO.getLastUpdatedUser());
            userRole.setCreatedUser(userRoleDTO.getLastUpdatedUser());

            userRoleRepository.save(userRole);

            return responseGenerator
                    .generateSuccessResponse(userRoleDTO, HttpStatus.OK, ResponseCode.USER_ROLE_SAVED_SUCCESS,
                            MessageConstant.USER_ROLE_SAVE, locale, userRoleDTO);
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
    public ResponseEntity<Object> editUserRole(UserRoleDTO userRoleDTO, Locale locale) {
        try {
            UserRole userRole = Optional.ofNullable(userRoleRepository.findByCodeAndStatusCodeNot(userRoleDTO.getCode(), Status.deleted)).orElse(
                    null
            );

            if (Objects.isNull(userRole)) {
                return responseGenerator.generateErrorResponse(userRoleDTO, HttpStatus.CONFLICT,
                        ResponseCode.NOT_FOUND, MessageConstant.USER_ROLE_NOT_FOUND, new
                                Object[]{userRoleDTO.getCode()},locale);
            }

            userRole.setStatusCode(Status.valueOf(userRoleDTO.getStatus()));
            userRole.setDescription(userRoleDTO.getDescription());
            Date date = new Date();
            userRole.setLastUpdatedTime(date);
            userRole.setLastUpdatedUser(userRoleDTO.getLastUpdatedUser());

            userRoleRepository.save(userRole);

            return responseGenerator
                    .generateSuccessResponse(userRoleDTO, HttpStatus.OK, ResponseCode.SPECIALIZATION_UPDATE_SUCCESS,
                            MessageConstant.SPECIALIZATION_SUCCESSFULLY_UPDATE, locale, userRoleDTO);
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
    public ResponseEntity<Object> deleteUserRole(UserRoleDTO userRoleDTO, Locale locale) {
        try {
            UserRole userRole = Optional.ofNullable(userRoleRepository.findByIdAndStatusCodeNot(userRoleDTO.getId(), Status.deleted)).orElse(
                    null
            );

            if (Objects.isNull(userRole)) {
                return responseGenerator.generateErrorResponse(userRoleDTO, HttpStatus.NOT_FOUND,
                        ResponseCode.NOT_FOUND, MessageConstant.USER_ROLE_NOT_FOUND, new
                                Object[]{userRoleDTO.getId()},locale);
            }

            userRole.setStatusCode(Status.deleted);

            userRoleRepository.save(userRole);

            return responseGenerator
                    .generateSuccessResponse(userRoleDTO, HttpStatus.OK, ResponseCode.USER_ROLE_DELETE_SUCCESS,
                            MessageConstant.USER_ROLE_DELETED, locale, userRoleDTO);
        } catch (EntityNotFoundException ex) {
            log.info(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }
}
