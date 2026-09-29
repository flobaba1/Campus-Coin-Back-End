package com.campuscoin.backend.repository;

import com.campuscoin.backend.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository
        extends JpaRepository<Transaction, String> {

    List<Transaction> findByUser_UserIdAndDateBetweenOrderByDateDesc(
            String userId,
            java.time.LocalDate startDate,
            java.time.LocalDate endDate
    );
}
