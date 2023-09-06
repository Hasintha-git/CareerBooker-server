package com.careerbooker.server.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ConsultantDaysResponseDTO {
    private Long id;
    private Long consultant_id;
    private Long spe_id;
    private String speDescription;
    private DaysList availability;
    private String status;
    private String statusDescription;


}
