package com.campuscoin.backend.service.ai;

import com.campuscoin.backend.ai.contexts.CategoryContextGenerator;
import com.campuscoin.backend.ai.dto.CategorySuggestionStructure;
import com.campuscoin.backend.ai.prompts.CategorySuggestionPrompt;
import com.campuscoin.backend.dto.category.CategorySuggestionRequest;
import com.campuscoin.backend.entity.Category;
import com.campuscoin.backend.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategorySuggestionService {

    private static final Logger log =
            LoggerFactory.getLogger(CategorySuggestionService.class);

    private final AiService aiService;
    private final CategoryRepository categoryRepository;

    private final String systemPrompt = """
            You classify student financial transactions.

            You must distinguish between:
            - an existing category available to the student
            - a possible new category that does not currently exist

            Never use a category outside the supplied category list
            for selectedCategory.

            suggestedCategory must not match any existing category.
            """;

    public CategorySuggestionService(
            AiService aiService,
            CategoryRepository categoryRepository
    ) {
        this.aiService = aiService;
        this.categoryRepository = categoryRepository;
    }

    public CategorySuggestionStructure generateSuggestion(
            String userId,
            CategorySuggestionRequest request
    ) {

        CategoryContextGenerator context =
                new CategoryContextGenerator(
                        userId,
                        request.getType(),
                        categoryRepository
                );

        CategorySuggestionPrompt prompt =
                new CategorySuggestionPrompt(
                        context,
                        request.getDescription()
                );

        log.info(
                "Generating AI category suggestion for user {}",
                userId
        );

        CategorySuggestionStructure result =
                aiService.generateStructured(
                        systemPrompt,
                        prompt.getPrompt(),
                        CategorySuggestionStructure.class
                );

        List<Category> availableCategories =
                categoryRepository.findAvailableCategoriesByType(
                        userId,
                        request.getType()
                );

        boolean selectedExists =
                availableCategories.stream()
                        .anyMatch(category ->
                                category.getName()
                                        .equalsIgnoreCase(
                                                result.selectedCategory()
                                        )
                        );

        if (!selectedExists) {
            throw new IllegalStateException(
                    "AI returned an invalid existing category"
            );
        }

        boolean suggestedAlreadyExists =
                availableCategories.stream()
                        .anyMatch(category ->
                                category.getName()
                                        .equalsIgnoreCase(
                                                result.suggestedCategory()
                                        )
                        );

        if (suggestedAlreadyExists) {
            throw new IllegalStateException(
                    "AI returned an existing category as a new suggestion"
            );
        }

        double confidence =
                result.confidence() == null
                        ? 0
                        : Math.max(
                        0,
                        Math.min(
                                1,
                                result.confidence()
                        )
                );

        return new CategorySuggestionStructure(
                result.selectedCategory(),
                result.reason(),
                confidence,
                result.suggestedCategory()
        );
    }
}