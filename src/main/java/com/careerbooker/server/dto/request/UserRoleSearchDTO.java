package com.careerbooker.server.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserRoleSearchDTO {
    private String description;
    private String statusCode;
}
