package com.campuscoin.backend.dto.transaction;

import com.campuscoin.backend.entity.Transaction;
import com.campuscoin.backend.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TransactionResponse {

    private String transactionId;
    private String categoryId;
    private String categoryName;
    private BigDecimal amount;
    private TransactionType type;
    private String description;
    private LocalDate date;
    private LocalDateTime createdAt;

    public TransactionResponse() {
    }

    public TransactionResponse(
            String transactionId,
            String categoryId,
            String categoryName,
            BigDecimal amount,
            TransactionType type,
            String description,
            LocalDate date,
            LocalDateTime createdAt
    ) {
        this.transactionId = transactionId;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.date = date;
        this.createdAt = createdAt;
    }

    public static TransactionResponse fromEntity(Transaction transaction) {
        return new TransactionResponse(
                transaction.getTransactionId(),
                transaction.getCategory().getCategoryId(),
                transaction.getCategory().getName(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getDescription(),
                transaction.getDate(),
                transaction.getCreatedAt()
        );
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
