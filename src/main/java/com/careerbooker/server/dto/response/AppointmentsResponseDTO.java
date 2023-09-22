package com.careerbooker.server.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Data
public class AppointmentsResponseDTO {
    private Long appointmentId;
    private Long consultantDaysId;
    private Long consultantId;
    private String consultantUserName;
    private String consultantSpecialize;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Colombo")
    private Date bookedDate;
    private String bookedSlot;
    private String userName;
    private String status;
    private String statusDescription;

    private String createdUser;
    private String lastUpdatedUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date createdTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date lastUpdatedTime;
}
