package com.careerbooker.server.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Data
public class AppointmentsResponseDTO {
    private Long appointmentId;
    private Long consultantDaysId;
    private Long consultantId;
    private Long consultantUserName;
    private Long consultantSpecialize;
    private Date bookedDate;
    private String bookedSlot;
    private String consultantAvaStatus;
    private Long userId;
    private String userName;
    private String status;
    private String statusDescription;

}
