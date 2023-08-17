package com.careerbooker.server.entity;

import com.careerbooker.server.util.enums.Status;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Table(name = "timeslots")
public class TimeSlots extends CommonEntity
{
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "slot_id", unique = true, nullable = false)
    private Long slot_id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_time", nullable = false)
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_time", nullable = false)
    private Date endTime;

    @Column(name = "STATUS", nullable = false, length = 8)
    @Enumerated(EnumType.STRING)
    private Status statusCode;

}
