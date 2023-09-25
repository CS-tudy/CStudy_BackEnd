package com.cstudy.moduleapi.controller.comment;

import com.cstudy.moduleapi.ApiResponse;
import com.cstudy.moduleapi.config.ControllerTest;
import com.cstudy.moduleapi.dto.comment.NoticeCommentRequestDto;
import com.cstudy.moduleapi.dto.comment.NoticeCommentResponse;
import com.cstudy.moduleapi.exception.ErrorResponse;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

class NoticeCommentControllerTest extends ControllerTest {

    private static final String URL = "/api/comment";

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
        public void 댓글_생성하기_유효성_실패_noticeId_음수_400() throws Exception {
            //given

            NoticeCommentRequestDto request = NoticeCommentRequestDto.builder()
                    .noticeId(-1L)
                    .content("댓글")
                    .build();
            //when
            ApiResponse<ErrorResponse> response = commentMockApiCaller.sendPostRequest_WithAuthorization_ParseErrorResponse(URL, request);
            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(400),
                    () -> assertThat(response.getBody().getCode()).isEqualTo("400"),
                    () -> assertThat(response.getBody().getMessage()).isEqualTo("잘못된 요청입니다."),
                    () -> assertThat(response.getBody().getValidation().get("noticeId")).isEqualTo("noticeId는 양수여야 합니다.")
            );
        }

        @Test
        public void 댓글_생성하기_유효성_실패_내용_400() throws Exception {
            //given

            NoticeCommentRequestDto request = NoticeCommentRequestDto.builder()
                    .noticeId(1L)
                    .build();
            //when
            ApiResponse<ErrorResponse> response = commentMockApiCaller.sendPostRequest_WithAuthorization_ParseErrorResponse(URL, request);
            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(400),
                    () -> assertThat(response.getBody().getCode()).isEqualTo("400"),
                    () -> assertThat(response.getBody().getMessage()).isEqualTo("잘못된 요청입니다."),
                    () -> assertThat(response.getBody().getValidation().get("content")).isEqualTo("content는 필수 항목입니다.")
            );
        }

        @Test
        public void 댓글_생성하기_유효성_실패_parentId_음수_400() throws Exception {
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
                    () -> assertThat(response.getStatus()).isEqualTo(400),
                    () -> assertThat(response.getBody().getCode()).isEqualTo("400"),
                    () -> assertThat(response.getBody().getMessage()).isEqualTo("잘못된 요청입니다."),
                    () -> assertThat(response.getBody().getValidation().get("parentCommentId")).isEqualTo("parentCommentId는 양수여야 합니다.")
            );
        }
    }

    @DisplayName("/api/comment/{noticeId} 계층형 댓글 조회")
    @Nested
    class Comments_Read {

        @BeforeEach
        void setUp() {
            // 댓글 1 - 부모 1
            NoticeCommentResponse parent1 = NoticeCommentResponse.builder()
                    .id(1L)
                    .author("김종국")
                    .content("1번 댓글")
                    .memberId(1L)
                    .build();

            // 댓글 2 - 부모 2
            NoticeCommentResponse parent2 = NoticeCommentResponse.builder()
                    .id(2L)
                    .author("유재석")
                    .content("2번 댓글")
                    .memberId(2L)
                    .build();

            // 댓글 3 - 부모 1의 대댓글
            NoticeCommentResponse child1 = NoticeCommentResponse.builder()
                    .id(3L)
                    .author("하하")
                    .content("3번 댓글")
                    .parentCommentId(1L)
                    .memberId(3L)
                    .build();

            // 댓글 4 - 부모 1의 대댓글의 대댓글
            NoticeCommentResponse child2 = NoticeCommentResponse.builder()
                    .id(4L)
                    .author("송지효")
                    .content("4번 댓글")
                    .parentCommentId(3L)
                    .memberId(4L)
                    .build();

            List<NoticeCommentResponse> responses = List.of(parent1, parent2);

            List<NoticeCommentResponse> responses2 = List.of(parent1, parent2, child1,child2);

            given(commentService.getCommentsForNotice(eq(1L))).willReturn(responses);
            given(commentService.getCommentsForNotice(eq(2L))).willReturn(responses2);
        }


        @Test
        public void 댓글_조회_부모_댓글만_조회하기_200() throws Exception {
            //given
            Long id = 1L;
            //when
            ApiResponse<List<NoticeCommentResponse>> response = commentMockApiCaller.getCommentsForNotice(id);
            //Then
            List<NoticeCommentResponse> commentResponses = response.getBody();

            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(200),
                    () -> assertThat(commentResponses).isNotEmpty(),
                    // 첫 번째 댓글은 부모 댓글이므로 자식 댓글이 없어야 함
                    () -> assertThat(commentResponses.get(0).getChildComments()).isEmpty(),
                    // 두 번째 댓글은 부모 댓글이 없어야 함
                    () -> assertThat(commentResponses.get(1).getParentCommentId()).isNull()
            );
        }

        @Test
        public void 댓글_조회_부모_댓글에_자식을_추가하기_조회하기_200() throws Exception {
            //given
            Long id = 2L;

            //when
            ApiResponse<List<NoticeCommentResponse>> response = commentMockApiCaller.getCommentsForNotice(id);

            //Then
            List<NoticeCommentResponse> commentList = response.getBody();

            assertThat(commentList.get(0).getMemberId()).isEqualTo(1L);
            assertThat(commentList.get(0).getAuthor()).isEqualTo("김종국");
            assertThat(commentList.get(0).getContent()).isEqualTo("1번 댓글");


            assertThat(commentList.get(1).getMemberId()).isEqualTo(2L);
            assertThat(commentList.get(1).getAuthor()).isEqualTo("유재석");
            assertThat(commentList.get(1).getParentCommentId()).isNull();
            assertThat(commentList.get(1).getContent()).isEqualTo("2번 댓글");

            assertThat(commentList.get(2).getMemberId()).isEqualTo(3L);
            assertThat(commentList.get(2).getAuthor()).isEqualTo("하하");
            assertThat(commentList.get(2).getParentCommentId()).isEqualTo(1L);
            assertThat(commentList.get(2).getContent()).isEqualTo("3번 댓글");

            assertThat(commentList.get(3).getMemberId()).isEqualTo(4L);
            assertThat(commentList.get(3).getAuthor()).isEqualTo("송지효");
            assertThat(commentList.get(3).getParentCommentId()).isEqualTo(3L);
            assertThat(commentList.get(3).getContent()).isEqualTo("4번 댓글");
        }

    }
}