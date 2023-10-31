package com.cstudy.moduleapi.controller.reviewNote;

import com.cstudy.moduleapi.ApiResponse;
import com.cstudy.moduleapi.config.ControllerTest;
import com.cstudy.moduleapi.dto.review.ReviewNoteResponseDto;
import com.cstudy.moduleapi.dto.review.ReviewUserResponseDto;
import com.cstudy.moduleapi.exception.ErrorResponse;
import com.cstudy.modulecommon.util.LoginUserDto;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class ReviewNoteControllerTest extends ControllerTest {


    @DisplayName("/api/review 오답노트 조회")
    @Nested
    class 오답노트 {

        private LocalDateTime time;

        @BeforeEach
        void setUp() {
            List<ReviewNoteResponseDto> reviewNotes = new ArrayList<>();
            time = LocalDateTime.of(2024, 5, 19, 20, 0);

            ReviewNoteResponseDto dto1 = ReviewNoteResponseDto.builder()
                    .id("1")
                    .createdDate(time)
                    .questionId(1L)
                    .successChoiceNumber(1)
                    .failChoiceNumber(0)
                    .isAnswer(true)
                    .build();

            ReviewNoteResponseDto dto2 = ReviewNoteResponseDto.builder()
                    .id("2")
                    .createdDate(time.plusHours(1))
                    .questionId(2L)
                    .successChoiceNumber(2)
                    .failChoiceNumber(0)
                    .isAnswer(true)
                    .build();

            ReviewNoteResponseDto dto3 = ReviewNoteResponseDto.builder()
                    .id("3")
                    .createdDate(time.plusHours(3))
                    .questionId(3L)
                    .successChoiceNumber(3)
                    .failChoiceNumber(0)
                    .isAnswer(true)
                    .build();

            ReviewNoteResponseDto dto4 = ReviewNoteResponseDto.builder()
                    .id("4")
                    .createdDate(time.plusHours(4))
                    .questionId(4L)
                    .successChoiceNumber(3)
                    .failChoiceNumber(2)
                    .isAnswer(false)
                    .build();

            ReviewNoteResponseDto dto5 = ReviewNoteResponseDto.builder()
                    .id("5")
                    .createdDate(time.plusHours(5))
                    .questionId(5L)
                    .successChoiceNumber(1)
                    .failChoiceNumber(2)
                    .isAnswer(false)
                    .build();

            reviewNotes.add(dto1);
            reviewNotes.add(dto2);
            reviewNotes.add(dto3);
            reviewNotes.add(dto4);
            reviewNotes.add(dto5);

            ReviewUserResponseDto responseDto = ReviewUserResponseDto.builder()
                    .userName("김무건")
                    .successQuestion(List.of("1", "2", "3"))
                    .failQuestion(List.of("4", "5"))
                    .reviewNotes(reviewNotes)
                    .build();
            given(reviewService.findMongoAboutReviewNote(any(LoginUserDto.class)))
                    .willReturn(responseDto);
        }

        @Test
        public void 오답노트_조회하기_성공_200() throws Exception {
            //given
            //when
            ApiResponse<ReviewUserResponseDto> response = reviewMockApiCaller.findMongoAboutReviewNote();
            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(200),

                    () -> assertThat(response.getBody().getUserName()).isEqualTo("김무건"),

                    () -> assertThat(response.getBody().getSuccessQuestion().get(0)).isEqualTo("1"),
                    () -> assertThat(response.getBody().getSuccessQuestion().get(1)).isEqualTo("2"),
                    () -> assertThat(response.getBody().getSuccessQuestion().get(2)).isEqualTo("3"),

                    () -> assertThat(response.getBody().getFailQuestion().get(0)).isEqualTo("4"),
                    () -> assertThat(response.getBody().getFailQuestion().get(1)).isEqualTo("5"),

                    () -> assertThat(response.getBody().getReviewNotes().get(0).getId()).isEqualTo("1"),
                    () -> assertThat(response.getBody().getReviewNotes().get(0).getCreatedDate()).isEqualTo(time),
                    () -> assertThat(response.getBody().getReviewNotes().get(0).getSuccessChoiceNumber()).isEqualTo(1),
                    () -> assertThat(response.getBody().getReviewNotes().get(0).getFailChoiceNumber()).isEqualTo(0),
                    () -> assertThat(response.getBody().getReviewNotes().get(0).getQuestionId()).isEqualTo(1L),
                    () -> assertThat(response.getBody().getReviewNotes().get(0).isAnswer()).isEqualTo(true),

                    () -> assertThat(response.getBody().getReviewNotes().get(1).getId()).isEqualTo("2"),
                    () -> assertThat(response.getBody().getReviewNotes().get(1).getCreatedDate()).isEqualTo(time.plusHours(1)),
                    () -> assertThat(response.getBody().getReviewNotes().get(1).getSuccessChoiceNumber()).isEqualTo(2),
                    () -> assertThat(response.getBody().getReviewNotes().get(1).getFailChoiceNumber()).isEqualTo(0),
                    () -> assertThat(response.getBody().getReviewNotes().get(1).getQuestionId()).isEqualTo(2L),
                    () -> assertThat(response.getBody().getReviewNotes().get(1).isAnswer()).isEqualTo(true),

                    () -> assertThat(response.getBody().getReviewNotes().get(2).getId()).isEqualTo("3"),
                    () -> assertThat(response.getBody().getReviewNotes().get(2).getCreatedDate()).isEqualTo(time.plusHours(3)),
                    () -> assertThat(response.getBody().getReviewNotes().get(2).getSuccessChoiceNumber()).isEqualTo(3),
                    () -> assertThat(response.getBody().getReviewNotes().get(2).getFailChoiceNumber()).isEqualTo(0),
                    () -> assertThat(response.getBody().getReviewNotes().get(2).getQuestionId()).isEqualTo(3L),
                    () -> assertThat(response.getBody().getReviewNotes().get(2).isAnswer()).isEqualTo(true),

                    () -> assertThat(response.getBody().getReviewNotes().get(3).getId()).isEqualTo("4"),
                    () -> assertThat(response.getBody().getReviewNotes().get(3).getCreatedDate()).isEqualTo(time.plusHours(4)),
                    () -> assertThat(response.getBody().getReviewNotes().get(3).getSuccessChoiceNumber()).isEqualTo(3),
                    () -> assertThat(response.getBody().getReviewNotes().get(3).getFailChoiceNumber()).isEqualTo(2),
                    () -> assertThat(response.getBody().getReviewNotes().get(3).getQuestionId()).isEqualTo(4L),
                    () -> assertThat(response.getBody().getReviewNotes().get(3).isAnswer()).isEqualTo(false),

                    () -> assertThat(response.getBody().getReviewNotes().get(4).getId()).isEqualTo("5"),
                    () -> assertThat(response.getBody().getReviewNotes().get(4).getCreatedDate()).isEqualTo(time.plusHours(5)),
                    () -> assertThat(response.getBody().getReviewNotes().get(4).getSuccessChoiceNumber()).isEqualTo(1),
                    () -> assertThat(response.getBody().getReviewNotes().get(4).getFailChoiceNumber()).isEqualTo(2),
                    () -> assertThat(response.getBody().getReviewNotes().get(4).getQuestionId()).isEqualTo(5L),
                    () -> assertThat(response.getBody().getReviewNotes().get(4).isAnswer()).isEqualTo(false)
            );

        }
    }

    @Test
    public void 오답노트_조회_토큰_없음_401() throws Exception{
        //given
        String url = "/api/review";
        //when
        ApiResponse<ErrorResponse> response = reviewMockApiCaller.sendGetRequest_WithNoAuthorization_ExpectErrorResponse(url, null);
        //Then
        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getBody().getCode()).isEqualTo("401");
        assertThat(response.getBody().getMessage()).isEqualTo("Headers에 토큰 형식의 값 찾을 수 없음");
    }


}