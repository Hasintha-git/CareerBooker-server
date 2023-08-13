package com.careerbooker.server.dto.response;

import com.careerbooker.server.entity.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Data
public class UserResponseDTO {
    private Long id;
    private String username;
    private UserRole userRole;
    private String fullName;
    private String nic;
    private String email;
    private String mobileNo;
    private Date dateOfBirth;
    private String address;
    private String city;
    private String status;
    private String pwStatus;
    private String statusDescription;
    private String passwordStatusDescription;
    private int attempt;
    private Date lastLoggedDate;
    private Date passwordExpireDate;

    public UserResponseDTO(Long id) {
        this.id = id;
    }

    public UserResponseDTO(String username) {
        this.username = username;
    }
}
