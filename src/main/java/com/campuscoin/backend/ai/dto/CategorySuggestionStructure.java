package com.campuscoin.backend.ai.dto;

public record CategorySuggestionStructure (
        String selectedCategory, String reason, Double confidence, String suggestedCategory
) {
}
