package com.campuscoin.backend.repository;

import com.campuscoin.backend.entity.Notification;
import com.campuscoin.backend.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository
        extends JpaRepository<Notification, String> {

    List<Notification> findAllByOrderByCreatedAtDesc();

    List<Notification> findByRecipientIdOrderByCreatedAtDesc(
            String recipientId
    );

    List<Notification> findByTypeOrderByCreatedAtDesc(
            NotificationType type
    );
}
