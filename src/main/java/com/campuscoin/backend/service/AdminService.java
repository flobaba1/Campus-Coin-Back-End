package com.campuscoin.backend.service;

import com.campuscoin.backend.dto.CreateNotificationRequest;
import com.campuscoin.backend.dto.NotificationResponse;
import com.campuscoin.backend.dto.UpdateNotificationRequest;
import com.campuscoin.backend.dto.UserResponse;
import com.campuscoin.backend.entity.Notification;
import com.campuscoin.backend.entity.User;
import com.campuscoin.backend.enums.UserStatus;
import com.campuscoin.backend.repository.NotificationRepository;
import com.campuscoin.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {

    private final NotificationRepository notificationRepository;

    private final UserRepository userRepository;

    public AdminService(
            NotificationRepository notificationRepository, UserRepository userRepository
    ) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public NotificationResponse createNotification(
            CreateNotificationRequest request
    ) {

        Notification notification = new Notification(
                request.getTitle(),
                request.getMessage(),
                request.getValidUntil(),
                request.getType(),
                request.getPlacement(),
                request.getCategory(),
                request.getStatus(),
                request.getRecipientId()
        );

        Notification savedNotification =
                notificationRepository.save(notification);

        return new NotificationResponse(savedNotification);
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotifications() {

        return notificationRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(NotificationResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public NotificationResponse getNotification(
            String notificationId
    ) {

        Notification notification = notificationRepository
                .findById(notificationId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Notification not found"
                        )
                );

        return new NotificationResponse(notification);
    }

    @Transactional
    public NotificationResponse updateNotification(
            String notificationId,
            UpdateNotificationRequest request
    ) {

        Notification notification = notificationRepository
                .findById(notificationId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Notification not found"
                        )
                );

        notification.update(
                request.getTitle(),
                request.getMessage(),
                request.getValidUntil(),
                request.getType(),
                request.getPlacement(),
                request.getCategory(),
                request.getStatus(),
                request.getRecipientId()
        );

        Notification updatedNotification =
                notificationRepository.save(notification);

        return new NotificationResponse(updatedNotification);
    }

    @Transactional
    public void deleteNotification(String notificationId) {

        Notification notification = notificationRepository
                .findById(notificationId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Notification not found"
                        )
                );

        notificationRepository.delete(notification);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {

        return userRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(UserResponse::new)
                .toList();
    }

    @Transactional
    public void suspendUser(String userId) {

        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );

        if (user.getStatus() == UserStatus.SUSPENDED) {
            throw new IllegalStateException(
                    "User is already suspended"
            );
        }

        if (user.getStatus() == UserStatus.DEACTIVATED) {
            throw new IllegalStateException(
                    "Deactivated user cannot be suspended"
            );
        }

        user.setStatus(UserStatus.SUSPENDED);

        userRepository.save(user);
    }
}
