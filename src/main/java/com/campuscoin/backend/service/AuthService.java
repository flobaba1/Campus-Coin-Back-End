package com.campuscoin.backend.service;

import com.campuscoin.backend.dto.LoginRequest;
import com.campuscoin.backend.dto.LoginResponse;
import com.campuscoin.backend.dto.SignupRequest;
import com.campuscoin.backend.dto.SignupResponse;
import com.campuscoin.backend.entity.Admin;
import com.campuscoin.backend.entity.Otp;
import com.campuscoin.backend.entity.User;
import com.campuscoin.backend.enums.OtpPurpose;
import com.campuscoin.backend.repository.AdminRepository;
import com.campuscoin.backend.repository.OtpRepository;
import com.campuscoin.backend.repository.PasswordResetTokenRepository;
import com.campuscoin.backend.repository.UserRepository;
import com.campuscoin.backend.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AdminRepository adminRepository;
    private final OtpRepository otpRepository;
    private final EmailService emailService;
    private static final int MAX_OTP_ATTEMPTS = 5;
    private final OtpService otpService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
                       PasswordResetTokenRepository tokenRepository, AdminRepository adminRepository,
                        OtpRepository otpRepository, EmailService emailService, OtpService otpService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.adminRepository = adminRepository;
        this.otpRepository = otpRepository;
        this.emailService = emailService;
        this.otpService = otpService;
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

    public boolean createAdmin(LoginRequest request){
        if(adminRepository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("Email already registered");
        }
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        Admin user = new Admin();
        user.setEmail(request.getEmail());
        user.setPasswordHash(hashedPassword);

        adminRepository.save(user);
        return true;
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

    public LoginResponse adminLogin(LoginRequest request){
        Admin admin = adminRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new RuntimeException("Invalid email or password"));

        if(!passwordEncoder.matches(
                request.getPassword(),
                admin.getPasswordHash()
        )){
            throw new RuntimeException("Invalid email or password");
        }

        String token  = jwtService.generateToken(admin);
        return new LoginResponse(token, admin);
    }

    @Transactional
    public String forgotPassword(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        // Remove previous pending password reset OTPs
        otpRepository.deleteByUserAndPurpose(
                user,
                OtpPurpose.PASSWORD_RESET
        );

        String otpCode = generateOtp();

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime expiresAt = now.plusMinutes(10);

        Otp otp = new Otp(
                user,
                otpCode,
                OtpPurpose.PASSWORD_RESET,
                now,
                expiresAt
        );

        otpRepository.save(otp);

        boolean emailSent = emailService.sendPasswordResetOtp(
                user.getEmail(),
                otpCode
        );

        if (!emailSent) {
            throw new RuntimeException(
                    "Unable to send password reset email"
            );
        }

        return otp.getOtpId();
    }

    @Transactional
    public void resetPassword(
            String otpId,
            String otpCode,
            String newPassword
    ) {

        Otp otp = otpRepository
                .findByOtpId(
                        otpId
                )
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid or expired OTP")
                );

        LocalDateTime now = LocalDateTime.now();

        if (otp.getAttempts() >= MAX_OTP_ATTEMPTS) {
            otpService.deleteOtp(otp);

            throw new IllegalArgumentException(
                    "Too many OTP attempts"
            );
        }

        if (now.isAfter(otp.getExpiresAt())) {

            otpService.deleteOtp(otp);

            throw new IllegalArgumentException("OTP has expired");
        }

        if (!otpCode.equals(otp.getOtpCode())){
            otpService.incrementAttempts(otp);
            throw new IllegalArgumentException("Incorrect OTP");
        }


        User user = otp.getUser();

        user.setPasswordHash(
                passwordEncoder.encode(newPassword)
        );

        userRepository.save(user);

        otpRepository.delete(otp);
    }

    private String generateOtp() {

        return String.format(
                "%06d",
                new SecureRandom().nextInt(1_000_000)
        );
    }

}
