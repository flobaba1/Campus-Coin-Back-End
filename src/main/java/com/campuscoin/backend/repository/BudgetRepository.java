package com.campuscoin.backend.repository;

import com.campuscoin.backend.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, String> {

    List<Budget> findByUser_UserIdOrderByYearDescMonthDesc(
            String userId
    );

    List<Budget> findByUser_UserIdAndYearAndMonthOrderByCategory_NameAsc(
            String userId,
            Integer year,
            Integer month
    );

    Optional<Budget> findByBudgetIdAndUser_UserId(
            String budgetId,
            String userId
    );

    Optional<Budget> findByUser_UserIdAndCategory_CategoryIdAndMonthAndYear(
            String userId,
            String categoryId,
            Integer month,
            Integer year
    );

    boolean existsByUser_UserIdAndCategory_CategoryIdAndMonthAndYear(
            String userId,
            String categoryId,
            Integer month,
            Integer year
    );
}
