package com.cstudy.modulecommon.error.competition;


import com.cstudy.modulecommon.error.abstracts.CompetitionAbstractException;
import com.cstudy.modulecommon.error.enums.ErrorCode;

public class NotFoundMemberCompetition extends CompetitionAbstractException {

    public NotFoundMemberCompetition() {
        super("Not Found MemberCompetition");
    }

    public NotFoundMemberCompetition(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getStatusCode() {
        return ErrorCode.NotFoundMemberCompetition.getErrorCode();
    }
}
