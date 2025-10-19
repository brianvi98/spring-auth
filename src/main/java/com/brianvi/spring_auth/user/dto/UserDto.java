package com.brianvi.spring_auth.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserDto {
    private final Long id;
    private final String username;
    private final String email;
}
