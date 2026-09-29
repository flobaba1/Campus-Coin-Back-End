package com.campuscoin.backend.service.analytics;

import com.campuscoin.backend.ai.contexts.MonthFinancialContext;
import com.campuscoin.backend.entity.Budget;
import com.campuscoin.backend.entity.Transaction;
import com.campuscoin.backend.entity.User;
import com.campuscoin.backend.enums.TransactionType;
import com.campuscoin.backend.repository.BudgetRepository;
import com.campuscoin.backend.repository.TransactionRepository;
import com.campuscoin.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class MonthlyFinancialAnalytics {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final BudgetRepository budgetRepository;

    public MonthlyFinancialAnalytics(
            UserRepository userRepository,
            TransactionRepository transactionRepository,
            BudgetRepository budgetRepository
    ) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.budgetRepository = budgetRepository;
    }

    @Transactional(readOnly = true)
    public MonthFinancialContext generateContext(
            String userId,
            int yearInView,
            int monthInView
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        LocalDate startDate = LocalDate.of(
                yearInView,
                monthInView,
                1
        );

        LocalDate endDate = startDate
                .withDayOfMonth(startDate.lengthOfMonth());

        List<Transaction> transactions =
                transactionRepository
                        .findByUser_UserIdAndDateBetweenOrderByDateDesc(
                                userId,
                                startDate,
                                endDate
                        );

        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpenses = BigDecimal.ZERO;

        Map<String, BigDecimal> spendingByCategory =
                new LinkedHashMap<>();

        for (Transaction transaction : transactions) {

            BigDecimal amount = transaction.getAmount();

            if (transaction.getType() == TransactionType.INCOME) {

                totalIncome = totalIncome.add(amount);

            } else if (transaction.getType() == TransactionType.EXPENSE) {

                totalExpenses = totalExpenses.add(amount);

                String categoryName =
                        transaction.getCategory().getName();

                spendingByCategory.merge(
                        categoryName,
                        amount,
                        BigDecimal::add
                );
            }
        }

        BigDecimal totalSavings =
                totalIncome.subtract(totalExpenses);

        List<Budget> budgets =
                budgetRepository.findEffectiveBudgets(
                        userId,
                        yearInView,
                        monthInView
                );

        BigDecimal totalBudget = BigDecimal.ZERO;

        Map<String, BigDecimal> budgetByCategory =
                new LinkedHashMap<>();

        for (Budget budget : budgets) {

            BigDecimal amount = budget.getAmount();

            totalBudget = totalBudget.add(amount);

            String categoryName =
                    budget.getCategory().getName();

            budgetByCategory.put(
                    categoryName,
                    amount
            );
        }

        BigDecimal monthlyIncome =
                user.getMonthlyIncome() != null
                        ? user.getMonthlyIncome()
                        : BigDecimal.ZERO;

        BigDecimal savingsGoal =
                user.getMonthlySavingsGoal() != null
                        ? user.getMonthlySavingsGoal()
                        : BigDecimal.ZERO;

        return new MonthFinancialContext(
                yearInView,
                monthInView,
                totalIncome,
                monthlyIncome,
                totalBudget,
                savingsGoal,
                totalExpenses,
                totalSavings,
                spendingByCategory,
                budgetByCategory,
                transactions.size()
        );
    }
}