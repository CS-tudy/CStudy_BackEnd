package com.cstudy.modulecommon.error.abstracts;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class WorkbookAbstractException extends RuntimeException {
    public final Map<String, String> validation = new HashMap<>();

    public WorkbookAbstractException(String message) {
        super(message);
    }

    public WorkbookAbstractException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }
}
