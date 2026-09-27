package com.campuscoin.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtService jwtService;

    public SecurityConfig(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserJwtAuthenticationFilter userJwtAuthenticationFilter() {
        return new UserJwtAuthenticationFilter(jwtService);
    }

    @Bean
    public AdminJwtAuthenticationFilter adminJwtAuthenticationFilter() {
        return new AdminJwtAuthenticationFilter(jwtService);
    }

    @Bean
    @Order(1)
    public SecurityFilterChain authFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .securityMatcher("/api/auth/**")
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth ->
                        auth.anyRequest().permitAll()
                );

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain adminFilterChain(
            HttpSecurity http,
            AdminJwtAuthenticationFilter adminJwtAuthenticationFilter
    ) throws Exception {

        http
                .securityMatcher("/api/admin/**")
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth ->
                        auth.anyRequest().authenticated()
                )
                .addFilterBefore(
                        adminJwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    @Order(3)
    public SecurityFilterChain userFilterChain(
            HttpSecurity http,
            UserJwtAuthenticationFilter userJwtAuthenticationFilter
    ) throws Exception {

        http
                .securityMatcher("/**")
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth ->
                        auth.anyRequest().authenticated()
                )
                .addFilterBefore(
                        userJwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}