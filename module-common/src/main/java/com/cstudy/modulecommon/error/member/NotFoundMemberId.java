package com.cstudy.modulecommon.error.member;


import com.cstudy.modulecommon.error.abstracts.MemberAbstractException;
import com.cstudy.modulecommon.error.enums.ErrorCode;

public class NotFoundMemberId extends MemberAbstractException {
    public NotFoundMemberId(Long message) {
        super("Not Found Member With:" + message);
    }

    public NotFoundMemberId(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getStatusCode() {
        return ErrorCode.NotFoundMemberId.getErrorCode();
    }
}
