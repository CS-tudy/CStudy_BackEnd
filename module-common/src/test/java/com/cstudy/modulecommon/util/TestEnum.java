package com.cstudy.modulecommon.util;

public enum TestEnum {
    MEMBER_EMAIL("test1234@gmail.com"),
    MEMBER_NAME("김무건"),
    NOTICE_TITLE("공지사항_제목"),
    NOTICE_CONTENT("공지사항_내용"),
    QUESTION_TITLE("문제 제목"),
    QUESTION_DESCRIPTION("문제 설명"),
    QUESTION_EXPLAIN("문제 정답 설명");

    private final String message;

    TestEnum(String message) {
        this.message = message;
    }
////public static Question of(String title, String description,String explain, List<Choice> choice) {
    public String getMessage() {
        return message;
    }
}
