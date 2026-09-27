package com.campuscoin.backend.service;

import com.campuscoin.backend.entity.Otp;
import com.campuscoin.backend.repository.OtpRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OtpService {

    private final OtpRepository otpRepository;

    public OtpService(OtpRepository otpRepository) {
        this.otpRepository = otpRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void incrementAttempts(Otp otp) {

        otp.incrementAttempts();

        otpRepository.save(otp);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteOtp(Otp otp) {
        otpRepository.delete(otp);
    }
}
