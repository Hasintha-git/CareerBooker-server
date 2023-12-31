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
public class ConsultantsDTO {
    @NotNull(message = "Specialization id required", groups = {UpdateValidation.class })
    private Long spe_id;

    @NotNull(message = "Consultant id required", groups = { FindValidation.class, DeleteValidation.class,
            UpdateValidation.class })
    private Long con_id;

    @NotNull(message = "User Id required", groups = {  InsertValidation.class, UpdateValidation.class })
    private Long userId;

    @NotBlank(message = "Status required", groups = {  InsertValidation.class, UpdateValidation.class })
    private String status;

    private List<SlotDto> slotDtoList;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date createdTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date lastUpdatedTime;

    private String activeUserName;
    private String createdUser;

    private String lastUpdatedUser;

    private int pageNumber;
    private int pageSize;
    private String sortColumn;
    private String sortDirection;

    private ConsultantSearchDTO consultantSearchDTO;
}
