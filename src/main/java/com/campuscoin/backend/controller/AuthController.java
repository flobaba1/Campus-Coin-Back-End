package com.campuscoin.backend.controller;


import com.campuscoin.backend.dto.*;
import com.campuscoin.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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

    @PostMapping("/forgot-password")
    public String forgotPassword(@Valid @RequestBody ForgotPasswordRequest request){
        authService.forgotPassword(request.getEmail());
        return "Password reset link generated";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@Valid @RequestBody ResetPasswordRequest request){
        authService.resetPassword(request.getToken(),
                request.getNewPassword());
        return "Password reset successful";
    }

}
