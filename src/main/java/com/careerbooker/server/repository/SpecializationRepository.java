package com.careerbooker.server.repository;

import com.careerbooker.server.entity.SpecializationType;
import com.careerbooker.server.util.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecializationRepository  extends JpaRepository<SpecializationType, Long>, JpaSpecificationExecutor<SpecializationType> {
    List<SpecializationType> findAllByStatus(Status status);

    SpecializationType findBySpecializationIdAndStatusNot(Long id, Status status);

}
