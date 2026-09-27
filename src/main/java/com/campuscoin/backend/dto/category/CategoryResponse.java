package com.campuscoin.backend.dto.category;

import com.campuscoin.backend.enums.TransactionType;

public class CategoryResponse {

    private String categoryId;
    private String name;
    private TransactionType type;
    private boolean defaultCategory;
    private int totalUsers;

    public CategoryResponse(
            String categoryId,
            String name,
            TransactionType type,
            boolean defaultCategory
    ) {
        this.categoryId = categoryId;
        this.name = name;
        this.type = type;
        this.defaultCategory = defaultCategory;
    }

    public CategoryResponse(
            String categoryId,
            String name,
            TransactionType type,
            boolean defaultCategory,
            int totalUsers
    ) {
        this.categoryId = categoryId;
        this.name = name;
        this.type = type;
        this.defaultCategory = defaultCategory;
        this.totalUsers = totalUsers;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public TransactionType getType() {
        return type;
    }

    public boolean isDefaultCategory() {
        return defaultCategory;
    }

    public int getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(int totalUsers) {
        this.totalUsers = totalUsers;
    }
}
