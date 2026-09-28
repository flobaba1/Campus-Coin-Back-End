package com.campuscoin.backend.service.ai;

import com.campuscoin.backend.ai.contexts.CategoryContextGenerator;
import com.campuscoin.backend.ai.dto.CategorySuggestionStructure;
import com.campuscoin.backend.ai.prompts.CategorySuggestionPrompt;
import com.campuscoin.backend.dto.category.CategorySuggestionRequest;
import com.campuscoin.backend.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CategorySuggestionService {

    private static final Logger log =
            LoggerFactory.getLogger(CategorySuggestionService.class);

    private final AiService aiService;

    private CategoryRepository categoryRepository;

    String systemPrompt = """
                    You classify student financial transactions.

                    Only choose a category that is appropriate
                    for the transaction.
                    """;

    public CategorySuggestionService(AiService aiService, CategoryRepository categoryRepository) {
        this.aiService = aiService;
        this.categoryRepository = categoryRepository;
    }

    public CategorySuggestionStructure generateSuggestion(String userId, CategorySuggestionRequest request){
        CategoryContextGenerator context = new CategoryContextGenerator(userId, request.getType(), this.categoryRepository);
        CategorySuggestionPrompt prompt = new CategorySuggestionPrompt(context, request.getDescription());
        log.info("Context : {}\n Prompt: {}", context.getGeneratedContext(), prompt.getPrompt());
        return aiService.generateStructured(systemPrompt, prompt.getPrompt(), CategorySuggestionStructure.class);
    }
}
