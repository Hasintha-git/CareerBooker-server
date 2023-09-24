package com.careerbooker.server.dto.request;

import com.careerbooker.server.validators.InsertValidation;
import com.careerbooker.server.validators.UpdateValidation;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@NoArgsConstructor
@Data
public class AppointmentDTO {
    @NotNull(message = "Appointment id required", groups = {UpdateValidation.class })
    private Long appointmentId;

    @NotNull(message = "Consultant id required", groups = { InsertValidation.class, UpdateValidation.class })
    private Long consultantId;

    @NotNull(message = "User Id required", groups = {  InsertValidation.class, UpdateValidation.class })
    private Long userId;

    @NotBlank(message = "Status required", groups = {  InsertValidation.class, UpdateValidation.class })
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Colombo")
    private Date bookedDate;

    @NotBlank(message = "Time slot required", groups = {  InsertValidation.class, UpdateValidation.class })
    private String slotId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date createdTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date lastUpdatedTime;

    private String createdUser;

    private String lastUpdatedUser;

    @NotBlank(message = "Active User name required", groups = {  InsertValidation.class, UpdateValidation.class })
    private String activeUserName;

    private int pageNumber;
    private int pageSize;
    private String sortColumn;
    private String sortDirection;

    private AppointmentSearchDTO appointmentSearchDTO;
}
