package com.campuscoin.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @Column(
            name = "category_id",
            length = 36,
            nullable = false,
            updatable = false
    )
    private String categoryId;

    @Column(
            name = "name",
            nullable = false,
            length = 100
    )
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "type",
            nullable = false
    )
    private TransactionType type;

    @Column(
            name = "is_default",
            nullable = false
    )
    private boolean defaultCategory = false;

    @Column(
            name = "created_by",
            length = 36
    )
    private String createdBy;

    @PrePersist
    protected void onCreate() {
        if (categoryId == null) {
            categoryId = java.util.UUID.randomUUID().toString();
        }
    }

    // Getters and Setters

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public boolean isDefaultCategory() {
        return defaultCategory;
    }

    public void setDefaultCategory(boolean defaultCategory) {
        this.defaultCategory = defaultCategory;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
