package com.careerbooker.server.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Data
public class SlotList {
    private List<String> slot;
}
