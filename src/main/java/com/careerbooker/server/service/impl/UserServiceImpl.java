package com.careerbooker.server.service.impl;

import com.careerbooker.server.dto.DataTableDTO;
import com.careerbooker.server.dto.SimpleBaseDTO;
import com.careerbooker.server.dto.request.ConsultantsDTO;
import com.careerbooker.server.dto.request.UserRequestDTO;
import com.careerbooker.server.dto.response.UserResponseDTO;
import com.careerbooker.server.entity.Appointments;
import com.careerbooker.server.entity.Consultants;
import com.careerbooker.server.entity.SystemUser;
import com.careerbooker.server.entity.UserRole;
import com.careerbooker.server.mapper.DtoToEntityMapper;
import com.careerbooker.server.mapper.EntityToDtoMapper;
import com.careerbooker.server.mapper.ResponseGenerator;
import com.careerbooker.server.repository.AppointmentRepository;
import com.careerbooker.server.repository.ConsultantRepository;
import com.careerbooker.server.repository.UserRepository;
import com.careerbooker.server.repository.UserRoleRepository;
import com.careerbooker.server.repository.specifications.UserSpecification;
import com.careerbooker.server.service.ConsultantService;
import com.careerbooker.server.service.UserService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private ConsultantRepository consultantRepository;
    private ConsultantService consultantService;

    private AppointmentRepository appointmentRepository;

    private UserRoleRepository userRoleRepository;

    private UserSpecification userSpecification;

    private ModelMapper modelMapper;

    private ResponseGenerator responseGenerator;


    private PasswordEncoder passwordEncoder;

    @Override
    public Object getReferenceData() {
        try {
            Map<String, Object> refData = new HashMap<>();

            //get status
            List<SimpleBaseDTO> defaultStatus = Stream.of(ClientStatusEnum.values()).map(statusEnum -> new SimpleBaseDTO(statusEnum.getCode(), statusEnum.getDescription())).collect(Collectors.toList());
            //get user role list
            List<UserRole> data = userRoleRepository.findAllByStatusCodeNot(Status.deleted);
            List<SimpleBaseDTO> simpleBaseDTOList = data.stream().map(userRole -> {
                SimpleBaseDTO simpleBaseDTO = new SimpleBaseDTO();
                return EntityToDtoMapper.mapUserRoleDropdown(simpleBaseDTO, userRole);
            }).collect(Collectors.toList());

            //set data
            refData.put("statusList", defaultStatus);
            refData.put("userRoleList", simpleBaseDTOList);

            return refData;

        } catch (Exception e) {
            log.error("Exception : ", e);
            throw e;
        }
    }

    @Override
    @Transactional
    public Object getUserFilterList(UserRequestDTO userRequestDTO, Locale locale) throws Exception {
        try {
            PageRequest pageRequest;

            if (Objects.nonNull(userRequestDTO.getSortColumn()) && Objects.nonNull(userRequestDTO.getSortDirection()) &&
                    !userRequestDTO.getSortColumn().isEmpty() && !userRequestDTO.getSortDirection().isEmpty()) {
                pageRequest = PageRequest.of(
                        userRequestDTO.getPageNumber(), userRequestDTO.getPageSize(),
                        Sort.by(Sort.Direction.valueOf(userRequestDTO.getSortDirection()), userRequestDTO.getSortColumn())
                );
            }else{
                pageRequest = PageRequest.of(userRequestDTO.getPageNumber(), userRequestDTO.getPageSize());
            }

            List<SystemUser> faqList = ((Objects.isNull(userRequestDTO.getUserRequestSearchDTO())) ? userRepository.findAll
                    (userSpecification.generateSearchCriteria(Status.deleted), pageRequest) :
                    userRepository.findAll(userSpecification.generateSearchCriteria(userRequestDTO.getUserRequestSearchDTO()), pageRequest))
                    .getContent();

            Long fullCount = (Objects.isNull(userRequestDTO.getUserRequestSearchDTO())) ? userRepository.count
                    (userSpecification.generateSearchCriteria(Status.deleted)) :
                    userRepository.count(userSpecification.generateSearchCriteria(userRequestDTO.getUserRequestSearchDTO()));

            List<UserResponseDTO> collect = faqList.stream()
                    .map(user -> EntityToDtoMapper.mapUser(user))
                    .collect(Collectors.toList());

            return new DataTableDTO<>(fullCount, (long) collect.size(), collect, null);
//            return responseGenerator
//                    .generateSuccessResponse(userRequestDTO, HttpStatus.OK, ResponseCode.USER_GET_SUCCESS
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
    public ResponseEntity<Object> findUserById(UserRequestDTO userRequestDTO, Locale locale) throws Exception {
        try {
            SystemUser systemUser = Optional.ofNullable(userRepository.findByIdAndStatusNot(userRequestDTO.getId(), Status.deleted)).orElse(
                    null
            );

            if (Objects.isNull(systemUser)) {
                return responseGenerator.generateErrorResponse(userRequestDTO, HttpStatus.NOT_FOUND,
                        ResponseCode.NOT_FOUND, MessageConstant.USER_NOT_FOUND, new
                                Object[]{userRequestDTO.getId()},locale);
            }

            UserResponseDTO userResponseDTO = EntityToDtoMapper.mapUser(systemUser);
            userResponseDTO.setUserRole(systemUser.getUserRole().getCode());
            userResponseDTO.setUserRoleDescription(systemUser.getUserRole().getDescription());
            return responseGenerator
                    .generateSuccessResponse(userRequestDTO, HttpStatus.OK, ResponseCode.USER_GET_SUCCESS,
                            MessageConstant.SUCCESSFULLY_GET, locale, userResponseDTO);
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
    public ResponseEntity<Object> findUserByNic(UserRequestDTO userRequestDTO, Locale locale) throws Exception {
        try {
            SystemUser systemUser = Optional.ofNullable(userRepository.findByNicAndStatusNot(userRequestDTO.getNic(), Status.deleted)).orElse(
                    null
            );

            if (Objects.isNull(systemUser)) {
                return responseGenerator.generateErrorResponse(userRequestDTO, HttpStatus.NOT_FOUND,
                        ResponseCode.NOT_FOUND, MessageConstant.USER_NOT_FOUND, new
                                Object[]{userRequestDTO.getId()},locale);
            }

            UserResponseDTO userResponseDTO = EntityToDtoMapper.mapUser(systemUser);
            userResponseDTO.setUserRole(systemUser.getUserRole().getCode());
            userResponseDTO.setUserRoleDescription(systemUser.getUserRole().getDescription());
            return responseGenerator
                    .generateSuccessResponse(userRequestDTO, HttpStatus.OK, ResponseCode.USER_GET_SUCCESS,
                            MessageConstant.SUCCESSFULLY_GET, locale, userResponseDTO);
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
    public ResponseEntity<Object> saveUser(UserRequestDTO userRequestDTO, Locale locale) throws Exception {
        try{
            SystemUser systemUser = Optional.ofNullable(userRepository.findByUsernameAndStatusNot(userRequestDTO.getUsername(), Status.deleted))
                    .orElse(null);
            if (Objects.nonNull(systemUser)) {
                return responseGenerator
                        .generateErrorResponse(userRequestDTO, HttpStatus.CONFLICT,
                                ResponseCode.ALREADY_EXIST ,  MessageConstant.USER_NAME_ALREADY_EXIST, new Object[] {userRequestDTO.getUsername()},
                                locale);
            }
            systemUser = Optional.ofNullable(userRepository.findByEmailAndStatusNot(userRequestDTO.getEmail(), Status.deleted))
                    .orElse(null);
            if (Objects.nonNull(systemUser)) {
                return responseGenerator
                        .generateErrorResponse(userRequestDTO, HttpStatus.CONFLICT,
                                ResponseCode.ALREADY_EXIST ,  MessageConstant.USER_EMAIL_ALREADY_EXIST, new Object[] {userRequestDTO.getEmail()},
                                locale);
            }

            systemUser = Optional.ofNullable(userRepository.findByNicAndStatusNot(userRequestDTO.getNic(), Status.deleted))
                    .orElse(null);
            if (Objects.nonNull(systemUser)) {
                return responseGenerator
                        .generateErrorResponse(userRequestDTO, HttpStatus.CONFLICT,
                                ResponseCode.ALREADY_EXIST ,  MessageConstant.USER_NIC_ALREADY_EXIST, new Object[] {userRequestDTO.getNic()},
                                locale);
            }

            systemUser = Optional.ofNullable(userRepository.findByMobileNoAndStatusNot(userRequestDTO.getMobileNo(), Status.deleted))
                    .orElse(null);
            if (Objects.nonNull(systemUser)) {
                return responseGenerator
                        .generateErrorResponse(userRequestDTO, HttpStatus.CONFLICT,
                                ResponseCode.ALREADY_EXIST ,  MessageConstant.USER_MOBILE_ALREADY_EXIST, new Object[] {userRequestDTO.getMobileNo()},
                                locale);
            }

            if (Objects.isNull(userRequestDTO.getUserRole())) {
                userRequestDTO.setUserRole("seeker");
            }

            UserRole userRole=null;
            if (Objects.nonNull(userRequestDTO.getUserRole())) {
                userRole = Optional.ofNullable(userRoleRepository.findByCodeAndStatusCode(userRequestDTO.getUserRole(), Status.active)).orElse(null);
                if (Objects.isNull(userRole)) {
                    return responseGenerator
                            .generateErrorResponse(userRequestDTO, HttpStatus.NOT_FOUND,
                                    ResponseCode.NOT_FOUND ,  MessageConstant.USER_ROLE_NOT_FOUND,
                                    locale);
                }
            }else {
                userRole= Optional.ofNullable(userRoleRepository.findByCodeAndStatusCodeNot("seeker", Status.deleted)).orElse(null);
            }


            systemUser = Optional.ofNullable(userRepository.findByUsernameAndStatus(userRequestDTO.getNic(), Status.deleted))
                    .orElse(new SystemUser());


            Date systemDate = new Date();
            modelMapper.map(userRequestDTO, systemUser);
            if (Objects.nonNull(userRequestDTO.getStatus())) {
                systemUser.setStatus(Status.valueOf(userRequestDTO.getStatus()));
            }else {
                systemUser.setStatus(Status.active);
            }
            systemUser.setPwStatus(Status.active);
            systemUser.setPasswordExpireDate(systemDate);
            if (Objects.nonNull(userRequestDTO.getActiveUserName())) {
                systemUser.setCreatedUser(userRequestDTO.getActiveUserName());
                systemUser.setLastUpdatedUser(userRequestDTO.getActiveUserName());
            }else {
                systemUser.setCreatedUser(userRequestDTO.getUsername());
                systemUser.setLastUpdatedUser(userRequestDTO.getUsername());
            }
            systemUser.setCreatedTime(systemDate);
            systemUser.setLastUpdatedTime(systemDate);
            systemUser.setAttempt(0);
            systemUser.setUserRole(userRole);

            String encode = passwordEncoder.encode(userRequestDTO.getPassword());
            systemUser.setPassword(encode);

            userRepository.save(systemUser);
            return responseGenerator.generateSuccessResponse(userRequestDTO, HttpStatus.OK,
                    ResponseCode.USER_SAVED_SUCCESS, MessageConstant.USER_SUCCESSFULLY_SAVE, locale, new Object[] {userRequestDTO.getUsername()});
        }
        catch (EntityNotFoundException ex) {
            log.info(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Object> editUser(UserRequestDTO userRequestDTO, Locale locale) {
        try{
            SystemUser systemUser = Optional.ofNullable(userRepository.findByEmailAndStatusNotAndIdNot(userRequestDTO.getEmail(), Status.deleted,userRequestDTO.getId()))
                    .orElse(null);
            if (Objects.nonNull(systemUser)) {
                return responseGenerator
                        .generateErrorResponse(userRequestDTO, HttpStatus.CONFLICT,
                                ResponseCode.ALREADY_EXIST ,  MessageConstant.USER_EMAIL_ALREADY_EXIST, new Object[] {userRequestDTO.getEmail()},
                                locale);
            }

            systemUser = Optional.ofNullable(userRepository.findByNicAndStatusNotAndIdNot(userRequestDTO.getNic(), Status.deleted,userRequestDTO.getId()))
                    .orElse(null);
            if (Objects.nonNull(systemUser)) {
                return responseGenerator
                        .generateErrorResponse(userRequestDTO, HttpStatus.CONFLICT,
                                ResponseCode.ALREADY_EXIST ,  MessageConstant.USER_NIC_ALREADY_EXIST, new Object[] {userRequestDTO.getNic()},
                                locale);
            }

            systemUser = Optional.ofNullable(userRepository.findByMobileNoAndStatusNotAndIdNot(userRequestDTO.getMobileNo(), Status.deleted,userRequestDTO.getId()))
                    .orElse(null);
            if (Objects.nonNull(systemUser)) {
                return responseGenerator
                        .generateErrorResponse(userRequestDTO, HttpStatus.CONFLICT,
                                ResponseCode.ALREADY_EXIST ,  MessageConstant.USER_MOBILE_ALREADY_EXIST, new Object[] {userRequestDTO.getMobileNo()},
                                locale);
            }

            UserRole userRole = Optional.ofNullable(userRoleRepository.findByCodeAndStatusCode(userRequestDTO.getUserRole(), Status.active)).orElse(null);
            if (Objects.isNull(userRole)) {
                return responseGenerator
                        .generateErrorResponse(userRequestDTO, HttpStatus.NOT_FOUND,
                                ResponseCode.NOT_FOUND ,  MessageConstant.USER_ROLE_NOT_FOUND,
                                locale);
            }

            systemUser = Optional.ofNullable(userRepository.findByIdAndStatusNot(userRequestDTO.getId(), Status.deleted))
                    .orElse(null);
            if (Objects.isNull(systemUser)) {
                return responseGenerator
                        .generateErrorResponse(userRequestDTO, HttpStatus.CONFLICT,
                                ResponseCode.NOT_FOUND ,  MessageConstant.USER_NOT_FOUND, new Object[] {userRequestDTO.getUsername()},
                                locale);
            }

            Date systemDate = new Date();
            systemUser.setLastUpdatedTime(systemDate);
            systemUser.setUserRole(userRole);

            DtoToEntityMapper.mapUser(systemUser, userRequestDTO);

            userRepository.save(systemUser);
            return responseGenerator.generateSuccessResponse(userRequestDTO, HttpStatus.OK,
                    ResponseCode.USER_UPDATE_SUCCESS, MessageConstant.USER_SUCCESSFULLY_UPDATE, locale, new Object[] {userRequestDTO.getUsername()});
        }
        catch (EntityNotFoundException ex) {
            log.info(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Object> deleteUser(UserRequestDTO userRequestDTO, Locale locale) {
        try{

            SystemUser systemUser = Optional.ofNullable(userRepository.findByIdAndStatusNot(userRequestDTO.getId(), Status.deleted))
                    .orElse(null);
            if (Objects.isNull(systemUser)) {
                return responseGenerator
                        .generateErrorResponse(userRequestDTO, HttpStatus.CONFLICT,
                                ResponseCode.NOT_FOUND ,  MessageConstant.USER_NOT_FOUND, new Object[] {userRequestDTO.getUsername()},
                                locale);
            }

            if (systemUser.getUserRole().getCode() == "consult") {
                Consultants consultants = Optional.ofNullable(consultantRepository.findConsultantsBySystemUserAndStatusNot(systemUser, Status.deleted)).orElse(null);
                if(Objects.nonNull(consultants)) {
                    return responseGenerator
                            .generateErrorResponse(userRequestDTO, HttpStatus.CONFLICT,
                                    ResponseCode.ALREADY_EXIST ,  MessageConstant.USER_EXIST_WITH_CONSULTOR, new Object[] {userRequestDTO.getUsername()},
                                    locale);
                }
            }

            if (systemUser.getUserRole().getCode() == "seeker") {
                List<Appointments> appointmentsList = Optional.ofNullable(appointmentRepository.findAppointmentsBySystemUserAndStatusNot(systemUser, Status.deleted)).orElse(null);
                if(Objects.nonNull(appointmentsList)) {
                    return responseGenerator
                            .generateErrorResponse(userRequestDTO, HttpStatus.CONFLICT,
                                    ResponseCode.ALREADY_EXIST ,  MessageConstant.USER_EXIST_WITH_APPOINTMENT, new Object[] {userRequestDTO.getUsername()},
                                    locale);
                }
            }

            systemUser.setStatus(Status.deleted);
            userRepository.save(systemUser);
            return responseGenerator.generateSuccessResponse(userRequestDTO, HttpStatus.OK,
                    ResponseCode.USER_DELETE_SUCCESS, MessageConstant.USER_SUCCESSFULLY_DELETE, locale, new Object[] {userRequestDTO.getUsername()});
        }
        catch (EntityNotFoundException ex) {
            log.info(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }
}
