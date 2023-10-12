package com.cstudy.moduleapi.application.reviewNote;

import com.cstudy.moduleapi.dto.review.ReviewUserResponseDto;
import com.cstudy.modulecommon.util.LoginUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {
    void createUserWhenSignupSaveMongodb(String userName);

    void solveQuestionWithValid(long questionId, int choiceNumber, boolean isAnswer, LoginUserDto loginUserDto, Integer choiceAnswerNumber);

    ReviewUserResponseDto findMongoAboutReviewNote(LoginUserDto loginUserDto);

    Page<ReviewUserResponseDto> findReviewPaging(LoginUserDto loginUserDto, Pageable pageable);
}
