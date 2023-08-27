package com.careerbooker.server.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ConsultantSearchDTO {
    private String nic;

    private String userName;

    private String spe_id;

    private String statusCode;
}
