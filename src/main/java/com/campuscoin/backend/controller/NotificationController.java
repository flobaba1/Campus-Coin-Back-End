package com.campuscoin.backend.controller;

import com.campuscoin.backend.dto.NotificationResponse;
import com.campuscoin.backend.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService
    ) {
        this.notificationService =
                notificationService;
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponse>>
    getNotifications(
            Authentication authentication
    ) {

        String userId =
                authentication.getName();

        return ResponseEntity.ok(
                notificationService.getNotifications(
                        userId
                )
        );
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<NotificationResponse>
    markAsRead(
            @PathVariable String notificationId,
            Authentication authentication
    ) {

        String userId =
                authentication.getName();

        return ResponseEntity.ok(
                notificationService.markAsRead(
                        userId,
                        notificationId
                )
        );
    }
}
