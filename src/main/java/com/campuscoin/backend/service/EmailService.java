package com.campuscoin.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class EmailService {

    private static final String EMAILJS_URL =
            "https://api.emailjs.com/api/v1.0/email/send";

    private final RestClient restClient;

    @Value("${EMAILJS_SERVICE_ID}")
    private String serviceId;

    @Value("${EMAILJS_TEMPLATE_ID}")
    private String templateId;

    @Value("${EMAILJS_PUBLIC_KEY}")
    private String publicKey;

    @Value("${EMAILJS_PRIVATE_KEY}")
    private String privateKey;

    public EmailService() {
        this.restClient = RestClient.builder().build();
    }

    public boolean sendPasswordResetOtp(
            String email,
            String otpCode
    ) {

        Map<String, Object> requestBody = Map.of(
                "service_id", serviceId,
                "template_id", templateId,
                "user_id", publicKey,
                "accessToken", privateKey,

                "template_params", Map.of(
                        "email", email,
                        "passcode", otpCode,
                        "CompanyName", "CampusCoin"
                )
        );

        try {

            restClient.post()
                    .uri(EMAILJS_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .toBodilessEntity();

            return true;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
