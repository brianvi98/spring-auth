package com.brianvi.spring_auth.note.controller;

import com.brianvi.spring_auth.note.dto.CreateNoteDto;
import com.brianvi.spring_auth.note.dto.NoteDto;
import com.brianvi.spring_auth.note.service.NoteService;
import com.brianvi.spring_auth.response.ApiResponse;
import com.brianvi.spring_auth.response.PageResponse;
import com.brianvi.spring_auth.user.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    private Long getAuthenticatedUserId() {
        return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<NoteDto>>> getNotes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request
    ) {
        Long userId = getAuthenticatedUserId();
        Page<NoteDto> notes = noteService.findByUser_Id(userId, page, size);
        PageResponse<NoteDto> responseBody = new PageResponse<>(
                notes.getContent(),
                notes.getNumber(),
                notes.getSize(),
                notes.getTotalElements(),
                notes.getTotalPages()
        );
        ApiResponse<PageResponse<NoteDto>> response = ApiResponse.success(responseBody, request.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<NoteDto>> createNote(
            @RequestBody CreateNoteDto createNoteDto,
            HttpServletRequest request
    ) {
        Long userId = getAuthenticatedUserId();
        NoteDto created = noteService.createNote(userId, createNoteDto);
        ApiResponse<NoteDto> response = ApiResponse.success(created, request.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteNote(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        Long userId = getAuthenticatedUserId();
        noteService.deleteNote(userId, id);
        return ResponseEntity.noContent().build();
    }
}
