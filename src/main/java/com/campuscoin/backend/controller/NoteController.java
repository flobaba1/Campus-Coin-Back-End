package com.campuscoin.backend.controller;

import com.campuscoin.backend.dto.note.NoteRequest;
import com.campuscoin.backend.dto.note.NoteResponse;
import com.campuscoin.backend.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    public ResponseEntity<List<NoteResponse>> getNotes(
            Authentication authentication
    ) {

        String userId = authentication.getName();

        return ResponseEntity.ok(
                noteService.getNotes(userId)
        );
    }

    @PostMapping
    public ResponseEntity<NoteResponse> createNote(
            @Valid @RequestBody NoteRequest request,
            Authentication authentication
    ) {

        String userId = authentication.getName();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        noteService.createNote(
                                userId,
                                request
                        )
                );
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<NoteResponse> updateNote(
            @PathVariable String noteId,
            @Valid @RequestBody NoteRequest request,
            Authentication authentication
    ) {

        String userId = authentication.getName();

        return ResponseEntity.ok(
                noteService.updateNote(
                        userId,
                        noteId,
                        request
                )
        );
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<Map<String, String>> deleteNote(
            @PathVariable String noteId,
            Authentication authentication
    ) {

        String userId = authentication.getName();

        noteService.deleteNote(
                userId,
                noteId
        );

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Note deleted successfully"
                )
        );
    }
}
