package com.campuscoin.backend.repository;

import com.campuscoin.backend.entity.Transaction;
import com.campuscoin.backend.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

    List<Transaction> findByUser_UserIdOrderByDateDescCreatedAtDesc(
            String userId
    );

    List<Transaction> findByUser_UserIdAndDateBetweenOrderByDateDesc(
            String userId,
            LocalDate startDate,
            LocalDate endDate
    );

    Optional<Transaction> findByTransactionIdAndUser_UserId(
            String transactionId,
            String userId
    );

    @Query("""
        SELECT COALESCE(SUM(t.amount), 0)
        FROM Transaction t
        WHERE t.user.userId = :userId
          AND t.category.categoryId = :categoryId
          AND t.type = :type
          AND t.date BETWEEN :startDate AND :endDate
        """)
    BigDecimal calculateSpent(
            @Param("userId") String userId,
            @Param("categoryId") String categoryId,
            @Param("type") TransactionType type,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
        SELECT t.date, COUNT(t)
        FROM Transaction t
        WHERE t.date BETWEEN :startDate AND :endDate
        GROUP BY t.date
        ORDER BY t.date ASC
        """)
    List<Object[]> countTransactionsByDate(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
    SELECT t.category.categoryId, COUNT(t)
    FROM Transaction t
    WHERE t.date BETWEEN :startDate AND :endDate
    GROUP BY t.category.categoryId
    ORDER BY COUNT(t) DESC
    """)
    List<Object[]> countTransactionsByCategory(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}