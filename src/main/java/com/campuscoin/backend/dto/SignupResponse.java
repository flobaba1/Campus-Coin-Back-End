package com.campuscoin.backend.dto;

import com.campuscoin.backend.entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SignupResponse {

    private String userId;
    private String name;
    private String email;
    private String academicYear;
    private BigDecimal monthlySavingsGoal;
    private LocalDateTime createdAt;

    public SignupResponse() {
    }

    public SignupResponse(
            String userId,
            String name,
            String email,
            String academicYear,
            BigDecimal monthlySavingsGoal,
            LocalDateTime createdAt) {

        this.userId = userId;
        this.name = name;
        this.email = email;
        this.academicYear = academicYear;
        this.monthlySavingsGoal = monthlySavingsGoal;
        this.createdAt = createdAt;
    }

    public SignupResponse(User user) {

        this.userId = user.getUserId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.academicYear = user.getAcademicYear();
        this.monthlySavingsGoal = user.getMonthlySavingsGoal();
        this.createdAt = user.getCreatedAt();
    }

    // Getters and Setters

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public BigDecimal getMonthlySavingsGoal() {
        return monthlySavingsGoal;
    }

    public void setMonthlySavingsGoal(BigDecimal monthlySavingsGoal) {
        this.monthlySavingsGoal = monthlySavingsGoal;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
