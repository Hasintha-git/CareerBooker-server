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
public class AppointmentSearchDTO {

    private String consultantName;

    private Long specialization;

    private String userName;

    private String status;

    private Date bookedDate;

    private String slotId;

}
