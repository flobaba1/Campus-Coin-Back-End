package com.campuscoin.backend.service;

import com.campuscoin.backend.dto.AdminAuditLogResponse;
import com.campuscoin.backend.entity.AdminAuditLog;
import com.campuscoin.backend.repository.AdminAuditLogRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminAuditLogService {

    private final AdminAuditLogRepository auditLogRepository;

    public AdminAuditLogService(
            AdminAuditLogRepository auditLogRepository
    ) {
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void record(
            String action,
            String entityType,
            String entityId,
            String summary
    ) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getName() == null
                || authentication.getName().isBlank()) {

            throw new IllegalStateException(
                    "Authenticated admin not found"
            );
        }

        String adminId = authentication.getName();

        AdminAuditLog auditLog = new AdminAuditLog();

        auditLog.setAdminId(adminId);
        auditLog.setAction(action);
        auditLog.setEntityType(entityType);
        auditLog.setEntityId(entityId);
        auditLog.setSummary(summary);

        auditLogRepository.save(auditLog);
    }

    @Transactional(readOnly = true)
    public List<AdminAuditLogResponse> getRecentActivity() {

        return auditLogRepository
                .findTop4ByOrderByCreatedAtDesc()
                .stream()
                .map(log -> new AdminAuditLogResponse(
                        log.getAuditLogId(),
                        log.getAction(),
                        log.getSummary(),
                        log.getAdminId(),
                        log.getCreatedAt()
                ))
                .toList();
    }
}