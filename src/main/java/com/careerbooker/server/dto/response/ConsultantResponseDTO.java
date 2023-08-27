package com.careerbooker.server.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ConsultantResponseDTO {
    private Long id;
    private Long spe_id;
    private String name;
    private String status;
    private String statusDescription;

    public ConsultantResponseDTO(Long id) {
        this.id = id;
    }
}
