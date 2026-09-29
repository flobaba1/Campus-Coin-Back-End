package com.campuscoin.backend.service;

import com.campuscoin.backend.dto.profile.ProfileResponse;
import com.campuscoin.backend.dto.profile.UpdateProfileRequest;
import com.campuscoin.backend.entity.User;
import com.campuscoin.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ProfileService {

    private static final long MAX_PHOTO_SIZE = 5L * 1024L * 1024L;

    private final UserRepository userRepository;

    public ProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public ProfileResponse getProfile(String userId) {
        User user = getUser(userId);
        return new ProfileResponse(user, userRepository.hasProfilePhoto(userId));
    }

    public ProfileResponse updateProfile(
            String userId,
            UpdateProfileRequest request
    ) {
        User user = getUser(userId);

        if (request.getName() != null) {
            String name = request.getName().trim();
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Full name is required");
            }
            user.setName(name);
        }

        if (request.getAcademicYear() != null) {
            String academicYear = request.getAcademicYear().trim();
            user.setAcademicYear(academicYear.isEmpty() ? null : academicYear);
        }

        if (request.getMonthlySavingsGoal() != null) {
            user.setMonthlySavingsGoal(request.getMonthlySavingsGoal());
        }

        if (request.getMonthlyIncome() != null) {
            user.setMonthlyIncome(request.getMonthlyIncome());
        }

        User savedUser = userRepository.save(user);
        return new ProfileResponse(savedUser, userRepository.hasProfilePhoto(userId));
    }

    @Transactional
    public void uploadProfilePhoto(
            String userId,
            MultipartFile file
    ) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Please select an image");
        }

        if (file.getSize() > MAX_PHOTO_SIZE) {
            throw new IllegalArgumentException("Profile photo must be 5 MB or smaller");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.toLowerCase(java.util.Locale.ROOT).startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }

        try {
            User user = getUser(userId);
            user.setProfilePhoto(file.getBytes());
            user.setProfilePhotoContentType(contentType);
            userRepository.save(user);
        } catch (IOException e) {
            throw new RuntimeException("Unable to store profile photo", e);
        }
    }

    @Transactional(readOnly = true)
    public ProfilePhoto getProfilePhoto(String userId) {
        User user = getUser(userId);

        if (user.getProfilePhoto() == null || user.getProfilePhoto().length == 0) {
            return null;
        }

        return new ProfilePhoto(
                user.getProfilePhoto(),
                user.getProfilePhotoContentType()
        );
    }

    @Transactional
    public void deleteProfilePhoto(String userId) {
        User user = getUser(userId);
        user.setProfilePhoto(null);
        user.setProfilePhotoContentType(null);
        userRepository.save(user);
    }

    private User getUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public record ProfilePhoto(byte[] bytes, String contentType) {}
}
