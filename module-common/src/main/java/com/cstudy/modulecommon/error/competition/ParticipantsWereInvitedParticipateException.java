package com.cstudy.modulecommon.error.competition;


import com.cstudy.modulecommon.error.abstracts.CompetitionAbstractException;
import com.cstudy.modulecommon.error.enums.ErrorCode;

public class ParticipantsWereInvitedParticipateException extends CompetitionAbstractException {

    public ParticipantsWereInvitedParticipateException() {
        super("참가 가능한 인원이 초과");
    }

    public ParticipantsWereInvitedParticipateException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getStatusCode() {
        return ErrorCode.participantsWereInvitedParticipateException.getErrorCode();
    }
}
