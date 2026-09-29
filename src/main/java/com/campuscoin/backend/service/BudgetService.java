package com.campuscoin.backend.service;

import com.campuscoin.backend.dto.BudgetRequest;
import com.campuscoin.backend.dto.BudgetResponse;
import com.campuscoin.backend.entity.Budget;
import com.campuscoin.backend.entity.Category;
import com.campuscoin.backend.entity.Transaction;
import com.campuscoin.backend.entity.User;
import com.campuscoin.backend.enums.TransactionType;
import com.campuscoin.backend.repository.BudgetRepository;
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
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public BudgetService(
            BudgetRepository budgetRepository,
            CategoryRepository categoryRepository,
            TransactionRepository transactionRepository,
            UserRepository userRepository
    ) {
        this.budgetRepository = budgetRepository;
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    // =========================================================
    // GET ALL BUDGETS FOR CURRENT USER
    // =========================================================

    @Transactional
    public List<BudgetResponse> getBudgets(String userId) {

        List<Budget> budgets =
                budgetRepository.findByUser_UserIdOrderByYearDescMonthDesc(
                        userId
                );

        return budgets.stream()
                .map(budget -> toResponse(budget, userId))
                .toList();
    }

    // =========================================================
    // GET BUDGETS FOR A SPECIFIC MONTH
    // =========================================================

    @Transactional
    public List<BudgetResponse> getBudgetsForMonth(
            String userId,
            Integer year,
            Integer month
    ) {

        validateMonthAndYear(year, month);

        List<Budget> budgets =
                budgetRepository
                        .findByUser_UserIdAndYearAndMonthOrderByCategory_NameAsc(
                                userId,
                                year,
                                month
                        );

        return budgets.stream()
                .map(budget -> toResponse(budget, userId))
                .toList();
    }

    // =========================================================
    // CREATE
    // =========================================================

    @Transactional
    public BudgetResponse createBudget(
            String userId,
            BudgetRequest request
    ) {

        validateMonthAndYear(
                request.getYear(),
                request.getMonth()
        );

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        Category category = getAccessibleCategory(
                request.getCategoryId(),
                userId
        );

        validateExpenseCategory(category);

        boolean exists =
                budgetRepository
                        .existsByUser_UserIdAndCategory_CategoryIdAndMonthAndYear(
                                userId,
                                request.getCategoryId(),
                                request.getMonth(),
                                request.getYear()
                        );

        if (exists) {
            throw new RuntimeException(
                    "A budget already exists for this category and month"
            );
        }

        Budget budget = new Budget();

        budget.setUser(user);
        budget.setCategory(category);
        budget.setAmount(request.getAmount());
        budget.setMonth(request.getMonth());
        budget.setYear(request.getYear());

        Budget savedBudget = budgetRepository.save(budget);

        return toResponse(savedBudget, userId);
    }

    // =========================================================
    // UPDATE
    // =========================================================

    @Transactional
    public BudgetResponse updateBudget(
            String userId,
            String budgetId,
            BudgetRequest request
    ) {

        validateMonthAndYear(
                request.getYear(),
                request.getMonth()
        );

        Budget budget =
                budgetRepository
                        .findByBudgetIdAndUser_UserId(
                                budgetId,
                                userId
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Budget not found"
                                )
                        );

        Category category = getAccessibleCategory(
                request.getCategoryId(),
                userId
        );

        validateExpenseCategory(category);

        boolean categoryChanged =
                !budget.getCategory()
                        .getCategoryId()
                        .equals(request.getCategoryId());

        boolean periodChanged =
                !budget.getMonth().equals(request.getMonth())
                        || !budget.getYear().equals(request.getYear());

        if (categoryChanged || periodChanged) {

            boolean exists =
                    budgetRepository
                            .existsByUser_UserIdAndCategory_CategoryIdAndMonthAndYear(
                                    userId,
                                    request.getCategoryId(),
                                    request.getMonth(),
                                    request.getYear()
                            );

            if (exists) {
                throw new RuntimeException(
                        "A budget already exists for this category and month"
                );
            }
        }

        budget.setCategory(category);
        budget.setAmount(request.getAmount());
        budget.setMonth(request.getMonth());
        budget.setYear(request.getYear());

        Budget updatedBudget =
                budgetRepository.save(budget);

        return toResponse(updatedBudget, userId);
    }

    // =========================================================
    // DELETE
    // =========================================================

    @Transactional
    public void deleteBudget(
            String userId,
            String budgetId
    ) {

        Budget budget =
                budgetRepository
                        .findByBudgetIdAndUser_UserId(
                                budgetId,
                                userId
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Budget not found"
                                )
                        );

        budgetRepository.delete(budget);
    }

    // =========================================================
    // BUILD RESPONSE
    // =========================================================

    private BudgetResponse toResponse(
            Budget budget,
            String userId
    ) {

        BigDecimal spent =
                calculateSpent(
                        userId,
                        budget.getCategory().getCategoryId(),
                        budget.getYear(),
                        budget.getMonth()
                );

        BigDecimal amount = budget.getAmount();

        BigDecimal remaining =
                amount.subtract(spent);

        BigDecimal percentageUsed;

        if (amount.compareTo(BigDecimal.ZERO) == 0) {

            percentageUsed = BigDecimal.ZERO;

        } else {

            percentageUsed =
                    spent
                            .divide(
                                    amount,
                                    4,
                                    RoundingMode.HALF_UP
                            )
                            .multiply(BigDecimal.valueOf(100))
                            .setScale(
                                    2,
                                    RoundingMode.HALF_UP
                            );
        }

        String status = determineStatus(
                spent,
                amount
        );

        return new BudgetResponse(
                budget.getBudgetId(),
                budget.getCategory().getCategoryId(),
                budget.getCategory().getName(),
                amount,
                spent,
                remaining,
                percentageUsed,
                status,
                budget.getMonth(),
                budget.getYear()
        );
    }

    // =========================================================
    // CALCULATE SPENDING
    // =========================================================

    private BigDecimal calculateSpent(
            String userId,
            String categoryId,
            Integer year,
            Integer month
    ) {

        LocalDate startDate =
                LocalDate.of(
                        year,
                        month,
                        1
                );

        LocalDate endDate =
                startDate.withDayOfMonth(
                        startDate.lengthOfMonth()
                );

        List<Transaction> transactions =
                transactionRepository
                        .findByUser_UserIdAndTypeAndDateBetweenOrderByDateDescCreatedAtDesc(
                                userId,
                                TransactionType.EXPENSE,
                                startDate,
                                endDate
                        );

        return transactions.stream()
                .filter(transaction ->
                        transaction.getCategory()
                                .getCategoryId()
                                .equals(categoryId)
                )
                .map(Transaction::getAmount)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                )
                .setScale(
                        2,
                        RoundingMode.HALF_UP
                );
    }

    // =========================================================
    // STATUS
    // =========================================================

    private String determineStatus(
            BigDecimal spent,
            BigDecimal budget
    ) {

        if (spent.compareTo(budget) > 0) {
            return "OVER_BUDGET";
        }

        if (spent.compareTo(budget) == 0) {
            return "AT_LIMIT";
        }

        BigDecimal percentage =
                spent
                        .divide(
                                budget,
                                4,
                                RoundingMode.HALF_UP
                        )
                        .multiply(BigDecimal.valueOf(100));

        if (percentage.compareTo(BigDecimal.valueOf(80)) >= 0) {
            return "APPROACHING_LIMIT";
        }

        return "ON_TRACK";
    }

    // =========================================================
    // CATEGORY VALIDATION
    // =========================================================

    private Category getAccessibleCategory(
            String categoryId,
            String userId
    ) {

        return categoryRepository
                .findByCategoryIdAndCreatedBy(
                        categoryId,
                        userId
                )
                .orElseGet(() ->
                        categoryRepository
                                .findById(categoryId)
                                .filter(Category::isDefaultCategory)
                                .orElseThrow(() ->
                                        new RuntimeException(
                                                "Category not found or not accessible"
                                        )
                                )
                );
    }

    private void validateExpenseCategory(
            Category category
    ) {

        if (category.getType() != TransactionType.EXPENSE) {
            throw new RuntimeException(
                    "Only expense categories can have budgets"
            );
        }
    }

    // =========================================================
    // DATE VALIDATION
    // =========================================================

    private void validateMonthAndYear(
            Integer year,
            Integer month
    ) {

        if (month == null || month < 1 || month > 12) {
            throw new RuntimeException(
                    "Month must be between 1 and 12"
            );
        }

        if (year == null || year < 2000 || year > 2100) {
            throw new RuntimeException(
                    "Year must be between 2000 and 2100"
            );
        }
    }
}