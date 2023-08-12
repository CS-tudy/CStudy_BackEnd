package com.cstudy.moduleapi.dto.member;

import lombok.Getter;

@Getter
public enum DuplicateResult {
    TRUE("true"),
    FALSE("false");

    private final String divisionResult;

    DuplicateResult(String divisionResult) {
        this.divisionResult = divisionResult;
    }
}
