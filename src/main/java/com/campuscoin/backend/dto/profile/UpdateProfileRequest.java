package com.campuscoin.backend.dto.profile;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class UpdateProfileRequest {

    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    @Size(max = 50, message = "Academic year must not exceed 50 characters")
    private String academicYear;

    @DecimalMin(value = "0.00", message = "Savings goal cannot be negative")
    private BigDecimal monthlySavingsGoal;

    @DecimalMin(value = "0.00", message = "Monthly income cannot be negative")
    private BigDecimal monthlyIncome;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }

    public BigDecimal getMonthlySavingsGoal() { return monthlySavingsGoal; }
    public void setMonthlySavingsGoal(BigDecimal monthlySavingsGoal) { this.monthlySavingsGoal = monthlySavingsGoal; }

    public BigDecimal getMonthlyIncome() { return monthlyIncome; }
    public void setMonthlyIncome(BigDecimal monthlyIncome) { this.monthlyIncome = monthlyIncome; }
}
