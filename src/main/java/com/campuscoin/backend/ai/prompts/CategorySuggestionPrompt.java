package com.campuscoin.backend.ai.prompts;

import com.campuscoin.backend.ai.contexts.CategoryContextGenerator;

public class CategorySuggestionPrompt {

    private final CategoryContextGenerator context;
    private final String description;

    public CategorySuggestionPrompt(
            CategoryContextGenerator context,
            String description
    ) {
        this.context = context;
        this.description = description;
    }

    public String getPrompt() {

        return """
                %s

                Analyze this student transaction description:

                "%s"

                Return a category classification using these rules:

                1. selectedCategory MUST be one of the categories listed above.
                2. suggestedCategory MUST be a new category name that is NOT in the listed categories.
                3. selectedCategory should be the best existing category for this transaction.
                4. suggestedCategory should only be a useful new category when the description suggests a more specific category that does not already exist.
                5. reason must briefly explain why the selected category fits.
                6. confidence must be a number between 0 and 1.
                7. Do not invent transaction details.
                8. Return only the requested structured response.
                """.formatted(
                context.getGeneratedContext(),
                description
        );
    }
}