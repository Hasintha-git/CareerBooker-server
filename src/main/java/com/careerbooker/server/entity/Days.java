package com.careerbooker.server.entity;

import com.careerbooker.server.util.enums.Status;
import lombok.Data;

import javax.persistence.*;

import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Table(name = "days")
public class Days
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "day_id", unique = true, nullable = false)
    private Long day_id;

    @Column(name = "day", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(name = "STATUS", nullable = false, length = 8)
    @Enumerated(EnumType.STRING)
    private Status statusCode;
    
}
