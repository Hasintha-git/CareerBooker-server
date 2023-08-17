package com.careerbooker.server.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable; // Import the Serializable interface

@Data
@Entity
@Table(name = "Consultant_Days")
public class ConsultantDays extends CommonEntity implements Serializable { // Implement Serializable
    @EmbeddedId // Use EmbeddedId for composite primary key
    private ConsultantDaysId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "consultant_id", insertable = false, updatable = false)
    private Consultants consultant;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "day_id", insertable = false, updatable = false)
    private Days day;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "slot_id", insertable = false, updatable = false)
    private TimeSlots slot;
}
