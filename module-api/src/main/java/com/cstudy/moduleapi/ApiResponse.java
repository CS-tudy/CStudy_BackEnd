package com.cstudy.moduleapi;

import com.cstudy.moduleapi.exception.ErrorResponse;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {

    public static final ApiResponse<String> SUCCESS = success("");

    private String code;

    private String message;

    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("", "", data);
    }

    public static ApiResponse<Object> error(ErrorResponse errorCode) {
        return new ApiResponse<>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    public static ApiResponse<Object> error(ErrorResponse errorCode, String message) {
        return new ApiResponse<>(errorCode.getCode(), message, null);
    }

}