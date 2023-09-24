package com.careerbooker.server.mapper;


import com.careerbooker.server.dto.SimpleBaseDTO;
import com.careerbooker.server.dto.request.ConsultantsDTO;
import com.careerbooker.server.dto.response.*;
import com.careerbooker.server.entity.*;
import com.careerbooker.server.util.enums.ClientStatusEnum;

import java.util.*;

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
        userResponseDTO.setUserRole(systemUser.getUserRole().getCode());
        userResponseDTO.setUserRoleDescription(systemUser.getUserRole().getDescription());

        if (Objects.nonNull(systemUser.getPasswordExpireDate())) {
            userResponseDTO.setPasswordExpireDate(systemUser.getPasswordExpireDate());
        }

        if (Objects.nonNull(systemUser.getLastLoggedDate())) {
            userResponseDTO.setLastLoggedDate(systemUser.getLastLoggedDate());
        }

        if (Objects.nonNull(systemUser.getCreatedUser())) {
            userResponseDTO.setCreatedUser(systemUser.getCreatedUser());
        }

        if (Objects.nonNull(systemUser.getCreatedTime())) {
            userResponseDTO.setCreatedTime(systemUser.getCreatedTime());
        }

        if (Objects.nonNull(systemUser.getLastUpdatedUser())) {
            userResponseDTO.setLastUpdatedUser(systemUser.getLastUpdatedUser());
        }

        if (Objects.nonNull(systemUser.getLastUpdatedTime())) {
            userResponseDTO.setLastUpdatedTime(systemUser.getLastUpdatedTime());
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
        if(Objects.nonNull(specializationType.getCreatedUser())) {
            responseDTO.setCreatedUser(specializationType.getCreatedUser());
        }
        if(Objects.nonNull(specializationType.getModifiedUser())) {
            responseDTO.setLastUpdatedUser(specializationType.getModifiedUser());
        }
        responseDTO.setCreatedTime(specializationType.getCreatedDate());
        responseDTO.setLastUpdatedTime(specializationType.getModifiedDate());
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
        responseDTO.setCon_id(consultants.getConsultantId());
        responseDTO.setSpe_id(consultants.getSpecializations().getSpecializationId());
        responseDTO.setSpeDescription(consultants.getSpecializations().getName());
        if (Objects.nonNull(consultants.getSystemUser())) {
            responseDTO.setUsername(consultants.getSystemUser().getUsername());
            responseDTO.setEmail(consultants.getSystemUser().getEmail());
            responseDTO.setStatus(String.valueOf(consultants.getSystemUser().getStatus()));
            responseDTO.setCity(consultants.getSystemUser().getCity());
            responseDTO.setNic(consultants.getSystemUser().getNic());
            responseDTO.setDateOfBirth(consultants.getSystemUser().getDateOfBirth());
            responseDTO.setFullName(consultants.getSystemUser().getFullName());
            responseDTO.setMobileNo(consultants.getSystemUser().getMobileNo());
            responseDTO.setAddress(consultants.getSystemUser().getAddress());
            responseDTO.setUserRole(consultants.getSystemUser().getUserRole().getCode());
            responseDTO.setUserRoleDescription(consultants.getSystemUser().getUserRole().getDescription());

        }
        return responseDTO;
    }

    public static DaysList mapConsultantDays(List<ConsultantDays> consultantDays) {
        DaysList daysList = new DaysList();

        // Create a list to hold the day-slot pairs
        List<DaysList.DaySlot> daySlots = new ArrayList<>();

        for (ConsultantDays consultantDay : consultantDays) {
            if (consultantDay.getDays() != null) {
                Date date = consultantDay.getDays().getDay();
                String slotCode = consultantDay.getSlot().getCode();

                // Check if there is an existing DaySlot for this date
                DaysList.DaySlot existingDaySlot = null;
                for (DaysList.DaySlot daySlot : daySlots) {
                    if (daySlot.getDay().equals(date)) {
                        existingDaySlot = daySlot;
                        break;
                    }
                }

                // If there is an existing DaySlot, add the slotCode to it
                if (existingDaySlot != null) {
                    existingDaySlot.getSlot().add(slotCode);
                } else {
                    // Otherwise, create a new DaySlot and add it to the list
                    DaysList.DaySlot newDaySlot = new DaysList.DaySlot();
                    newDaySlot.setDay(date);
                    newDaySlot.getSlot().add(slotCode);
                    daySlots.add(newDaySlot);
                }
            }
        }

        // Set the generated daySlots list in the daysList object
        daysList.setDaysLists(daySlots);

        return daysList;
    }


    public static AppointmentsResponseDTO mapAppointment(Appointments appointments) {
        AppointmentsResponseDTO responseDTO = new AppointmentsResponseDTO();
        responseDTO.setAppointmentId(appointments.getAppointmentId());
        responseDTO.setConsultantDaysId(appointments.getConsultantDays().getId());
        responseDTO.setConsultantId(appointments.getConsultantDays().getConsultant().getConsultantId());
        responseDTO.setStatus(String.valueOf(appointments.getStatus()));
        responseDTO.setStatusDescription(ClientStatusEnum.getEnum(String.valueOf(appointments.getStatus())).getDescription());
        responseDTO.setBookedSlot(appointments.getConsultantDays().getSlot().getDescription());
        responseDTO.setBookedDate(appointments.getConsultantDays().getDays().getDay());
        responseDTO.setUserName(appointments.getSystemUser().getUsername());
        responseDTO.setConsultantUserName(appointments.getConsultantDays().getConsultant().getSystemUser().getUsername());
        responseDTO.setConsultantSpecialize(appointments.getConsultantDays().getConsultant().getSpecializations().getName());

        return responseDTO;
    }

}
