package com.careerbooker.server.mapper;


import com.careerbooker.server.dto.SimpleBaseDTO;
import com.careerbooker.server.dto.request.ConsultantsDTO;
import com.careerbooker.server.dto.response.ConsultantResponseDTO;
import com.careerbooker.server.dto.response.SpecializationResponseDTO;
import com.careerbooker.server.dto.response.UserResponseDTO;
import com.careerbooker.server.dto.response.UserRoleResponseDTO;
import com.careerbooker.server.entity.Consultants;
import com.careerbooker.server.entity.SpecializationType;
import com.careerbooker.server.entity.SystemUser;
import com.careerbooker.server.entity.UserRole;
import com.careerbooker.server.util.enums.ClientStatusEnum;

import java.util.Objects;

public class EntityToDtoMapper {
    private EntityToDtoMapper() {

    }
    public static UserResponseDTO mapUser(SystemUser systemUser) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(systemUser.getId());
        userResponseDTO.setUsername(systemUser.getUsername());
        userResponseDTO.setEmail(systemUser.getEmail());
        userResponseDTO.setStatus(String.valueOf(systemUser.getStatus()));
        userResponseDTO.setPwStatus(String.valueOf(systemUser.getPwStatus()));
        userResponseDTO.setCity(systemUser.getCity());
        userResponseDTO.setNic(systemUser.getNic());
        userResponseDTO.setDateOfBirth(systemUser.getDateOfBirth());
        userResponseDTO.setFullName(systemUser.getFullName());
        userResponseDTO.setMobileNo(systemUser.getMobileNo());
        userResponseDTO.setAddress(systemUser.getAddress());

        if (Objects.nonNull(systemUser.getPasswordExpireDate())) {
            userResponseDTO.setPasswordExpireDate(systemUser.getPasswordExpireDate());
        }

        if (Objects.nonNull(systemUser.getLastLoggedDate())) {
            userResponseDTO.setLastLoggedDate(systemUser.getLastLoggedDate());
        }
        return userResponseDTO;
    }

    public static SimpleBaseDTO mapUserRoleDropdown(SimpleBaseDTO simpleBaseDTO, UserRole userRole) {
        simpleBaseDTO.setCode(userRole.getCode());
        simpleBaseDTO.setDescription(userRole.getDescription());
        return simpleBaseDTO;
    }

    public static SpecializationResponseDTO mapSpecialization(SpecializationType specializationType) {
        SpecializationResponseDTO responseDTO = new SpecializationResponseDTO();
        responseDTO.setStatus(String.valueOf(specializationType.getStatus()));
        responseDTO.setStatusDescription(ClientStatusEnum.getEnum(String.valueOf(specializationType.getStatus())).getDescription());
        responseDTO.setName(specializationType.getName());
        responseDTO.setId(specializationType.getSpecializationId());
        return responseDTO;
    }

    public static SimpleBaseDTO mapSpecializationDropdown(SimpleBaseDTO simpleBaseDTO, SpecializationType specializationType) {
        simpleBaseDTO.setId(specializationType.getSpecializationId());
        simpleBaseDTO.setDescription(specializationType.getName());
        return simpleBaseDTO;
    }

    public static UserRoleResponseDTO mapUserRole(UserRole userRole) {
        UserRoleResponseDTO responseDTO = new UserRoleResponseDTO();
        responseDTO.setStatus(String.valueOf(userRole.getStatusCode()));
        responseDTO.setStatusDescription(ClientStatusEnum.getEnum(String.valueOf(userRole.getStatusCode())).getDescription());
        responseDTO.setCode(userRole.getCode());
        responseDTO.setDescription(userRole.getDescription());
        responseDTO.setId(userRole.getId());
        return responseDTO;
    }

    public static ConsultantResponseDTO mapConsultant(Consultants consultants) {
        ConsultantResponseDTO responseDTO = new ConsultantResponseDTO();
        responseDTO.setStatus(String.valueOf(consultants.getStatus()));
        responseDTO.setStatusDescription(ClientStatusEnum.getEnum(String.valueOf(consultants.getStatus())).getDescription());
        responseDTO.setName(consultants.getName());
        responseDTO.setId(consultants.getConsultant_id());
        responseDTO.setSpe_id(consultants.getSpecializations().getSpecializationId());
        return responseDTO;
    }

}
