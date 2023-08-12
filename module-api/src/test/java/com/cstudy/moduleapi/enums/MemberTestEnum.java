package com.cstudy.moduleapi.enums;

public enum  MemberTestEnum {
    VALID_EMAIL("test1234@test.com"),
    VALID_PASSWORD("test1234!"),
    VALID_NAME("김무건"),
    INVALID_EMAIL("error@error.com"),
    INVALID_PASSWORD("error1234!"),
    INVALID_NAME("실패");

    private final String message;

    MemberTestEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
