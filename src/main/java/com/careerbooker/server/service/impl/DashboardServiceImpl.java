package com.careerbooker.server.service.impl;

import com.careerbooker.server.dto.SimpleBaseDTO;
import com.careerbooker.server.entity.SpecializationType;
import com.careerbooker.server.mapper.EntityToDtoMapper;
import com.careerbooker.server.repository.AppointmentRepository;
import com.careerbooker.server.repository.specifications.ConsultantSpecification;
import com.careerbooker.server.service.DashboardService;
import com.careerbooker.server.util.enums.ClientStatusEnum;
import com.careerbooker.server.util.enums.Status;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DashboardServiceImpl implements DashboardService {

    private AppointmentRepository appointmentRepository;

    @Override
    public Object getReferenceData() {
        try {
            Map<String, Object> refData = new HashMap<>();

            //get status
            List<SimpleBaseDTO> defaultStatus = Stream.of(ClientStatusEnum.values()).map(statusEnum -> new SimpleBaseDTO(statusEnum.getCode(), statusEnum.getDescription())).collect(Collectors.toList());
            //get user role list
            Long pendingData = appointmentRepository.countAllByStatus(Status.pending);
            Long cancelData = appointmentRepository.countAllByStatus(Status.cancel);
            Long completedData = appointmentRepository.countAllByStatus(Status.complete);


            //set data
            refData.put("pendingData", pendingData);
            refData.put("cancelData", cancelData);
            refData.put("completedData", completedData);

            return refData;

        } catch (Exception e) {
            log.error("Exception : ", e);
            throw e;
        }
    }
}
