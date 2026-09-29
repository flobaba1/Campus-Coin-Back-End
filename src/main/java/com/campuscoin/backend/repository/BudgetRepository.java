package com.campuscoin.backend.repository;

import com.campuscoin.backend.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, String> {


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