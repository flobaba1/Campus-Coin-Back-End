package com.campuscoin.backend.repository;

import com.campuscoin.backend.entity.Transaction;
import com.campuscoin.backend.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

    List<Transaction> findByUser_UserIdOrderByDateDescCreatedAtDesc(
            String userId
    );

    Optional<Transaction> findByTransactionIdAndUser_UserId(
            String transactionId,
            String userId
    );

    List<Transaction> findByUser_UserIdAndTypeOrderByDateDescCreatedAtDesc(
            String userId,
            TransactionType type
    );

    List<Transaction> findByUser_UserIdAndDateBetweenOrderByDateDescCreatedAtDesc(
            String userId,
            LocalDate startDate,
            LocalDate endDate
    );

    List<Transaction> findByUser_UserIdAndTypeAndDateBetweenOrderByDateDescCreatedAtDesc(
            String userId,
            TransactionType type,
            LocalDate startDate,
            LocalDate endDate
    );

    long countByUser_UserId(String userId);

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
    SELECT t.category.name, COUNT(t)
    FROM Transaction t
    WHERE t.date BETWEEN :startDate AND :endDate
    GROUP BY t.category.name
    ORDER BY COUNT(t) DESC
""")
    List<Object[]> countTransactionsByCategory(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
