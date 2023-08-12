package com.cstudy.modulecommon.error.member;


import com.cstudy.modulecommon.error.abstracts.MemberAbstractException;
import com.cstudy.modulecommon.error.enums.ErrorCode;

public class NotFoundMemberEmail extends MemberAbstractException {

    public NotFoundMemberEmail(String message) {
        super("User email is not found : " + message);
    }

    public NotFoundMemberEmail(String message, Throwable cause) {
        super(message, cause);
    }


    @Override
    public int getStatusCode() {
        return ErrorCode.NotFoundMemberEmail.getErrorCode();
    }
}
