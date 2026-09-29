package com.campuscoin.backend.service;

import com.campuscoin.backend.dto.CategoryAnalyticsResponse;
import com.campuscoin.backend.dto.DailyActivityResponse;
import com.campuscoin.backend.dto.DailyTransactionResponse;
import com.campuscoin.backend.repository.StudentDailyActivityRepository;

import com.campuscoin.backend.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminAnalyticsService {

    private final StudentDailyActivityRepository activityRepository;
    private final TransactionRepository transactionRepository;

    public AdminAnalyticsService(
            StudentDailyActivityRepository activityRepository , TransactionRepository transactionRepository
    ) {
        this.activityRepository = activityRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional(readOnly = true)
    public List<DailyActivityResponse> getDailyActiveStudents(int days) {

        if (days < 1 || days > 365) {
            throw new IllegalArgumentException(
                    "Days must be between 1 and 365"
            );
        }

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(days - 1);

        List<DailyActivityResponse> results = new ArrayList<>();

        for (int i = 0; i < days; i++) {

            LocalDate date = startDate.plusDays(i);

            long count = activityRepository.countByActivityDate(date);

            results.add(
                    new DailyActivityResponse(date, count)
            );
        }

        return results;
    }
    @Transactional(readOnly = true)
    public List<DailyTransactionResponse> getDailyTransactions(int days) {

        if (days < 1 || days > 365) {
            throw new IllegalArgumentException(
                    "Days must be between 1 and 365"
            );
        }

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(days - 1);

        List<Object[]> results =
                transactionRepository.countTransactionsByDate(
                        startDate,
                        today
                );

        Map<LocalDate, Long> countsByDate = new HashMap<>();

        for (Object[] row : results) {
            LocalDate date = (LocalDate) row[0];
            Long count = ((Number) row[1]).longValue();

            countsByDate.put(date, count);
        }

        List<DailyTransactionResponse> response = new ArrayList<>();

        for (int i = 0; i < days; i++) {
            LocalDate date = startDate.plusDays(i);

            long count = countsByDate.getOrDefault(date, 0L);

            response.add(
                    new DailyTransactionResponse(date, count)
            );
        }

        return response;
    }

    @Transactional(readOnly = true)
    public List<CategoryAnalyticsResponse> getCategoryAnalytics(int days) {

        if (days < 1 || days > 365) {
            throw new IllegalArgumentException(
                    "Days must be between 1 and 365"
            );
        }

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(days - 1);

        List<Object[]> results =
                transactionRepository.countTransactionsByCategory(
                        startDate,
                        today
                );

        long totalTransactions = 0;

        for (Object[] row : results) {
            totalTransactions += ((Number) row[1]).longValue();
        }

        List<CategoryAnalyticsResponse> response = new ArrayList<>();

        for (Object[] row : results) {

            String categoryName = (String) row[0];
            long count = ((Number) row[1]).longValue();

            double percentage = totalTransactions == 0
                    ? 0.0
                    : (count * 100.0) / totalTransactions;

            response.add(
                    new CategoryAnalyticsResponse(
                            categoryName,
                            count,
                            percentage
                    )
            );
        }

        return response;
    }
}