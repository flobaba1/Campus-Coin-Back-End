package com.campuscoin.backend.dto.note;

import com.campuscoin.backend.entity.Note;

import java.time.LocalDateTime;

public record NoteResponse(
        String noteId,
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static NoteResponse from(Note note) {
        return new NoteResponse(
                note.getNoteId(),
                note.getTitle(),
                note.getContent(),
                note.getCreatedAt(),
                note.getUpdatedAt()
        );
    }
}
