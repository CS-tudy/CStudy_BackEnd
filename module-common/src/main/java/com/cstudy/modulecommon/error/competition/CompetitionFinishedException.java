package com.cstudy.modulecommon.error.competition;


import com.cstudy.modulecommon.error.abstracts.CompetitionAbstractException;
import com.cstudy.modulecommon.error.enums.ErrorCode;

public class CompetitionFinishedException extends CompetitionAbstractException {


    public CompetitionFinishedException() {
        super("Competition Already Finished");
    }

    public CompetitionFinishedException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getStatusCode() {
        return ErrorCode.CompetitionFinishedException.getErrorCode();
    }
}
