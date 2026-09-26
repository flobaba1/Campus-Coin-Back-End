package com.campuscoin.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_email", columnNames = "email")
        }
)
public class User {

    @Id
    @Column(
            name = "user_id",
            length = 36,
            nullable = false,
            updatable = false
    )
    private String userId;

    @Column(
            name = "name",
            nullable = false,
            length = 255
    )
    private String name;

    @Column(
            name = "email",
            nullable = false,
            unique = true,
            length = 255
    )
    private String email;

    @JsonIgnore
    @Column(
            name = "password_hash",
            nullable = false,
            length = 255
    )
    private String passwordHash;

    @Column(
            name = "academic_year",
            length = 50
    )
    private String academicYear;

    @Column(
            name = "monthly_savings_goal",
            nullable = false,
            precision = 15,
            scale = 2
    )
    private BigDecimal monthlySavingsGoal = BigDecimal.ZERO;

    @Column(
            name = "monthly_income",
            nullable = false,
            precision = 15,
            scale = 2
    )
    private BigDecimal monthlyIncome = BigDecimal.ZERO;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    protected User() {
    }

    public User(
            String name,
            String email,
            String passwordHash,
            String academicYear,
            BigDecimal monthlySavingsGoal
    ) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.academicYear = academicYear;
        this.monthlySavingsGoal = monthlySavingsGoal;
    }

    @PrePersist
    protected void onCreate() {
        if (userId == null) {
            userId = java.util.UUID.randomUUID().toString();
        }

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (monthlySavingsGoal == null) {
            monthlySavingsGoal = BigDecimal.ZERO;
        }
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

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
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

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }
}