package com.cstudy.modulecommon.error.abstracts;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class PathvariableAbstractException extends RuntimeException {
    public final Map<String, String> validation = new HashMap<>();

    public PathvariableAbstractException(String message) {
        super(message);
    }

    public PathvariableAbstractException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }
}
