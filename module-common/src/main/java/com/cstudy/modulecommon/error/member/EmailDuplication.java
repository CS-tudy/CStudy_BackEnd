package com.cstudy.modulecommon.error.member;


import com.cstudy.modulecommon.error.abstracts.MemberAbstractException;
import com.cstudy.modulecommon.error.enums.ErrorCode;

public class EmailDuplication extends MemberAbstractException {

    public EmailDuplication(String message) {
        super("User email is already existed:" + message);
    }

    public EmailDuplication(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getStatusCode() {
        return ErrorCode.EmailDuplication.getErrorCode();
    }
}
