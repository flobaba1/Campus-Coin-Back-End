package com.campuscoin.backend.controller;

import com.campuscoin.backend.dto.CategoryAnalyticsResponse;
import com.campuscoin.backend.dto.DailyActivityResponse;
import com.campuscoin.backend.dto.DailyTransactionResponse;
import com.campuscoin.backend.service.AdminAnalyticsService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/analytics")
public class AdminAnalyticsController {

    private final AdminAnalyticsService analyticsService;


    public AdminAnalyticsController(
            AdminAnalyticsService analyticsService
    ) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/active-students")
    public List<DailyActivityResponse> getActiveStudents(
            @RequestParam(defaultValue = "30") int days
    ) {
        return analyticsService.getDailyActiveStudents(days);
    }

    @GetMapping("/transactions/daily")
    public List<DailyTransactionResponse> getDailyTransactions(
            @RequestParam(defaultValue = "14") int days
    ) {
        return analyticsService.getDailyTransactions(days);
    }
    @GetMapping("/transactions/categories")
    public List<CategoryAnalyticsResponse> getCategoryAnalytics(
            @RequestParam(defaultValue = "30") int days
    ) {
        return analyticsService.getCategoryAnalytics(days);
    }
}