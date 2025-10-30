package com.brianvi.spring_auth.security.controller;

import com.brianvi.spring_auth.response.ApiResponse;
import com.brianvi.spring_auth.security.dto.LoginUserDto;
import com.brianvi.spring_auth.security.dto.RegisterUserDto;
import com.brianvi.spring_auth.security.dto.VerifyUserDto;
import com.brianvi.spring_auth.security.exception.UserAlreadyExistsException;
import com.brianvi.spring_auth.security.response.LoginResponse;
import com.brianvi.spring_auth.security.service.AuthenticationService;
import com.brianvi.spring_auth.security.service.JwtService;
import com.brianvi.spring_auth.user.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<String>> register(
            @RequestBody RegisterUserDto registerUserDto,
            HttpServletRequest request
    ) throws UserAlreadyExistsException
    {
        authenticationService.signUp(registerUserDto);
        ApiResponse<String> response = ApiResponse
                .success("Sign up successful for " + registerUserDto.getEmail(), request.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> authenticate(
            @RequestBody LoginUserDto loginUserDto,
            HttpServletRequest request
    ) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());
        ApiResponse<LoginResponse> response = ApiResponse.success(loginResponse, request.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(
            @RequestBody VerifyUserDto verifyUserDto,
            HttpServletRequest request
    ) {
        authenticationService.verifyUser(verifyUserDto);
        ApiResponse<String> response = ApiResponse.success("Account successfully verified", request.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email, HttpServletRequest request) {
        authenticationService.resendVerificationCode(email);
        ApiResponse<String> response = ApiResponse.success("Verification code sent", request.getRequestURI());
        return ResponseEntity.ok(response);
    }
}
