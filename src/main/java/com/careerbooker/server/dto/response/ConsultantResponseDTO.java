package com.careerbooker.server.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Data
public class ConsultantResponseDTO {
    private Long con_id;
    private Long spe_id;
    private String speDescription;
    private String status;
    private String statusDescription;

    //    user details
    private String username;
    private String userRole;
    private String userRoleDescription;
    private String fullName;
    private String nic;
    private String email;
    private String mobileNo;
    private Date dateOfBirth;
    private String address;
    private String city;

    private String createdUser;
    private String lastUpdatedUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date createdTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date lastUpdatedTime;

    public ConsultantResponseDTO(Long id) {
        this.con_id = id;
    }
}
