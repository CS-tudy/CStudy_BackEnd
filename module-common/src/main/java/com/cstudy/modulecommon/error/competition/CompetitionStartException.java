package com.cstudy.modulecommon.error.competition;


import com.cstudy.modulecommon.error.abstracts.CompetitionAbstractException;
import com.cstudy.modulecommon.error.enums.ErrorCode;

public class CompetitionStartException extends CompetitionAbstractException {


    public CompetitionStartException() {
        super("Competition Start Time Exception");
    }

    public CompetitionStartException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getStatusCode() {
        return ErrorCode.CompetitionStartException.getErrorCode();
    }
}
