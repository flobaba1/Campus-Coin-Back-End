package com.campuscoin.backend.controller;

import com.campuscoin.backend.dto.CreateNotificationRequest;
import com.campuscoin.backend.dto.NotificationResponse;
import com.campuscoin.backend.dto.UpdateNotificationRequest;
import com.campuscoin.backend.dto.UserResponse;
import com.campuscoin.backend.dto.category.CategoryResponse;
import com.campuscoin.backend.dto.category.CreateCategoryRequest;
import com.campuscoin.backend.dto.category.UpdateCategoryRequest;
import com.campuscoin.backend.service.AdminService;
import com.campuscoin.backend.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final CategoryService categoryService;

    private final AdminService adminService;

    public AdminController(CategoryService categoryService, AdminService adminService) {
        this.categoryService = categoryService;
        this.adminService = adminService;
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponse>> getCategories(
            Authentication authentication
    ) {

        String adminId = authentication.getName();

        return ResponseEntity.ok(
                categoryService.getCategoriesForAdmin()
        );
    }

    @PostMapping("/categories")
    public ResponseEntity<CategoryResponse> createCategory(
            Authentication authentication,
            @Valid @RequestBody CreateCategoryRequest request
    ) {

        String userId = authentication.getName();

        CategoryResponse response =
                categoryService.createAdminCategory(userId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(
            Authentication authentication,
            @PathVariable String categoryId,
            @Valid @RequestBody UpdateCategoryRequest request
    ) {

        String userId = authentication.getName();

        return ResponseEntity.ok(
                categoryService.updateAdminCategory(
                        userId,
                        categoryId,
                        request
                )
        );
    }

    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<Void> deleteCategory(
            Authentication authentication,
            @PathVariable String categoryId
    ) {

        String userId = authentication.getName();

        categoryService.deleteAdminCategory(
                userId,
                categoryId
        );

        return ResponseEntity.noContent().build();
    }


    @PostMapping("/notification")
    public NotificationResponse createNotification(
            @Valid @RequestBody CreateNotificationRequest request
    ) {

        return adminService.createNotification(request);
    }

    @GetMapping("/notification")
    public List<NotificationResponse> getNotifications() {

        return adminService.getNotifications();
    }

    @GetMapping("/notification/{notificationId}")
    public NotificationResponse getNotification(
            @PathVariable String notificationId
    ) {

        return adminService.getNotification(notificationId);
    }

    @PutMapping("/notification/{notificationId}")
    public NotificationResponse updateNotification(
            @PathVariable String notificationId,
            @Valid @RequestBody UpdateNotificationRequest request
    ) {

        return adminService.updateNotification(
                notificationId,
                request
        );
    }

    @DeleteMapping("/notification/{notificationId}")
    public ResponseEntity<Void> deleteNotification(
            @PathVariable String notificationId
    ) {

        adminService.deleteNotification(notificationId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/{userId}/suspend")
    public ResponseEntity<Void> suspendUser(
            @PathVariable String userId
    ) {

        adminService.suspendUser(userId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users")
    public List<UserResponse> getAllUsers() {

        return adminService.getAllUsers();
    }
}
