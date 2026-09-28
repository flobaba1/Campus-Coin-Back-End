package com.campuscoin.backend.service;

import com.campuscoin.backend.dto.BookmarkRequest;
import com.campuscoin.backend.dto.BookmarkResponse;
import com.campuscoin.backend.entity.Bookmark;
import com.campuscoin.backend.entity.User;
import com.campuscoin.backend.enums.BookmarkType;
import com.campuscoin.backend.repository.BookmarkRepository;
import com.campuscoin.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;

    public BookmarkService(
            BookmarkRepository bookmarkRepository,
            UserRepository userRepository
    ) {
        this.bookmarkRepository = bookmarkRepository;
        this.userRepository = userRepository;
    }

    // =========================================================
    // GET ALL BOOKMARKS
    // =========================================================

    @Transactional
    public List<BookmarkResponse> getBookmarks(
            String userId
    ) {

        return bookmarkRepository
                .findByUser_UserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =========================================================
    // GET BY TYPE
    // =========================================================

    @Transactional
    public List<BookmarkResponse> getBookmarksByType(
            String userId,
            BookmarkType type
    ) {

        return bookmarkRepository
                .findByUser_UserIdAndTypeOrderByCreatedAtDesc(
                        userId,
                        type
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =========================================================
    // CREATE
    // =========================================================

    @Transactional
    public BookmarkResponse createBookmark(
            String userId,
            BookmarkRequest request
    ) {

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );

        String title =
                request.getTitle().trim();

        String content =
                request.getContent().trim();

        boolean exists =
                bookmarkRepository
                        .existsByUser_UserIdAndTypeAndTitle(
                                userId,
                                request.getType(),
                                title
                        );

        if (exists) {
            throw new RuntimeException(
                    "This bookmark already exists"
            );
        }

        Bookmark bookmark =
                new Bookmark();

        bookmark.setUser(user);
        bookmark.setType(
                request.getType()
        );
        bookmark.setTitle(title);
        bookmark.setContent(content);

        Bookmark saved =
                bookmarkRepository.save(
                        bookmark
                );

        return toResponse(saved);
    }

    // =========================================================
    // DELETE
    // =========================================================

    @Transactional
    public void deleteBookmark(
            String userId,
            String bookmarkId
    ) {

        Bookmark bookmark =
                bookmarkRepository
                        .findByBookmarkIdAndUser_UserId(
                                bookmarkId,
                                userId
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Bookmark not found"
                                )
                        );

        bookmarkRepository.delete(bookmark);
    }

    // =========================================================
    // RESPONSE
    // =========================================================

    private BookmarkResponse toResponse(
            Bookmark bookmark
    ) {

        return new BookmarkResponse(
                bookmark.getBookmarkId(),
                bookmark.getType(),
                bookmark.getTitle(),
                bookmark.getContent(),
                bookmark.getCreatedAt(),
                bookmark.getUpdatedAt()
        );
    }
}