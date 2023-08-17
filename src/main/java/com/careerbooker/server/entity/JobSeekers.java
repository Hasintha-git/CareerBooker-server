package com.careerbooker.server.entity;

import com.careerbooker.server.util.enums.Status;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Table(name = "jobseekers")
public class JobSeekers extends CommonEntity
{
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "job_seeker_id", unique = true, nullable = false)
    private Long job_seeker_id;

    @Column(name = "name", nullable = false, length = 128)
    private String name;

    @Column(name = "EMAIL", nullable = false, length = 128)
    private String email;

    @Column(name = "phone", nullable = false, length = 16)
    private String mobileNo;

    @Column(name = "STATUS", nullable = false, length = 8)
    @Enumerated(EnumType.STRING)
    private Status statusCode;
    
}
