package com.campuscoin.backend.controller;

import com.campuscoin.backend.dto.profile.ProfileResponse;
import com.campuscoin.backend.dto.profile.UpdateProfileRequest;
import com.campuscoin.backend.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public ResponseEntity<ProfileResponse> getProfile(
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                profileService.getProfile(authentication.getName())
        );
    }

    @PutMapping
    public ResponseEntity<ProfileResponse> updateProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        return ResponseEntity.ok(
                profileService.updateProfile(
                        authentication.getName(),
                        request
                )
        );
    }

    @PostMapping(value = "/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadProfilePhoto(
            Authentication authentication,
            @RequestPart("file") MultipartFile file
    ) {
        profileService.uploadProfilePhoto(
                authentication.getName(),
                file
        );

        return ResponseEntity.ok().build();
    }

    @GetMapping("/photo")
    public ResponseEntity<byte[]> getProfilePhoto(
            Authentication authentication
    ) {
        ProfileService.ProfilePhoto photo =
                profileService.getProfilePhoto(authentication.getName());

        if (photo == null) {
            return ResponseEntity.notFound().build();
        }

        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        if (photo.contentType() != null) {
            try {
                mediaType = MediaType.parseMediaType(photo.contentType());
            } catch (IllegalArgumentException ignored) {
                // Keep application/octet-stream for unknown content types.
            }
        }

        return ResponseEntity.ok()
                .cacheControl(CacheControl.noCache())
                .contentType(mediaType)
                .body(photo.bytes());
    }

    @DeleteMapping("/photo")
    public ResponseEntity<Void> deleteProfilePhoto(
            Authentication authentication
    ) {
        profileService.deleteProfilePhoto(authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
