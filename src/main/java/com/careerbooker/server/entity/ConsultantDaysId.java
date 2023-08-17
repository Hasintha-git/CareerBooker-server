package com.careerbooker.server.entity;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class ConsultantDaysId implements Serializable {
    private Long consultant; // Change to the appropriate type
    private Long day;       // Change to the appropriate type
    private Long slot;      // Change to the appropriate type
}
