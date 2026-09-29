package com.campuscoin.backend.repository;

import com.campuscoin.backend.entity.Notification;
import com.campuscoin.backend.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("""
        SELECT n
        FROM Notification n
        WHERE
            (n.recipientId = :userId OR n.recipientId IS NULL)
            AND n.validUntil > CURRENT_TIMESTAMP
        ORDER BY n.createdAt DESC
    """)
    List<Notification> findVisibleNotifications(
            @Param("userId") String userId
    );
}