package com.careerbooker.server.dto.response;

import com.careerbooker.server.entity.UserRole;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    private String createdUser;
    private String lastUpdatedUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date createdTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date lastUpdatedTime;

    public SpecializationResponseDTO(Long id) {
        this.id = id;
    }

    public SpecializationResponseDTO(String username) {
        this.name = username;
    }
}
