package com.careerbooker.server.dto;

import com.careerbooker.server.util.enums.TimeSlot;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SlotDto {

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Colombo")
    private Date day;

    private List<TimeSlot> timeSlots;
}
