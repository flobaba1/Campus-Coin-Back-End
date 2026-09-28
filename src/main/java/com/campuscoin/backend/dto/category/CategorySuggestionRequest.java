package com.campuscoin.backend.dto.category;

import com.campuscoin.backend.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CategorySuggestionRequest {

    @NotBlank(message = "transaction description is required")
    private String description;

    @NotNull(message = "Category type is required")
    private TransactionType type;

    // Getters and Setters
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }
}
