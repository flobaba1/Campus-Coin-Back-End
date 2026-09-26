package com.campuscoin.backend.repository;

import com.campuscoin.backend.entity.Otp;
import com.campuscoin.backend.entity.User;
import com.campuscoin.backend.enums.OtpPurpose;
import com.campuscoin.backend.enums.OtpStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, String> {

    Optional<Otp> findByOtpCodeAndPurposeAndStatus(
            String otpCode,
            OtpPurpose purpose,
            OtpStatus status
    );

    Optional<Otp> findByOtpId(String otpId);

    void deleteByUserAndPurpose(
            User user,
            OtpPurpose purpose
    );

}
