package com.campuscoin.backend.config;

import com.campuscoin.backend.entity.Category;
import com.campuscoin.backend.entity.TransactionType;
import com.campuscoin.backend.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CategoryDataInitializer {

    @Bean
    CommandLineRunner initializeDefaultCategories(
            CategoryRepository categoryRepository
    ) {

        return args -> {

            createIfMissing(
                    categoryRepository,
                    "Allowance",
                    TransactionType.INCOME
            );

            createIfMissing(
                    categoryRepository,
                    "Scholarship",
                    TransactionType.INCOME
            );

            createIfMissing(
                    categoryRepository,
                    "Part-time Work",
                    TransactionType.INCOME
            );

            createIfMissing(
                    categoryRepository,
                    "Freelance",
                    TransactionType.INCOME
            );

            createIfMissing(
                    categoryRepository,
                    "Gifts",
                    TransactionType.INCOME
            );

            createIfMissing(
                    categoryRepository,
                    "Food",
                    TransactionType.EXPENSE
            );

            createIfMissing(
                    categoryRepository,
                    "Transport",
                    TransactionType.EXPENSE
            );

            createIfMissing(
                    categoryRepository,
                    "Education",
                    TransactionType.EXPENSE
            );

            createIfMissing(
                    categoryRepository,
                    "Bills & Utilities",
                    TransactionType.EXPENSE
            );

            createIfMissing(
                    categoryRepository,
                    "Entertainment",
                    TransactionType.EXPENSE
            );

            createIfMissing(
                    categoryRepository,
                    "Personal Care",
                    TransactionType.EXPENSE
            );

            createIfMissing(
                    categoryRepository,
                    "Shopping",
                    TransactionType.EXPENSE
            );

            createIfMissing(
                    categoryRepository,
                    "Health",
                    TransactionType.EXPENSE
            );

            createIfMissing(
                    categoryRepository,
                    "Savings",
                    TransactionType.EXPENSE
            );

            createIfMissing(
                    categoryRepository,
                    "Other",
                    TransactionType.EXPENSE
            );
        };
    }

    private void createIfMissing(
            CategoryRepository repository,
            String name,
            TransactionType type
    ) {

        if (!repository.existsByNameIgnoreCaseAndDefaultCategoryTrue(name)) {

            Category category = new Category();

            category.setName(name);
            category.setType(type);
            category.setDefaultCategory(true);
            category.setCreatedBy(null);

            repository.save(category);
        }
    }
}
