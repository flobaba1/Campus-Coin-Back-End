package com.campuscoin.backend.repository;

import com.campuscoin.backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, String> {

    List<Category> findByDefaultCategoryTrue();

    List<Category> findByCreatedBy(String createdBy);

    List<Category> findByDefaultCategoryTrueOrCreatedBy(String createdBy);

    Optional<Category> findByCategoryIdAndCreatedBy(
            String categoryId,
            String createdBy
    );

    boolean existsByNameIgnoreCaseAndCreatedBy(
            String name,
            String createdBy
    );

    boolean existsByNameIgnoreCaseAndDefaultCategoryTrue(
            String name
    );
}
