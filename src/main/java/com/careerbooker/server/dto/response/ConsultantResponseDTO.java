package com.careerbooker.server.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Data
public class ConsultantResponseDTO {
    private Long id;
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
    public ConsultantResponseDTO(Long id) {
        this.id = id;
    }
}
