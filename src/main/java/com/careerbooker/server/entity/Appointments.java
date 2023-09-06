package com.careerbooker.server.entity;

import com.careerbooker.server.util.enums.Status;
import lombok.Data;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Table(name = "appointments")
public class Appointments extends CommonEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "appointment_id", unique = true, nullable = false)
    private Long appointmentId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "consultant_day_id", nullable = false)
    private ConsultantDays consultantDays;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private SystemUser systemUser;

    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Status status;
}
