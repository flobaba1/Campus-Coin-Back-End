package com.campuscoin.backend.dto.category;

import com.campuscoin.backend.enums.TransactionType;

public class CategoryResponse {

    private String categoryId;
    private String name;
    private TransactionType type;
    private boolean defaultCategory;

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
}
