package com.brianvi.spring_auth.note.repository;

import com.brianvi.spring_auth.note.model.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    Page<Note> findByUser_Id(Long id, Pageable pageable);
    Page<Note> findByUser_IdAndContentContainingIgnoreCase(Long id, String content, Pageable pageable);
}
