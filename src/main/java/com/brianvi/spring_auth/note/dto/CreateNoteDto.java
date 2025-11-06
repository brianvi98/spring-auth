package com.brianvi.spring_auth.note.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateNoteDto {

    private String content;
}
