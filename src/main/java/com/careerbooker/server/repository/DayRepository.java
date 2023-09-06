package com.careerbooker.server.repository;

import com.careerbooker.server.entity.Days;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface DayRepository extends JpaRepository<Days, Long>, JpaSpecificationExecutor<Days> {
    Days findByDate(Date date);
}
