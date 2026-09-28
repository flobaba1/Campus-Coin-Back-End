package com.campuscoin.backend.controller;

import com.campuscoin.backend.ai.dto.CategorySuggestionStructure;
import com.campuscoin.backend.dto.category.CategorySuggestionRequest;
import com.campuscoin.backend.service.ai.AiService;
import com.campuscoin.backend.service.ai.CategorySuggestionService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiService aiService;

    private CategorySuggestionService categorySuggestionService;

    public AiController(AiService aiService, CategorySuggestionService categorySuggestionService) {
        this.aiService = aiService;
        this.categorySuggestionService = categorySuggestionService;
    }

    @GetMapping("/test")
    public String test() {
        return aiService.testAi();
    }

    @GetMapping("/category-suggestion")
    public CategorySuggestionStructure getItems(Authentication authentication, CategorySuggestionRequest requestDTO) {

        String userId = authentication.getName();

        return categorySuggestionService.generateSuggestion(userId, requestDTO);
    }
}
