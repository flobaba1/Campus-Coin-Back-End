package com.campuscoin.backend.controller;

import com.campuscoin.backend.dto.BookmarkRequest;
import com.campuscoin.backend.dto.BookmarkResponse;
import com.campuscoin.backend.enums.BookmarkType;
import com.campuscoin.backend.service.BookmarkService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    public BookmarkController(
            BookmarkService bookmarkService
    ) {
        this.bookmarkService = bookmarkService;
    }

    // =========================================================
    // GET ALL
    // =========================================================

    @GetMapping
    public ResponseEntity<List<BookmarkResponse>> getBookmarks(
            Authentication authentication
    ) {

        String userId =
                authentication.getName();

        return ResponseEntity.ok(
                bookmarkService.getBookmarks(
                        userId
                )
        );
    }

    // =========================================================
    // GET BY TYPE
    // =========================================================

    @GetMapping("/type/{type}")
    public ResponseEntity<List<BookmarkResponse>>
    getBookmarksByType(
            @PathVariable BookmarkType type,
            Authentication authentication
    ) {

        String userId =
                authentication.getName();

        return ResponseEntity.ok(
                bookmarkService.getBookmarksByType(
                        userId,
                        type
                )
        );
    }

    // =========================================================
    // CREATE
    // =========================================================

    @PostMapping
    public ResponseEntity<BookmarkResponse>
    createBookmark(
            @Valid @RequestBody BookmarkRequest request,
            Authentication authentication
    ) {

        String userId =
                authentication.getName();

        BookmarkResponse response =
                bookmarkService.createBookmark(
                        userId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =========================================================
    // DELETE
    // =========================================================

    @DeleteMapping("/{bookmarkId}")
    public ResponseEntity<Map<String, String>>
    deleteBookmark(
            @PathVariable String bookmarkId,
            Authentication authentication
    ) {

        String userId =
                authentication.getName();

        bookmarkService.deleteBookmark(
                userId,
                bookmarkId
        );

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Bookmark deleted successfully"
                )
        );
    }
}