package com.campuscoin.backend.service;

import com.campuscoin.backend.dto.transaction.CreateTransactionRequest;
import com.campuscoin.backend.dto.transaction.TransactionResponse;
import com.campuscoin.backend.dto.transaction.UpdateTransactionRequest;
import com.campuscoin.backend.entity.Category;
import com.campuscoin.backend.entity.Transaction;
import com.campuscoin.backend.entity.User;
import com.campuscoin.backend.enums.TransactionType;
import com.campuscoin.backend.repository.CategoryRepository;
import com.campuscoin.backend.repository.TransactionRepository;
import com.campuscoin.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public TransactionService(
            TransactionRepository transactionRepository,
            CategoryRepository categoryRepository,
            UserRepository userRepository
    ) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public List<TransactionResponse> getTransactions(String userId) {

        return transactionRepository
                .findByUser_UserIdOrderByDateDescCreatedAtDesc(userId)
                .stream()
                .map(TransactionResponse::fromEntity)
                .toList();
    }

    public TransactionResponse createTransaction(
            String userId,
            CreateTransactionRequest request
    ) {

        User user = findUser(userId);

        Category category = findAccessibleCategory(
                userId,
                request.getCategoryId()
        );

        validateCategoryType(
                category,
                request.getType()
        );

        Transaction transaction = new Transaction();

        transaction.setUser(user);
        transaction.setCategory(category);
        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setDescription(
                normalizeDescription(request.getDescription())
        );
        transaction.setDate(request.getDate());

        Transaction savedTransaction =
                transactionRepository.save(transaction);

        return TransactionResponse.fromEntity(savedTransaction);
    }

    public TransactionResponse updateTransaction(
            String userId,
            String transactionId,
            UpdateTransactionRequest request
    ) {

        Transaction transaction =
                transactionRepository
                        .findByTransactionIdAndUser_UserId(
                                transactionId,
                                userId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Transaction not found"
                                )
                        );

        Category category = findAccessibleCategory(
                userId,
                request.getCategoryId()
        );

        validateCategoryType(
                category,
                request.getType()
        );

        transaction.setCategory(category);
        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setDescription(
                normalizeDescription(request.getDescription())
        );
        transaction.setDate(request.getDate());

        Transaction updatedTransaction =
                transactionRepository.save(transaction);

        return TransactionResponse.fromEntity(updatedTransaction);
    }

    public void deleteTransaction(
            String userId,
            String transactionId
    ) {

        Transaction transaction =
                transactionRepository
                        .findByTransactionIdAndUser_UserId(
                                transactionId,
                                userId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Transaction not found"
                                )
                        );

        transactionRepository.delete(transaction);
    }

    private User findUser(String userId) {

        return userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found"
                        )
                );
    }

    private Category findAccessibleCategory(
            String userId,
            String categoryId
    ) {

        return categoryRepository
                .findByDefaultCategoryTrueOrCreatedBy(userId)
                .stream()
                .filter(category ->
                        category.getCategoryId().equals(categoryId)
                )
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Category not found or not accessible"
                        )
                );
    }

    private void validateCategoryType(
            Category category,
            TransactionType transactionType
    ) {

        if (category.getType() != transactionType) {
            throw new IllegalArgumentException(
                    "Category type must match transaction type"
            );
        }
    }

    private String normalizeDescription(String description) {

        if (description == null) {
            return null;
        }

        String trimmed = description.trim();

        return trimmed.isEmpty() ? null : trimmed;
    }

    public BigDecimal calculateSpent(
            String userId,
            String categoryId,
            Integer year,
            Integer month
    ) {

        LocalDate startDate = LocalDate.of(year, month, 1);

        LocalDate endDate =
                startDate.withDayOfMonth(startDate.lengthOfMonth());

        return transactionRepository
                .calculateSpent(
                        userId,
                        categoryId,
                        TransactionType.EXPENSE,
                        startDate,
                        endDate
                )
                .setScale(2, RoundingMode.HALF_UP);
    }
}