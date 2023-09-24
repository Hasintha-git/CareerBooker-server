package com.careerbooker.server.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class DaysList {
    private List<DaySlot> daysLists = new ArrayList<>();

    @Data
    @NoArgsConstructor
    public static class DaySlot {
        private Date day;
        private List<String> slot = new ArrayList<>();
    }
}
