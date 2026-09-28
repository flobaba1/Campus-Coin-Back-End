package com.campuscoin.backend.ai.prompts;

import com.campuscoin.backend.ai.contexts.CategoryContextGenerator;

public class CategorySuggestionPrompt {

    private CategoryContextGenerator context;

    private String description;

    public CategorySuggestionPrompt(CategoryContextGenerator context, String description) {
        this.context = context;
        this.description = description;
    }

    public String getPrompt(){
        String prompt = context.getGeneratedContext() + "Given the below transaction description \n" +
                "`" +
                description +
                "`\n" +
                "Select the most appropriate category for the transactions and also suggest a category that is not included in the above list";
        return prompt;
    }
}
