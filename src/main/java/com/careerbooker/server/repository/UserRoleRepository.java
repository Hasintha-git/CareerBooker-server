package com.careerbooker.server.repository;

import com.careerbooker.server.entity.UserRole;
import com.careerbooker.server.util.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByCode(String code);
    List<UserRole> findAllByStatusCodeNot(Status status);

    UserRole findByCodeAndStatusCode(String code, Status status);

}
