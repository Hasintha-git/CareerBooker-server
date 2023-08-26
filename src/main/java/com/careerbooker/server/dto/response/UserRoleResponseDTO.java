package com.careerbooker.server.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserRoleResponseDTO {
    private Long id;
    private String code;
    private String description;
    private String status;
    private String statusDescription;

    public UserRoleResponseDTO(Long id) {
        this.id = id;
    }

}
