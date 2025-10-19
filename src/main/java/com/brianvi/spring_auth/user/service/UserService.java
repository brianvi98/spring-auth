package com.brianvi.spring_auth.user.service;

import com.brianvi.spring_auth.security.service.EmailService;
import com.brianvi.spring_auth.user.model.User;
import com.brianvi.spring_auth.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
    }

    public List<User> allUsers() {
        return new ArrayList<>(userRepository.findAll());
    }
}
