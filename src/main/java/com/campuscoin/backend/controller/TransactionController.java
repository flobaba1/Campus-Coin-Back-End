package com.campuscoin.backend.controller;

import com.campuscoin.backend.dto.transaction.CreateTransactionRequest;
import com.campuscoin.backend.dto.transaction.TransactionResponse;
import com.campuscoin.backend.dto.transaction.UpdateTransactionRequest;
import com.campuscoin.backend.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(
            TransactionService transactionService
    ) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getTransactions(
            Authentication authentication
    ) {

        String userId = authentication.getName();

        return ResponseEntity.ok(
                transactionService.getTransactions(userId)
        );
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            Authentication authentication,
            @Valid @RequestBody CreateTransactionRequest request
    ) {

        String userId = authentication.getName();

        TransactionResponse response =
                transactionService.createTransaction(
                        userId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> updateTransaction(
            Authentication authentication,
            @PathVariable String transactionId,
            @Valid @RequestBody UpdateTransactionRequest request
    ) {

        String userId = authentication.getName();

        TransactionResponse response =
                transactionService.updateTransaction(
                        userId,
                        transactionId,
                        request
                );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(
            Authentication authentication,
            @PathVariable String transactionId
    ) {

        String userId = authentication.getName();

        transactionService.deleteTransaction(
                userId,
                transactionId
        );

        return ResponseEntity.noContent().build();
    }
}
