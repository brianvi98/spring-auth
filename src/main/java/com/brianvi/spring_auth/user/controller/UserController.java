package com.brianvi.spring_auth.user.controller;

import com.brianvi.spring_auth.user.dto.UserDto;
import com.brianvi.spring_auth.user.model.User;
import com.brianvi.spring_auth.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User user)) {
            return ResponseEntity.status(401).build();
        }

        UserDto userDto = new UserDto(user.getId(), user.getUsername(), user.getEmail());
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/")
    public ResponseEntity<List<String>> allUsers() {
        List<String> usernames = new ArrayList<>();

        for (User u : userService.allUsers()) {
            usernames.add(u.getUsername());
        }
        return ResponseEntity.ok(usernames);
    }
}
