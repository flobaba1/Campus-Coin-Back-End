package com.campuscoin.backend.controller;

import com.campuscoin.backend.ai.dto.CategorySuggestionStructure;
import com.campuscoin.backend.dto.category.CategorySuggestionRequest;
import com.campuscoin.backend.service.ai.AiService;
import com.campuscoin.backend.service.ai.CategorySuggestionService;
import com.campuscoin.backend.service.ai.MonthlyFinancialSummaryService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiService aiService;

    private final CategorySuggestionService categorySuggestionService;

    private final MonthlyFinancialSummaryService monthlyFinancialSummaryService;

    public AiController(AiService aiService, CategorySuggestionService categorySuggestionService, MonthlyFinancialSummaryService monthlyFinancialSummaryService) {
        this.aiService = aiService;
        this.categorySuggestionService = categorySuggestionService;
        this.monthlyFinancialSummaryService = monthlyFinancialSummaryService;
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

    @GetMapping("/month-report")
    public boolean report(Authentication authentication, CategorySuggestionRequest requestDTO) {

        String userId = authentication.getName();

        return monthlyFinancialSummaryService.generateMonthReport(userId, 9, 2026);
    }

    @GetMapping("/generate-report")
    public Map<String, String> myReport(Authentication authentication, @RequestParam List<String> months ) {
        String userId = authentication.getName();
        boolean respStat = monthlyFinancialSummaryService.getMyReport(userId, months);
        return Map.of("status", "success", "message", "report generated");
    }
}
