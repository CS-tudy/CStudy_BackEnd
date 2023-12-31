package com.cstudy.moduleapi.enums;

public enum  MemberTestEnum {
    VALID_EMAIL("test@test.com"),
    VALID_PASSWORD("test1234!"),
    VALID_NAME("김무건"),
    CUSTOM_EMAIL("CUSTOM1234@CUSTOM.COM"),
    CUSTOM_NAME("CUSTOM"),
    ADMIN_EMAIL("admin@admin.com"),
    ADMIN_PASSWORD("admin1234!"),
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
