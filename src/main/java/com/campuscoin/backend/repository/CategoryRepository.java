package com.campuscoin.backend.repository;

import com.campuscoin.backend.entity.Category;
import com.campuscoin.backend.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, String> {

    List<Category> findByDefaultCategoryTrue();

    List<Category> findByCreatedBy(String createdBy);

    List<Category> findByDefaultCategoryTrueOrCreatedBy(String createdBy);

    @Query("""
    SELECT c
    FROM Category c
    WHERE c.type = :type
      AND (c.defaultCategory = true OR c.createdBy = :userId)
    """)
    List<Category> findAvailableCategoriesByType(
            @Param("userId") String userId,
            @Param("type") TransactionType type
    );

    Optional<Category> findByCategoryIdAndDefaultCategoryTrue(String categoryId);

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
