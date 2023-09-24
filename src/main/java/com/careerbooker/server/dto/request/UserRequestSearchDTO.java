package com.careerbooker.server.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserRequestSearchDTO {
    private String userName;
    private String mobileNo;
    private String status;
    private String nic;
    private String email;
    private String userRole;
}
