package com.campuscoin.backend.dto;


import com.campuscoin.backend.enums.NotificationStatus;
import com.campuscoin.backend.enums.NotificationType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class CreateNotificationRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    private String title;

    @NotBlank(message = "Message is required")
    private String message;

    @NotNull(message = "Valid until is required")
    @Future(message = "Valid until must be a future date")
    private LocalDateTime validUntil;

    private NotificationType type;

    @NotBlank(message = "Placement is required")
    @Size(max = 100, message = "Placement cannot exceed 100 characters")
    private String placement;

    @Size(max = 100, message = "Category cannot exceed 100 characters")
    private String category;

    private NotificationStatus status;

    @Size(max = 36, message = "Recipient ID cannot exceed 36 characters")
    private String recipientId;

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setValidUntil(LocalDateTime validUntil) {
        this.validUntil = validUntil;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public void setPlacement(String placement) {
        this.placement = placement;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }
}
