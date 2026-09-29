package com.campuscoin.backend.dto;

import java.time.LocalDateTime;

public class AdminAuditLogResponse {

    private String auditLogId;
    private String action;
    private String summary;
    private String actorName;
    private LocalDateTime createdAt;

    public AdminAuditLogResponse(
            String auditLogId,
            String action,
            String summary,
            String actorName,
            LocalDateTime createdAt
    ) {
        this.auditLogId = auditLogId;
        this.action = action;
        this.summary = summary;
        this.actorName = actorName;
        this.createdAt = createdAt;
    }

    public String getAuditLogId() {
        return auditLogId;
    }

    public String getAction() {
        return action;
    }

    public String getSummary() {
        return summary;
    }

    public String getActorName() {
        return actorName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}