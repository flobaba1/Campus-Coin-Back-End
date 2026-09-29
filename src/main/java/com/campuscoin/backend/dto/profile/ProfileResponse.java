package com.campuscoin.backend.dto.profile;

import com.campuscoin.backend.entity.User;

import java.math.BigDecimal;

public class ProfileResponse {

    private String userId;
    private String name;
    private String email;
    private String academicYear;
    private BigDecimal monthlySavingsGoal;
    private BigDecimal monthlyIncome;
    private boolean profilePhotoAvailable;

    public ProfileResponse(User user) {
        this(user, user.getProfilePhoto() != null && user.getProfilePhoto().length > 0);
    }

    public ProfileResponse(User user, boolean profilePhotoAvailable) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.academicYear = user.getAcademicYear();
        this.monthlySavingsGoal = user.getMonthlySavingsGoal();
        this.monthlyIncome = user.getMonthlyIncome();
        this.profilePhotoAvailable = profilePhotoAvailable;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getAcademicYear() { return academicYear; }
    public BigDecimal getMonthlySavingsGoal() { return monthlySavingsGoal; }
    public BigDecimal getMonthlyIncome() { return monthlyIncome; }
    public boolean isProfilePhotoAvailable() { return profilePhotoAvailable; }
}
