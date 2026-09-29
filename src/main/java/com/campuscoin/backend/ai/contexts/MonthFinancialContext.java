package com.campuscoin.backend.ai.contexts;

import java.math.BigDecimal;
import java.time.Month;
import java.util.Map;

public record MonthFinancialContext(
        int year,
        int month,
        BigDecimal totalMonthIncome,
        BigDecimal expectedMonthlyIncome,
        BigDecimal totalBudget,
        BigDecimal savingsGoal,
        BigDecimal totalExpenses,
        BigDecimal totalSavings,
        Map<String, BigDecimal> spendingByCategory,
        Map<String, BigDecimal> budgetByCategory,
        int transactionCount
) implements Comparable<MonthFinancialContext> {

    public String generateContext() {

        StringBuilder context = new StringBuilder();

        context.append(String.format(
                "MONTHLY FINANCIAL REPORT - %s %02d/%d%n",
                Month.of(month).name(),
                month,
                year
        ));

        context.append("----------------------------------------%n");

        context.append(String.format(
                "Total Income: %s%n",
                totalMonthIncome
        ));

        context.append(String.format(
                "Expected Monthly Income: %s%n",
                expectedMonthlyIncome
        ));

        context.append(String.format(
                "Total Expenses: %s%n",
                totalExpenses
        ));

        context.append(String.format(
                "Total Savings: %s%n",
                totalSavings
        ));

        context.append(String.format(
                "Savings Goal: %s%n",
                savingsGoal
        ));

        context.append(String.format(
                "Total Budget: %s%n",
                totalBudget
        ));

        context.append(String.format(
                "Transaction Count: %d%n",
                transactionCount
        ));

        context.append("%n");

        context.append("SPENDING BY CATEGORY%n");
        context.append("--------------------%n");

        if (spendingByCategory == null || spendingByCategory.isEmpty()) {
            context.append("No expenses recorded.%n");
        } else {
            spendingByCategory.forEach((category, amount) ->
                    context.append(String.format(
                            "- %s: %s%n",
                            category,
                            amount
                    ))
            );
        }

        context.append("%n");

        context.append("BUDGET BY CATEGORY%n");
        context.append("------------------%n");

        if (budgetByCategory == null || budgetByCategory.isEmpty()) {
            context.append("No budgets recorded.%n");
        } else {
            budgetByCategory.forEach((category, amount) ->
                    context.append(String.format(
                            "- %s: %s%n",
                            category,
                            amount
                    ))
            );
        }

        // Important: end with a newline so multiple contexts
        // can safely be concatenated.
        context.append("%n");

        return context.toString();
    }

    @Override
    public int compareTo(MonthFinancialContext other) {

        int yearComparison =
                Integer.compare(this.year, other.year);

        if (yearComparison != 0) {
            return yearComparison;
        }

        return Integer.compare(this.month, other.month);
    }
}