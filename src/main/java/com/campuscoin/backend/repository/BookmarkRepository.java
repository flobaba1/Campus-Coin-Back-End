package com.campuscoin.backend.repository;

import com.campuscoin.backend.entity.Bookmark;
import com.campuscoin.backend.enums.BookmarkType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository
        extends JpaRepository<Bookmark, String> {

    List<Bookmark>
    findByUser_UserIdOrderByCreatedAtDesc(
            String userId
    );

    List<Bookmark>
    findByUser_UserIdAndTypeOrderByCreatedAtDesc(
            String userId,
            BookmarkType type
    );

    Optional<Bookmark>
    findByBookmarkIdAndUser_UserId(
            String bookmarkId,
            String userId
    );

    boolean
    existsByUser_UserIdAndTypeAndTitle(
            String userId,
            BookmarkType type,
            String title
    );
}