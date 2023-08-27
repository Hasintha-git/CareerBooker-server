package com.careerbooker.server.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ConsultantSearchDTO {
    private String name;

    private String spe_id;

    private String statusCode;
}
