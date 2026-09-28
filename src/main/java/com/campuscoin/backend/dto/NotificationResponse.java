package com.campuscoin.backend.dto;


import com.campuscoin.backend.entity.Notification;
import com.campuscoin.backend.enums.NotificationStatus;
import com.campuscoin.backend.enums.NotificationType;

import java.time.LocalDateTime;

public class NotificationResponse {

    private String notificationId;
    private String title;
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime validUntil;
    private NotificationType type;
    private String placement;
    private String category;
    private NotificationStatus status;
    private String recipientId;

    public NotificationResponse(Notification notification) {
        this.notificationId = notification.getNotificationId();
        this.title = notification.getTitle();
        this.message = notification.getMessage();
        this.createdAt = notification.getCreatedAt();
        this.validUntil = notification.getValidUntil();
        this.type = notification.getType();
        this.placement = notification.getPlacement();
        this.category = notification.getCategory();
        this.status = notification.getStatus();
        this.recipientId = notification.getRecipientId();
    }

    public String getNotificationId() {
        return notificationId;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getValidUntil() {
        return validUntil;
    }

    public NotificationType getType() {
        return type;
    }

    public String getPlacement() {
        return placement;
    }

    public String getCategory() {
        return category;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public String getRecipientId() {
        return recipientId;
    }
}
