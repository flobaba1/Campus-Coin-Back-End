package com.campuscoin.backend.repository;

import com.campuscoin.backend.entity.Transaction;
import com.campuscoin.backend.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
