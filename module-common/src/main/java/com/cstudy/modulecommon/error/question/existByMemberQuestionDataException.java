package com.cstudy.modulecommon.error.question;


import com.cstudy.modulecommon.error.abstracts.QuestionAbstractException;
import com.cstudy.modulecommon.error.enums.ErrorCode;

public class existByMemberQuestionDataException extends QuestionAbstractException {

    public existByMemberQuestionDataException(Long memberId, Long questionId, int choiceNumber) {
        super(memberId+"회원 아이디를 가진 사람이"+questionId+"문제에"+choiceNumber+"를 선택한 데이터가 존재");
    }

    public existByMemberQuestionDataException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getStatusCode() {
        return ErrorCode.existByMemberQuestionDataException.getErrorCode();
    }

}
