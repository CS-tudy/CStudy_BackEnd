package com.cstudy.modulecommon.error.member;


import com.cstudy.modulecommon.error.abstracts.MemberAbstractException;
import com.cstudy.modulecommon.error.enums.ErrorCode;

public class InvalidMatchPasswordException extends MemberAbstractException {
    public InvalidMatchPasswordException(String message) {
        super(message + "가 일치하지 않습니다.");
    }

    public InvalidMatchPasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getStatusCode() {
        return ErrorCode.InvalidMatchPasswordException.getErrorCode();
    }

}
