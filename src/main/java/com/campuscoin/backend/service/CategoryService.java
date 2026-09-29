package com.campuscoin.backend.service;

import com.campuscoin.backend.dto.category.CategoryResponse;
import com.campuscoin.backend.dto.category.CreateCategoryRequest;
import com.campuscoin.backend.dto.category.UpdateCategoryRequest;
import com.campuscoin.backend.entity.Category;
import com.campuscoin.backend.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final AdminAuditLogService auditLogService;

    public CategoryService(CategoryRepository categoryRepository, AdminAuditLogService auditLogService) {
        this.categoryRepository = categoryRepository;
        this.auditLogService = auditLogService;
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategoriesForUser(String userId) {

        return categoryRepository
                .findByDefaultCategoryTrueOrCreatedBy(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategoriesForAdmin() {

        return categoryRepository
                .findByDefaultCategoryTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public CategoryResponse createCategory(
            String userId,
            CreateCategoryRequest request
    ) {

        String name = request.getName().trim();

        if (categoryRepository.existsByNameIgnoreCaseAndDefaultCategoryTrue(name)
                || categoryRepository.existsByNameIgnoreCaseAndCreatedBy(name, userId)) {

            throw new IllegalArgumentException(
                    "A category with this name already exists"
            );
        }

        Category category = new Category();

        category.setName(name);
        category.setType(request.getType());
        category.setDefaultCategory(false);
        category.setCreatedBy(userId);
        category.setTotalUser(1);

        Category saved = categoryRepository.save(category);

        return toResponse(saved);
    }

    @Transactional
    public CategoryResponse createAdminCategory(
            String userId,
            CreateCategoryRequest request
    ) {

        String name = request.getName().trim();

        if (categoryRepository.existsByNameIgnoreCaseAndDefaultCategoryTrue(name)
                || categoryRepository.existsByNameIgnoreCaseAndCreatedBy(name, userId)) {

            throw new IllegalArgumentException(
                    "A category with this name already exists"
            );
        }

        Category category = new Category();

        category.setName(name);
        category.setType(request.getType());
        category.setDefaultCategory(true);
        category.setCreatedBy(userId);
        category.setTotalUser(0);

        Category saved = categoryRepository.save(category);

        auditLogService.record(
                "CATEGORY_CREATED",
                "CATEGORY",
                saved.getCategoryId(),
                "Created category"
        );


        return toResponse(saved);
    }

    @Transactional
    public CategoryResponse updateCategory(
            String userId,
            String categoryId,
            UpdateCategoryRequest request
    ) {

        Category category = categoryRepository
                .findByCategoryIdAndCreatedBy(categoryId, userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Category not found or cannot be modified"
                        )
                );

        String name = request.getName().trim();

        boolean duplicate =
                categoryRepository.existsByNameIgnoreCaseAndDefaultCategoryTrue(name)
                        || categoryRepository.existsByNameIgnoreCaseAndCreatedBy(name, userId);

        if (duplicate && !category.getName().equalsIgnoreCase(name)) {
            throw new IllegalArgumentException(
                    "A category with this name already exists"
            );
        }

        category.setName(name);
        category.setType(request.getType());

        return toResponse(categoryRepository.save(category));
    }

    @Transactional
    public CategoryResponse updateAdminCategory(
            String userId,
            String categoryId,
            UpdateCategoryRequest request
    ) {

        Category category = categoryRepository
                .findByCategoryIdAndDefaultCategoryTrue(categoryId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Category not found"
                        )
                );

        String name = request.getName().trim();

        boolean duplicate =
                categoryRepository.existsByNameIgnoreCaseAndDefaultCategoryTrue(name);

        if (duplicate && !category.getName().equalsIgnoreCase(name)) {
            throw new IllegalArgumentException(
                    "A category with this name already exists"
            );
        }

        category.setName(name);
        category.setType(request.getType());

        Category updated = categoryRepository.save(category);

        auditLogService.record(
                "CATEGORY_UPDATED",
                "CATEGORY",
                updated.getCategoryId(),
                "Updated category"
        );

        return toResponse(updated);
    }

    @Transactional
    public void deleteCategory(
            String userId,
            String categoryId
    ) {

        Category category = categoryRepository
                .findByCategoryIdAndCreatedBy(categoryId, userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Category not found or cannot be deleted"
                        )
                );

        categoryRepository.delete(category);
    }

    @Transactional
    public void deleteAdminCategory(
            String userId,
            String categoryId
    ) {

        Category category = categoryRepository
                .findByCategoryIdAndDefaultCategoryTrue(categoryId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Category not found"
                        )
                );

        if (category.getTotalUser() > 0){
            throw new IllegalArgumentException("Category cannot be deleted, used by users");
        }



        categoryRepository.delete(category);

        auditLogService.record(
                "CATEGORY_DELETED",
                "CATEGORY",
                categoryId,
                "Deleted category"
        );
    }

    private CategoryResponse toResponse(Category category) {

        return new CategoryResponse(
                category.getCategoryId(),
                category.getName(),
                category.getType(),
                category.isDefaultCategory(),
                category.getTotalUser()
        );
    }
}
