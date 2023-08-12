package com.cstudy.moduleapi.application.reviewNote;

import com.cstudy.modulecommon.util.LoginUserDto;

public interface ReviewService {
    void createUserWhenSignupSaveMongodb(String userName);
    void solveQuestionWithValid(long questionId, int choiceNumber, boolean isAnswer, LoginUserDto loginUserDto);
}
