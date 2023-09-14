package com.cstudy.modulecommon.error.pathvariable;


import com.cstudy.modulecommon.error.abstracts.PathvariableAbstractException;
import com.cstudy.modulecommon.error.enums.ErrorCode;

public class PositivePatriarchal extends PathvariableAbstractException {

    public PositivePatriarchal(Long id) {
        super("pathvariable은 양수로 처리를 해야됩니다." + id);
    }

    public PositivePatriarchal(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getStatusCode() {
        return ErrorCode.PositivePatriarchal.getErrorCode();
    }
}
