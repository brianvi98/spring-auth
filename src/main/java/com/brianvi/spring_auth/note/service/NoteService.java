package com.brianvi.spring_auth.note.service;

import com.brianvi.spring_auth.note.dto.CreateNoteDto;
import com.brianvi.spring_auth.note.dto.NoteDto;
import com.brianvi.spring_auth.note.model.Note;
import com.brianvi.spring_auth.note.repository.NoteRepository;
import com.brianvi.spring_auth.user.dto.UserDto;
import com.brianvi.spring_auth.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NoteService {
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public NoteService(NoteRepository noteRepository,  UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    private NoteDto mapToDto(Note note) {

        return new NoteDto(
            note.getId(),
            note.getUser().getId(),
            note.getCreatedAt(),
            note.getUpdatedAt(),
            note.getContent()
        );
    }

    public Page<NoteDto> getNotes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return noteRepository.findAll(pageable)
                .map(this::mapToDto);
    }

    public NoteDto createNote(Long userId, CreateNoteDto createNoteDto) {
        Note note = new Note();
        note.setContent(createNoteDto.getContent());
        note.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found")));

        return mapToDto(noteRepository.save(note));
    }

    public NoteDto deleteNote(Long userId, Long noteId) {
        Note note = noteRepository.findById(noteId)
                        .orElseThrow(() -> new IllegalArgumentException("Note not found"));

        if (!note.getUser().getId().equals(userId)) {
            throw new SecurityException("You do not have permission to delete this note");
        }

        NoteDto noteDto = mapToDto(note);
        noteRepository.delete(note);
        return noteDto;
    }

    public Page<NoteDto> findByUser_Id(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return noteRepository.findByUser_Id(userId, pageable)
                .map(this::mapToDto);
    }

    public Page<NoteDto> findByUser_IdAndContentContainingIgnoreCase(Long id, String content, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return noteRepository.findByUser_IdAndContentContainingIgnoreCase(id, content, pageable)
                .map(this::mapToDto);
    }
}
