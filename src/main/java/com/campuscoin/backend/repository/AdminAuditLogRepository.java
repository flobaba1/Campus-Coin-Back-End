package com.campuscoin.backend.repository;

import com.campuscoin.backend.entity.AdminAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminAuditLogRepository
        extends JpaRepository<AdminAuditLog, String> {

    List<AdminAuditLog> findTop4ByOrderByCreatedAtDesc();
}