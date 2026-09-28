package com.campuscoin.backend.entity;

import com.campuscoin.backend.enums.NotificationStatus;
import com.campuscoin.backend.enums.NotificationType;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @Column(name = "notification_id", length = 36)
    private String notificationId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "valid_until", nullable = false)
    private LocalDateTime validUntil;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private NotificationType type;

    @Column(name = "placement", nullable = false, length = 100)
    private String placement;

    @Column(name = "category", length = 100)
    private String category;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private NotificationStatus status;

    @Column(name = "recipient_id", length = 36)
    private String recipientId;

    protected Notification() {
    }

    public Notification(
            String title,
            String message,
            LocalDateTime validUntil,
            NotificationType type,
            String placement,
            String category,
            NotificationStatus status,
            String recipientId
    ) {
        this.notificationId = UUID.randomUUID().toString();
        this.title = title;
        this.message = message;
        this.createdAt = LocalDateTime.now();
        this.validUntil = validUntil;
        this.type = type;
        this.placement = placement;
        this.category = category;
        this.status = status != null
                ? status
                : NotificationStatus.UNREAD;
        this.recipientId = recipientId;
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

    public void update(
            String title,
            String message,
            LocalDateTime validUntil,
            NotificationType type,
            String placement,
            String category,
            NotificationStatus status,
            String recipientId
    ) {
        this.title = title;
        this.message = message;
        this.validUntil = validUntil;
        this.type = type;
        this.placement = placement;
        this.category = category;
        this.status = status;
        this.recipientId = recipientId;
    }
}
