package com.careerbooker.server.dto.response;

import com.careerbooker.server.entity.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Data
public class SpecializationResponseDTO {
    private Long id;
    private String name;
    private String status;
    private String statusDescription;

    public SpecializationResponseDTO(Long id) {
        this.id = id;
    }

    public SpecializationResponseDTO(String username) {
        this.name = username;
    }
}
