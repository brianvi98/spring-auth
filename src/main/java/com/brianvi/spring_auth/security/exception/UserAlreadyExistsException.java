package com.brianvi.spring_auth.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.naming.AuthenticationException;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExistsException extends AuthenticationException {
    public UserAlreadyExistsException(String msg) {
        super(msg);
    }
}
