package com.campuscoin.backend.dto;

import com.campuscoin.backend.enums.BookmarkType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class BookmarkRequest {

    @NotNull(message = "Bookmark type is required")
    private BookmarkType type;

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    public BookmarkRequest() {
    }

    public BookmarkType getType() {
        return type;
    }

    public void setType(BookmarkType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
