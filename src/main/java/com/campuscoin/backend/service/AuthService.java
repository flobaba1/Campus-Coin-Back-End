package com.campuscoin.backend.service;

import com.campuscoin.backend.dto.LoginRequest;
import com.campuscoin.backend.dto.LoginResponse;
import com.campuscoin.backend.dto.SignupRequest;
import com.campuscoin.backend.dto.SignupResponse;
import com.campuscoin.backend.entity.PasswordResetToken;
import com.campuscoin.backend.entity.User;
import com.campuscoin.backend.repository.PasswordResetTokenRepository;
import com.campuscoin.backend.repository.UserRepository;
import com.campuscoin.backend.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final PasswordResetTokenRepository tokenRepository;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, PasswordResetTokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
    }

    public SignupResponse signup(SignupRequest request){
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("Email already registered");
        }
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(
                request.getName(),
                request.getEmail(),
                hashedPassword,
                request.getAcademicYear(),
                request.getMonthlySavingsGoal()
        );

        userRepository.save(user);
        return new SignupResponse(user);
    }

    public LoginResponse login(LoginRequest request){
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new RuntimeException("Invalid email or password"));

        if(!passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash()
        )){
            throw new RuntimeException("Invalid email or password");
        }

        String token  = jwtService.generateToken(user);
        return new LoginResponse(token, user);
    }

    public void forgotPassword(String email){

        User user = userRepository.findByEmail(email)
                .orElseThrow(()->
                        new RuntimeException("User not found"));
        String token = java.util.UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken(
                token,
                user,
                java.time.LocalDateTime.now().plusMinutes(15)
        );

        tokenRepository.save(resetToken);

        System.out.println(
                "PASSWORD RESET LINK: " +
                        "http://localhost:8080/api/auth/reset-password?token=" +
                        token
        );
    }

    public void resetPassword(
            String token,
            String newPassword
    ){
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(()->
                        new RuntimeException("Invalid reset token"));

        if(resetToken.getExpiryDate()
                .isBefore(java.time.LocalDateTime.now())){
            throw new RuntimeException("Reset token has expired");
        }

        User user = resetToken.getUser();

        String hashedPassword = passwordEncoder.encode(newPassword);
        user.setPasswordHash(hashedPassword);
        userRepository.save(user);
        tokenRepository.delete(resetToken);
    }
}
