package com.cstudy.moduleapi.dto.review;

import com.cstudy.moduleapi.dto.question.QuestionResponseDto;
import com.cstudy.modulecommon.domain.question.Question;
import com.cstudy.modulecommon.domain.reviewQuestion.ReviewUser;
import com.cstudy.modulecommon.dto.ChoiceQuestionResponseDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ReviewUserResponseDto {

    private String userName;
    private List<String> successQuestion;
    private List<String> failQuestion;
    private List<ReviewNoteResponseDto> reviewNotes = new ArrayList<>();

    @Builder
    public ReviewUserResponseDto(String userName, List<String> successQuestion, List<String> failQuestion, List<ReviewNoteResponseDto> reviewNotes) {
        this.userName = userName;
        this.successQuestion = successQuestion;
        this.failQuestion = failQuestion;
        this.reviewNotes = reviewNotes;
    }

    public static ReviewUserResponseDto of(ReviewUser reviewUser) {
        return ReviewUserResponseDto.builder()
                .userName(reviewUser.getUserName())
                .successQuestion(reviewUser.getSuccessQuestion())
                .failQuestion(reviewUser.getFailQuestion())
                .reviewNotes(reviewUser.getReviewNotes().stream()
                        .map(ReviewNoteResponseDto::new)
                        .collect(Collectors.toList()))
                .build();
    }
    public static QuestionResponseDto of(Question question) {
        return QuestionResponseDto.builder()
                .title(question.getTitle())
                .description(question.getDescription())
                .categoryTitle(question.getCategory().getCategoryTitle())
                .explain(question.getExplain())
                .choices(question.getChoices().stream()
                        .map(ChoiceQuestionResponseDto::new)
                        .collect(Collectors.toList()))
                .build();
    }
}
