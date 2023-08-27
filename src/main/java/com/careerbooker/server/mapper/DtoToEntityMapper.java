package com.careerbooker.server.mapper;


import com.careerbooker.server.dto.request.ConsultantsDTO;
import com.careerbooker.server.dto.request.SpecializationDTO;
import com.careerbooker.server.dto.request.UserRequestDTO;
import com.careerbooker.server.dto.request.UserRoleDTO;
import com.careerbooker.server.entity.Consultants;
import com.careerbooker.server.entity.SpecializationType;
import com.careerbooker.server.entity.SystemUser;
import com.careerbooker.server.entity.UserRole;
import com.careerbooker.server.util.enums.Status;

import java.util.Objects;

public class DtoToEntityMapper {
    private DtoToEntityMapper() {

    }
    public static void mapUser(SystemUser systemUser, UserRequestDTO userRequestDTO) {
        systemUser.setStatus(Status.valueOf(userRequestDTO.getStatusCode()));
        systemUser.setPwStatus(Status.valueOf(userRequestDTO.getPwStatus()));
        if (Objects.nonNull(userRequestDTO.getPasswordExpireDate())) {
            systemUser.setPasswordExpireDate(userRequestDTO.getPasswordExpireDate());
        }
        systemUser.setLastUpdatedUser(userRequestDTO.getLastUpdatedUser());
        systemUser.setAttempt(0);
        systemUser.setEmail(userRequestDTO.getEmail());
        systemUser.setMobileNo(userRequestDTO.getMobileNo());
        systemUser.setFullName(userRequestDTO.getFullName());
        systemUser.setAddress(userRequestDTO.getAddress());
        systemUser.setCity(userRequestDTO.getCity());
    }

    public static void mapSpecialization(SpecializationType specializationType, SpecializationDTO specializationDTO) {
        specializationType.setStatus(Status.valueOf(specializationDTO.getStatusCode()));
        specializationType.setName(specializationDTO.getName());
        if (Objects.nonNull(specializationDTO.getId())) {
            specializationType.setSpecializationId(specializationDTO.getId());

        }
    }

    public static void mapUserRole(UserRole userRole, UserRoleDTO userRoleDTO) {
        userRole.setStatusCode(Status.valueOf(userRoleDTO.getStatus()));
        userRole.setCode(userRoleDTO.getCode());
        userRole.setDescription(userRoleDTO.getDescription());
    }

    public static void mapConsultant(Consultants consultants, ConsultantsDTO consultantsDTO) {
        consultants.setStatus(Status.valueOf(consultantsDTO.getStatus()));
    }
}
