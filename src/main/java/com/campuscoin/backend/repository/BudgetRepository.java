package com.campuscoin.backend.repository;

import com.campuscoin.backend.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, String> {

    /*
     * Get all budgets belonging to a user,
     * newest year/month first.
     */
    List<Budget> findByUserIdOrderByYearDescMonthDesc(
            String userId
    );


    /*
     * Get budgets belonging to a user
     * for a specific month and year.
     */
    List<Budget> findByUserIdAndYearAndMonthOrderByCategory_NameAsc(
            String userId,
            int year,
            int month
    );


    /*
     * Find a specific budget belonging to a specific user.
     */
    Optional<Budget> findByBudgetIdAndUserId(
            String budgetId,
            String userId
    );


    /*
     * Check whether a budget already exists for:
     *
     * user + category + month + year
     */
    boolean existsByUserIdAndCategory_CategoryIdAndMonthAndYear(
            String userId,
            String categoryId,
            int month,
            int year
    );


    /*
     * Get the effective budget for each category
     * as of the requested month/year.
     *
     * If a category has budgets for:
     *
     * January 2026
     * February 2026
     * March 2026
     *
     * and the requested report is March 2026,
     * March's budget is used.
     *
     * If there is no March budget but February exists,
     * February's budget is used.
     */
    @Query("""
        SELECT b
        FROM Budget b
        WHERE b.userId = :userId
          AND (
                b.year < :year
                OR (
                    b.year = :year
                    AND b.month <= :month
                )
              )
          AND NOT EXISTS (
                SELECT newer
                FROM Budget newer
                WHERE newer.userId = b.userId
                  AND newer.category = b.category
                  AND (
                        newer.year > b.year
                        OR (
                            newer.year = b.year
                            AND newer.month > b.month
                        )
                      )
                  AND (
                        newer.year < :year
                        OR (
                            newer.year = :year
                            AND newer.month <= :month
                        )
                      )
          )
        ORDER BY b.category.name
        """)
    List<Budget> findEffectiveBudgets(
            @Param("userId") String userId,
            @Param("year") int year,
            @Param("month") int month
    );
}