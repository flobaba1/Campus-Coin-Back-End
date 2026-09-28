package com.campuscoin.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "admin_audit_logs",
        indexes = {
                @Index(
                        name = "idx_audit_created_at",
                        columnList = "created_at"
                ),
                @Index(
                        name = "idx_audit_admin_id",
                        columnList = "admin_id"
                )
        }
)
public class AdminAuditLog {

    @Id
    @Column(name = "audit_log_id", nullable = false, updatable = false)
    private String auditLogId;

    @Column(name = "admin_id", nullable = false, updatable = false)
    private String adminId;

    @Column(name = "action", nullable = false, updatable = false)
    private String action;

    @Column(name = "entity_type", nullable = false, updatable = false)
    private String entityType;

    @Column(name = "entity_id", updatable = false)
    private String entityId;

    @Column(name = "summary", nullable = false, length = 500, updatable = false)
    private String summary;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public AdminAuditLog() {
    }

    @PrePersist
    public void prePersist() {
        if (auditLogId == null) {
            auditLogId = UUID.randomUUID().toString();
        }

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public String getAuditLogId() {
        return auditLogId;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}