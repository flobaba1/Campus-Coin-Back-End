package com.campuscoin.backend.dto;

import java.math.BigDecimal;

public class BudgetResponse {

    private String budgetId;
    private String categoryId;
    private String categoryName;

    private BigDecimal amount;
    private BigDecimal spent;
    private BigDecimal remaining;

    private BigDecimal percentageUsed;

    private String status;

    private Integer month;
    private Integer year;

    public BudgetResponse() {
    }

    public BudgetResponse(
            String budgetId,
            String categoryId,
            String categoryName,
            BigDecimal amount,
            BigDecimal spent,
            BigDecimal remaining,
            BigDecimal percentageUsed,
            String status,
            Integer month,
            Integer year
    ) {
        this.budgetId = budgetId;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.amount = amount;
        this.spent = spent;
        this.remaining = remaining;
        this.percentageUsed = percentageUsed;
        this.status = status;
        this.month = month;
        this.year = year;
    }

    public String getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(String budgetId) {
        this.budgetId = budgetId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getSpent() {
        return spent;
    }

    public void setSpent(BigDecimal spent) {
        this.spent = spent;
    }

    public BigDecimal getRemaining() {
        return remaining;
    }

    public void setRemaining(BigDecimal remaining) {
        this.remaining = remaining;
    }

    public BigDecimal getPercentageUsed() {
        return percentageUsed;
    }

    public void setPercentageUsed(BigDecimal percentageUsed) {
        this.percentageUsed = percentageUsed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
