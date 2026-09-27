package com.campuscoin.backend.controller;

import com.campuscoin.backend.dto.category.CategoryResponse;
import com.campuscoin.backend.dto.category.CreateCategoryRequest;
import com.campuscoin.backend.dto.category.UpdateCategoryRequest;
import com.campuscoin.backend.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategories(
            Authentication authentication
    ) {

        String userId = authentication.getName();

        return ResponseEntity.ok(
                categoryService.getCategoriesForUser(userId)
        );
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            Authentication authentication,
            @Valid @RequestBody CreateCategoryRequest request
    ) {

        String userId = authentication.getName();

        CategoryResponse response =
                categoryService.createCategory(userId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(
            Authentication authentication,
            @PathVariable String categoryId,
            @Valid @RequestBody UpdateCategoryRequest request
    ) {

        String userId = authentication.getName();

        return ResponseEntity.ok(
                categoryService.updateCategory(
                        userId,
                        categoryId,
                        request
                )
        );
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(
            Authentication authentication,
            @PathVariable String categoryId
    ) {

        String userId = authentication.getName();

        categoryService.deleteCategory(
                userId,
                categoryId
        );

        return ResponseEntity.noContent().build();
    }
}
