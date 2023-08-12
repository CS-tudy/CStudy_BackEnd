package com.cstudy.modulecommon.error.member;


import com.cstudy.modulecommon.error.abstracts.MemberAbstractException;
import com.cstudy.modulecommon.error.enums.ErrorCode;

public class NameDuplication extends MemberAbstractException {

    public NameDuplication(String message) {
        super("User name is already existed:" + message);
    }

    public NameDuplication(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getStatusCode() {
        return ErrorCode.EmailDuplication.getErrorCode();
    }
}
