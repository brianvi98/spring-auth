package com.brianvi.spring_auth.note.repository;

import com.brianvi.spring_auth.note.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUser_Id(Long id);
    List<Note> findByUser_IdOrderByCreatedAtDesc(Long id);
    List<Note> findByUser_IdAndContentContainingIgnoreCase(Long id, String content);
}
