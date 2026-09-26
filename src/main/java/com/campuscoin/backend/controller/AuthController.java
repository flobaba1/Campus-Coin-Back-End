package com.campuscoin.backend.controller;


import com.campuscoin.backend.dto.*;
import com.campuscoin.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/signup")
        public SignupResponse signup(@Valid @RequestBody SignupRequest request){
           return authService.signup(request);
        }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request){
        return authService.login(request);
    }

    @PostMapping("/create-admin")
    public boolean createAdmin(@Valid @RequestBody LoginRequest request){
        return authService.createAdmin(request);
    }

    @PostMapping("/forgot-password")
    public Map<String, String> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request
    ) {

        String otpId = authService.forgotPassword(
                request.getEmail()
        );

        return Map.of("otpId", otpId, "message", "One time password sent to email");
    }

    @PostMapping("/admin-login")
    public LoginResponse adminLogin(@Valid @RequestBody LoginRequest request){
        return authService.adminLogin(request);
    }

    @PostMapping("/reset-password")
    public String resetPassword(@Valid @RequestBody ResetPasswordRequest request){
        authService.resetPassword(request.getOtpId(), request.getOtpCode(),
                request.getNewPassword());
        return "Password reset successful";
    }

}
