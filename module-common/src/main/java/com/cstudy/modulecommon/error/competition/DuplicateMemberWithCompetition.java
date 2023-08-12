package com.cstudy.modulecommon.error.competition;


import com.cstudy.modulecommon.error.abstracts.CompetitionAbstractException;
import com.cstudy.modulecommon.error.enums.ErrorCode;

public class DuplicateMemberWithCompetition extends CompetitionAbstractException {

    public DuplicateMemberWithCompetition(Long memberId) {
        super("Duplicate request sent for the same competition." + memberId);
    }

    public DuplicateMemberWithCompetition(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getStatusCode() {
        return ErrorCode.DuplicateMemberWithCompetition.getErrorCode();
    }

}
