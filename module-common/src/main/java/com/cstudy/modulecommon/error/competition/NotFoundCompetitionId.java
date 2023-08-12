package com.cstudy.modulecommon.error.competition;


import com.cstudy.modulecommon.error.abstracts.CompetitionAbstractException;
import com.cstudy.modulecommon.error.enums.ErrorCode;

public class NotFoundCompetitionId extends CompetitionAbstractException {
    public NotFoundCompetitionId(Long competitionId) {
        super("Not Found Competition. CompetitionId: "+competitionId);
    }

    public NotFoundCompetitionId(String message, Throwable cause) {
        super(message, cause);
    }


    @Override
    public int getStatusCode() {
        return ErrorCode.NotFoundCompetitionId.getErrorCode();
    }
}
