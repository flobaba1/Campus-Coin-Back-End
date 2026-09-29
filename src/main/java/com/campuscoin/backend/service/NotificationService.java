package com.campuscoin.backend.service;

import com.campuscoin.backend.dto.NotificationResponse;
import com.campuscoin.backend.entity.Notification;
import com.campuscoin.backend.enums.NotificationStatus;
import com.campuscoin.backend.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(
            NotificationRepository notificationRepository
    ) {
        this.notificationRepository =
                notificationRepository;
    }

    @Transactional
    public List<NotificationResponse> getNotifications(
            String userId
    ) {

        return notificationRepository
                .findVisibleNotifications(userId)
                .stream()
                .map(NotificationResponse::new)
                .toList();
    }

    @Transactional
    public NotificationResponse markAsRead(
            String userId,
            String notificationId
    ) {

        Notification notification =
                notificationRepository
                        .findById(notificationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification not found"
                                )
                        );

        /*
         * General notifications can be viewed by everyone,
         * but we don't change their global status here because
         * that would mark them read for every student.
         */
        if (notification.getRecipientId() == null) {
            return new NotificationResponse(
                    notification
            );
        }

        if (!notification.getRecipientId().equals(userId)) {
            throw new RuntimeException(
                    "Notification does not belong to this user"
            );
        }

        notification.update(
                notification.getTitle(),
                notification.getMessage(),
                notification.getValidUntil(),
                notification.getType(),
                notification.getPlacement(),
                notification.getCategory(),
                NotificationStatus.READ,
                notification.getRecipientId()
        );

        Notification saved =
                notificationRepository.save(
                        notification
                );

        return new NotificationResponse(saved);
    }
}
