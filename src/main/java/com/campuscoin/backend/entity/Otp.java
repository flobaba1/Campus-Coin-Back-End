package com.campuscoin.backend.entity;

import com.campuscoin.backend.enums.OtpPurpose;
import com.campuscoin.backend.enums.OtpStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "otps")
public class Otp {

    @Id
    @Column(name = "otp_id", length = 36)
    private String otpId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_otp_user")
    )
    private User user;

    @Column(name = "otp_code", nullable = false, length = 10)
    private String otpCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "purpose", nullable = false)
    private OtpPurpose purpose;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OtpStatus status;

    @Column(name = "attempts", nullable = false)
    private int attempts;

    protected Otp() {
    }

    public Otp(
            User user,
            String otpCode,
            OtpPurpose purpose,
            LocalDateTime createdAt,
            LocalDateTime expiresAt
    ) {
        this.otpId = UUID.randomUUID().toString();
        this.user = user;
        this.otpCode = otpCode;
        this.purpose = purpose;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.status = OtpStatus.PENDING;
        this.attempts = 0;
    }

    public String getOtpId() {
        return otpId;
    }

    public User getUser() {
        return user;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public OtpPurpose getPurpose() {
        return purpose;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public OtpStatus getStatus() {
        return status;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setStatus(OtpStatus status) {
        this.status = status;
    }

    public void incrementAttempts() {
        this.attempts++;
    }
}
