package com.brianvi.spring_auth.user.controller;

import com.brianvi.spring_auth.response.ApiResponse;
import com.brianvi.spring_auth.user.dto.UserDto;
import com.brianvi.spring_auth.user.model.User;
import com.brianvi.spring_auth.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<ApiResponse<UserDto>> authenticatedUser(HttpServletRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDto userDto = new UserDto(user.getId(), user.getUsername(), user.getEmail());
        return ResponseEntity.ok(ApiResponse.success(userDto, request.getRequestURI()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<String>>> allUsers(HttpServletRequest request) {
        List<String> usernames = userService.allUsers().stream()
                .map(User::getUsername).toList();

        ApiResponse<List<String>> response = ApiResponse.success(usernames, request.getRequestURI());
        return ResponseEntity.ok(response);
    }
}
