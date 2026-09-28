package com.campuscoin.backend.dto;

import com.campuscoin.backend.entity.User;
import com.campuscoin.backend.enums.UserStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class UserResponse {

    private String userId;
    private String name;
    private String email;
    private String academicYear;
    private BigDecimal monthlySavingsGoal;
    private BigDecimal monthlyIncome;
    private LocalDateTime createdAt;
    private LocalDateTime lastVisited;
    private UserStatus status;

    public UserResponse(User user) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.academicYear = user.getAcademicYear();
        this.monthlySavingsGoal = user.getMonthlySavingsGoal();
        this.monthlyIncome = user.getMonthlyIncome();
        this.createdAt = user.getCreatedAt();
        this.lastVisited = user.getLastVisited();
        this.status = user.getStatus();
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

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastVisited() {
        return lastVisited;
    }

    public void setLastVisited(LocalDateTime lastVisited) {
        this.lastVisited = lastVisited;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }
}
