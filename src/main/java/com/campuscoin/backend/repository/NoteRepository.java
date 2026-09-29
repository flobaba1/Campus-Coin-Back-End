package com.campuscoin.backend.repository;

import com.campuscoin.backend.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepository
        extends JpaRepository<Note, String> {

    List<Note> findByUser_UserIdOrderByCreatedAtDesc(
            String userId
    );

    Optional<Note> findByNoteIdAndUser_UserId(
            String noteId,
            String userId
    );
}