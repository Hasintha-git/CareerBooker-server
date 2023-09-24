package com.careerbooker.server.dto.request;

import com.careerbooker.server.dto.SlotDto;
import com.careerbooker.server.validators.DeleteValidation;
import com.careerbooker.server.validators.FindValidation;
import com.careerbooker.server.validators.InsertValidation;
import com.careerbooker.server.validators.UpdateValidation;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Data
public class TimeSlotDTO {

    @NotNull(message = "Id required", groups = { FindValidation.class, DeleteValidation.class,
            UpdateValidation.class })
    private Long id;

    @NotNull(message = "Consultant id required", groups = { FindValidation.class, DeleteValidation.class,
            UpdateValidation.class })
    private Long con_id;

    @NotBlank(message = "Status required", groups = {   UpdateValidation.class })
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Colombo")
    private Date day;

    private List<SlotDto> slotDtoList;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date createdTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date lastUpdatedTime;

    private String createdUser;

    private String lastUpdatedUser;
    private String activeUserName;

    private int pageNumber;
    private int pageSize;
    private String sortColumn;
    private String sortDirection;

}
