package com.careerbooker.server.entity;

import com.careerbooker.server.util.enums.Status;
import com.careerbooker.server.util.enums.TimeSlot;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable; // Import the Serializable interface
import java.util.Date;

@Data
@Entity
@Table(name = "Consultant_Days")
public class ConsultantDays extends CommonEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "consultant_id")
    private Consultants consultant;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "day_id")
    private Days days;

    @JoinColumn(name = "slot")
    @Enumerated(EnumType.STRING)
    private TimeSlot slot;

    @Column(name = "STATUS", nullable = false, length = 8)
    @Enumerated(EnumType.STRING)
    private Status status;
}
