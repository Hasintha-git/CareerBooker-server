package com.careerbooker.server.repository;

import com.careerbooker.server.entity.Consultants;
import com.careerbooker.server.entity.SpecializationType;
import com.careerbooker.server.util.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultantRepository extends JpaRepository<Consultants, Long>, JpaSpecificationExecutor<Consultants> {
    List<Consultants> findAllByStatus(Status status);

    Consultants findByConsultantIdAndStatusNot(Long consultantId, Status status);
    Consultants findBySpecializations_SpecializationIdAndStatusNot(Long id, Status status);

}
