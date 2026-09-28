package com.campuscoin.backend.ai.contexts;

import com.campuscoin.backend.entity.Category;
import com.campuscoin.backend.enums.TransactionType;
import com.campuscoin.backend.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class CategoryContextGenerator {

    private static final Logger log =
            LoggerFactory.getLogger(CategoryContextGenerator.class);

    private String userId;

    private TransactionType transactionType;

    @Autowired
    private CategoryRepository categoryRepository;

    private String generatedContext;

    public CategoryContextGenerator(String userId, TransactionType transactionType, CategoryRepository categoryRepository) {
        this.userId = userId;
        this.transactionType = transactionType;
        this.categoryRepository = categoryRepository;
        generateContext();
    }

    private void generateContext(){
        try {
            List<Category> availableCategories = categoryRepository.findAvailableCategoriesByType(userId, transactionType);
            StringBuilder context = new StringBuilder();
            context.append("Here is a list of current available categories for ").append(transactionType).append(" transactions : \n");
            for(Category category : availableCategories){
                context.append(" - ").append(category.getName()).append("\n");
            }

            generatedContext = context.toString();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Context generation failed, try again later");
        }
    }

    public String getGeneratedContext() {
        return generatedContext;
    }
}
