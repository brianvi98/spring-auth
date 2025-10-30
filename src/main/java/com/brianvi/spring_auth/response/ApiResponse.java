package com.brianvi.spring_auth.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse<T> {
    private int status;
    private T data;
    private String error;
    private Long timestamp;
    private String path;

    public static <T> ApiResponse<T> success(T data, String path) {
        return new ApiResponse<>(200, data, null, System.currentTimeMillis(), path);
    }

    public static <T> ApiResponse<T> error(String errorMessage, int status, String path) {
        return new ApiResponse<>(status, null, errorMessage, System.currentTimeMillis(), path);
    }
}
