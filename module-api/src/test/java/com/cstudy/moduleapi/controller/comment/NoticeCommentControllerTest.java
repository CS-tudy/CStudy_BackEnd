package com.cstudy.moduleapi.controller.comment;

import com.cstudy.moduleapi.ApiResponse;
import com.cstudy.moduleapi.config.ControllerTest;
import com.cstudy.moduleapi.dto.comment.NoticeCommentRequestDto;
import com.cstudy.moduleapi.exception.ErrorResponse;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class NoticeCommentControllerTest extends ControllerTest {

    private static final String URL ="/api/comment";

    @BeforeEach
    void setUp() throws Exception {
        super.setup();
    }

    @AfterEach
    void cleanUp() {
        super.cleanup();
    }

    @DisplayName("/api/comment")
    @Nested
    class Comment_create {

        @Test
        public void 댓글_생성하기_성공_201() throws Exception {
            //given
            NoticeCommentRequestDto noticeCommentRequestDto = NoticeCommentRequestDto.builder()
                    .noticeId(1L)
                    .content("댓글")
                    .build();
            //when
            ApiResponse<String> response = commentMockApiCaller.postNewNoticeComment(noticeCommentRequestDto);
            //Then
            assertThat(response.getStatus()).isEqualTo(201);
        }

        @Test
        public void 댓글_생성하기_유효성_실패_noticeId_음수_400() throws Exception{
            //given

            NoticeCommentRequestDto request = NoticeCommentRequestDto.builder()
                    .noticeId(-1L)
                    .content("댓글")
                    .build();
            //when
            ApiResponse<ErrorResponse> response = commentMockApiCaller.sendPostRequest_WithAuthorization_ParseErrorResponse(URL, request);
            //Then
            assertAll(
                    ()->assertThat(response.getStatus()).isEqualTo(400),
                    ()->assertThat(response.getBody().getCode()).isEqualTo("400"),
                    ()->assertThat(response.getBody().getMessage()).isEqualTo("잘못된 요청입니다."),
                    ()->assertThat(response.getBody().getValidation().get("noticeId")).isEqualTo("noticeId는 양수여야 합니다.")
            );
        }

        @Test
        public void 댓글_생성하기_유효성_실패_내용_400() throws Exception{
            //given

            NoticeCommentRequestDto request = NoticeCommentRequestDto.builder()
                    .noticeId(1L)
                    .build();
            //when
            ApiResponse<ErrorResponse> response = commentMockApiCaller.sendPostRequest_WithAuthorization_ParseErrorResponse(URL, request);
            //Then
            assertAll(
                    ()->assertThat(response.getStatus()).isEqualTo(400),
                    ()->assertThat(response.getBody().getCode()).isEqualTo("400"),
                    ()->assertThat(response.getBody().getMessage()).isEqualTo("잘못된 요청입니다."),
                    ()->assertThat(response.getBody().getValidation().get("content")).isEqualTo("content는 필수 항목입니다.")
            );
        }

        @Test
        public void 댓글_생성하기_유효성_실패_parentId_음수_400() throws Exception{
            //given

            NoticeCommentRequestDto request = NoticeCommentRequestDto.builder()
                    .noticeId(1L)
                    .parentCommentId(-1L)
                    .content("내용")
                    .build();
            //when
            ApiResponse<ErrorResponse> response = commentMockApiCaller.sendPostRequest_WithAuthorization_ParseErrorResponse(URL, request);
            //Then
            assertAll(
                    ()->assertThat(response.getStatus()).isEqualTo(400),
                    ()->assertThat(response.getBody().getCode()).isEqualTo("400"),
                    ()->assertThat(response.getBody().getMessage()).isEqualTo("잘못된 요청입니다."),
                    ()->assertThat(response.getBody().getValidation().get("parentCommentId")).isEqualTo("parentCommentId는 양수여야 합니다.")
            );
        }
    }
}