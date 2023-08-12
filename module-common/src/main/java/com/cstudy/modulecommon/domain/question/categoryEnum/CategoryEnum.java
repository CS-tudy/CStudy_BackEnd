package com.cstudy.modulecommon.domain.question.categoryEnum;

import lombok.Getter;

@Getter
public enum CategoryEnum {

    JAVA("자바"),
    NETWORK("네트워크"),
    OS("운영체제"),
    DB("데이터베이스");

    private final String categoryName;


    CategoryEnum(String categoryName) {
        this.categoryName = categoryName;
    }
}
