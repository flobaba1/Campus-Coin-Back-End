package com.campuscoin.backend.service;

import com.campuscoin.backend.dto.note.NoteRequest;
import com.campuscoin.backend.dto.note.NoteResponse;
import com.campuscoin.backend.entity.Note;
import com.campuscoin.backend.entity.User;
import com.campuscoin.backend.repository.NoteRepository;
import com.campuscoin.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public NoteService(
            NoteRepository noteRepository,
            UserRepository userRepository
    ) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    public List<NoteResponse> getNotes(String userId) {

        return noteRepository
                .findByUser_UserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(NoteResponse::from)
                .toList();
    }

    @Transactional
    public NoteResponse createNote(
            String userId,
            NoteRequest request
    ) {

        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        Note note = new Note();

        note.setUser(user);
        note.setTitle(request.getTitle().trim());
        note.setContent(request.getContent().trim());

        return NoteResponse.from(
                noteRepository.save(note)
        );
    }

    @Transactional
    public NoteResponse updateNote(
            String userId,
            String noteId,
            NoteRequest request
    ) {

        Note note = noteRepository
                .findByNoteIdAndUser_UserId(
                        noteId,
                        userId
                )
                .orElseThrow(() ->
                        new RuntimeException("Note not found")
                );

        note.setTitle(request.getTitle().trim());
        note.setContent(request.getContent().trim());

        return NoteResponse.from(
                noteRepository.save(note)
        );
    }

    @Transactional
    public void deleteNote(
            String userId,
            String noteId
    ) {

        Note note = noteRepository
                .findByNoteIdAndUser_UserId(
                        noteId,
                        userId
                )
                .orElseThrow(() ->
                        new RuntimeException("Note not found")
                );

        noteRepository.delete(note);
    }
}
