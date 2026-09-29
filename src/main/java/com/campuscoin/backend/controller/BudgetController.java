package com.campuscoin.backend.controller;

import com.campuscoin.backend.dto.BudgetRequest;
import com.campuscoin.backend.dto.BudgetResponse;
import com.campuscoin.backend.service.BudgetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(
            BudgetService budgetService
    ) {
        this.budgetService = budgetService;
    }

    // =========================================================
    // GET ALL BUDGETS
    // =========================================================

    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getBudgets(
            Authentication authentication
    ) {

        String userId = authentication.getName();

        return ResponseEntity.ok(
                budgetService.getBudgets(userId)
        );
    }

    // =========================================================
    // GET BUDGETS FOR MONTH
    // =========================================================

    @GetMapping("/month")
    public ResponseEntity<List<BudgetResponse>> getBudgetsForMonth(
            @RequestParam Integer year,
            @RequestParam Integer month,
            Authentication authentication
    ) {

        String userId = authentication.getName();

        return ResponseEntity.ok(
                budgetService.getBudgetsForMonth(
                        userId,
                        year,
                        month
                )
        );
    }

    // =========================================================
    // CREATE
    // =========================================================

    @PostMapping
    public ResponseEntity<BudgetResponse> createBudget(
            @Valid @RequestBody BudgetRequest request,
            Authentication authentication
    ) {

        String userId = authentication.getName();

        BudgetResponse response =
                budgetService.createBudget(
                        userId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =========================================================
    // UPDATE
    // =========================================================

    @PutMapping("/{budgetId}")
    public ResponseEntity<BudgetResponse> updateBudget(
            @PathVariable String budgetId,
            @Valid @RequestBody BudgetRequest request,
            Authentication authentication
    ) {

        String userId = authentication.getName();

        BudgetResponse response =
                budgetService.updateBudget(
                        userId,
                        budgetId,
                        request
                );

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // DELETE
    // =========================================================

    @DeleteMapping("/{budgetId}")
    public ResponseEntity<Map<String, String>> deleteBudget(
            @PathVariable String budgetId,
            Authentication authentication
    ) {

        String userId = authentication.getName();

        budgetService.deleteBudget(
                userId,
                budgetId
        );

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Budget deleted successfully"
                )
        );
    }
}
