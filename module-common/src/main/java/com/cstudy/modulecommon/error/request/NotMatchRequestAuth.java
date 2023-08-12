package com.cstudy.modulecommon.error.request;


import com.cstudy.modulecommon.error.abstracts.RequestAbstractException;
import com.cstudy.modulecommon.error.enums.ErrorCode;

public class NotMatchRequestAuth extends RequestAbstractException {

    public NotMatchRequestAuth(Long message) {
        super("Not Match Auth Request" + message);
    }

    public NotMatchRequestAuth(Long message, Throwable cause) {
        super(String.valueOf(message), cause);
    }

    @Override
    public int getStatusCode() {
        return ErrorCode.NotMatchRequestAuth.getErrorCode();
    }
}
