package com.campuscoin.backend.security;

import com.campuscoin.backend.entity.Admin;
import com.campuscoin.backend.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final String userSecret;
    private final String adminSecret;
    private final long userExpiration;
    private final long adminExpiration;

    public JwtService(
            @Value("${USER_JWT_SECRET}") String userSecret,
            @Value("${ADMIN_JWT_SECRET}") String adminSecret,
            @Value("${USER_JWT_EXPIRATION:900000}") long userExpiration,
            @Value("${ADMIN_JWT_EXPIRATION:900000}") long adminExpiration
    ) {
        this.userSecret = userSecret;
        this.adminSecret = adminSecret;
        this.userExpiration = userExpiration;
        this.adminExpiration = adminExpiration;
    }

    private SecretKey getUserSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(userSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private SecretKey getAdminSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(adminSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User user) {

        Date now = new Date();

        Date expiry = new Date(
                now.getTime() + userExpiration
        );

        return Jwts.builder()
                .subject(user.getUserId())
                .claim("email", user.getEmail())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getUserSigningKey())
                .compact();
    }

    public String generateToken(Admin user) {

        Date now = new Date();

        Date expiry = new Date(
                now.getTime() + adminExpiration
        );

        return Jwts.builder()
                .subject(user.getAdminId())
                .claim("email", user.getEmail())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getAdminSigningKey())
                .compact();
    }

    public String extractUserSubject(String token) {

        return Jwts.parser()
                .verifyWith(getUserSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String extractAdminSubject(String token) {

        return Jwts.parser()
                .verifyWith(getAdminSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isUserTokenValid(String token) {

        try {

            Jwts.parser()
                    .verifyWith(getUserSigningKey())
                    .build()
                    .parseSignedClaims(token);

            return true;

        } catch (Exception e) {

            return false;
        }
    }

    public boolean isAdminTokenValid(String token) {

        try {

            Jwts.parser()
                    .verifyWith(getAdminSigningKey())
                    .build()
                    .parseSignedClaims(token);

            return true;

        } catch (Exception e) {

            return false;
        }
    }
}