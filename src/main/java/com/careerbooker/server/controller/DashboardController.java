package com.careerbooker.server.controller;

import com.careerbooker.server.dto.request.SpecializationDTO;
import com.careerbooker.server.service.DashboardService;
import com.careerbooker.server.service.SpecializationService;
import com.careerbooker.server.util.EndPoint;
import com.careerbooker.server.validators.*;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/dashboard")
@Log4j2
@CrossOrigin
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DashboardController {

    private DashboardService dashboardService;

    @GetMapping(value = EndPoint.DASHBOARD_DATA_GET)
    public ResponseEntity<Object> getReferenceData() {
        log.debug("Received Dashboard Reference Data Request {} -");
        return ResponseEntity.status(HttpStatus.OK).body(dashboardService.getReferenceData());
    }
}
