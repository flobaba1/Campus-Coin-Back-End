package com.campuscoin.backend.dto;

public class CategoryAnalyticsResponse {

    private String categoryName;
    private long transactionCount;
    private double percentage;

    public CategoryAnalyticsResponse(
            String categoryName,
            long transactionCount,
            double percentage
    ) {
        this.categoryName = categoryName;
        this.transactionCount = transactionCount;
        this.percentage = percentage;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public long getTransactionCount() {
        return transactionCount;
    }

    public double getPercentage() {
        return percentage;
    }
}