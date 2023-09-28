package com.cstudy.moduleapi.controller.notice;

import com.cstudy.moduleapi.ApiResponse;
import com.cstudy.moduleapi.config.ControllerTest;
import com.cstudy.moduleapi.dto.notice.NoticeSaveRequestDto;
import com.cstudy.moduleapi.exception.ErrorResponse;
import com.cstudy.modulecommon.dto.NoticeResponseDto;
import com.cstudy.modulecommon.dto.NoticeSearchRequestDto;
import com.cstudy.modulecommon.dto.NoticeUpdateRequestDto;
import com.cstudy.modulecommon.error.notice.NotFoundNoticeId;
import org.junit.jupiter.api.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.cstudy.modulecommon.dto.NoticeResponseDto.createNoticeResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

class NoticeControllerTest extends ControllerTest {

    @BeforeEach
    void setUp() throws Exception {
        super.setup();
    }

    @AfterEach
    void cleanUp() {
        super.cleanup();
    }

    @DisplayName("/api/notice 공지사항 조회 페이징")
    @Nested
    class paging_findNotice {

        private final LocalDateTime localDateTime = LocalDateTime.of(2023, 9, 28, 14, 30);


        @BeforeEach
        void setUp() {
            NoticeResponseDto noticeResponse1 = createNoticeResponse(1L, "제목1", "내용1", localDateTime);
            NoticeResponseDto noticeResponse2 = createNoticeResponse(2L, "제목2", "내용2", localDateTime.plusHours(1));

            List<NoticeResponseDto> list = new ArrayList<>(Arrays.asList(noticeResponse1, noticeResponse2));
            List<NoticeResponseDto> list2 = new ArrayList<>(List.of(noticeResponse2));

            Page<NoticeResponseDto> pagedResponse = new PageImpl<>(list);
            Page<NoticeResponseDto> pagedResponse2 = new PageImpl<>(list2);
            Page<NoticeResponseDto> pagedResponse3 = new PageImpl<>(list2);
            Page<NoticeResponseDto> pagedResponse4 = new PageImpl<>(list);

            NoticeSearchRequestDto request = NoticeSearchRequestDto.builder().build();
            NoticeSearchRequestDto search = NoticeSearchRequestDto.builder().title("제목2").build();
            NoticeSearchRequestDto search_title = NoticeSearchRequestDto.builder().content("내용2").build();
            NoticeSearchRequestDto search_date = NoticeSearchRequestDto.builder().createdDate(localDateTime).build();

            given(noticeService.findNoticePage(anyInt(), anyInt(), eq(request))).willReturn(pagedResponse);
            given(noticeService.findNoticePage(anyInt(), anyInt(), eq(search))).willReturn(pagedResponse2);
            given(noticeService.findNoticePage(anyInt(), anyInt(), eq(search_title))).willReturn(pagedResponse3);
            given(noticeService.findNoticePage(anyInt(), anyInt(), eq(search_date))).willReturn(pagedResponse4);
        }

        @Test
        public void 공지사항_조회_페이징_기본_PageRequest_Default_200() throws Exception {
            //given
            NoticeSearchRequestDto request = NoticeSearchRequestDto.builder().build();
            String url = "/api/notice";
            //when
            ApiResponse<Page<NoticeResponseDto>> response = noticeMockApiCaller.findNoticeWithPage(url, request);

            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(200),

                    () -> assertThat(response.getBody().getTotalPages()).isEqualTo(1),
                    () -> assertThat(response.getBody().getNumber()).isEqualTo(0),
                    () -> assertThat(response.getBody().getSize()).isEqualTo(2),


                    () -> assertThat(response.getBody().getContent().get(0).getId()).isEqualTo(1L),
                    () -> assertThat(response.getBody().getContent().get(0).getTitle()).isEqualTo("제목1"),
                    () -> assertThat(response.getBody().getContent().get(0).getContent()).isEqualTo("내용1"),
                    () -> assertThat(response.getBody().getContent().get(0).getCreatedDate()).isEqualTo(localDateTime),

                    () -> assertThat(response.getBody().getContent().get(1).getId()).isEqualTo(2L),
                    () -> assertThat(response.getBody().getContent().get(1).getTitle()).isEqualTo("제목2"),
                    () -> assertThat(response.getBody().getContent().get(1).getContent()).isEqualTo("내용2"),
                    () -> assertThat(response.getBody().getContent().get(1).getCreatedDate()).isEqualTo(localDateTime.plusHours(1))
            );
        }

        @Test
        public void 공지사항_조회_제목을_기반으로_페이징_성공_200() throws Exception {
            //given
            NoticeSearchRequestDto search = NoticeSearchRequestDto.builder().title("제목2").build();
            String url = "/api/notice?title=제목2";
            //when
            ApiResponse<Page<NoticeResponseDto>> response = noticeMockApiCaller.findNoticeWithPage(url, search);

            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(200),

                    () -> assertThat(response.getBody().getSize()).isEqualTo(1),
                    () -> assertThat(response.getBody().getTotalPages()).isEqualTo(1),

                    () -> assertThat(response.getBody().getContent().get(0).getId()).isEqualTo(2L),
                    () -> assertThat(response.getBody().getContent().get(0).getTitle()).isEqualTo("제목2"),
                    () -> assertThat(response.getBody().getContent().get(0).getContent()).isEqualTo("내용2"),
                    () -> assertThat(response.getBody().getContent().get(0).getCreatedDate()).isEqualTo(localDateTime.plusHours(1))
            );
        }


        @Test
        public void 공지사항_조회_내용을_기반으로_페이징_성공_200() throws Exception {
            //given
            NoticeSearchRequestDto search = NoticeSearchRequestDto.builder()
                    .content("내용2")
                    .build();
            String url = "/api/notice?content=내용2";
            //when
            ApiResponse<Page<NoticeResponseDto>> response = noticeMockApiCaller.findNoticeWithPage(url, search);

            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(200),

                    () -> assertThat(response.getBody().getSize()).isEqualTo(1),
                    () -> assertThat(response.getBody().getTotalPages()).isEqualTo(1),

                    () -> assertThat(response.getBody().getContent().get(0).getId()).isEqualTo(2L),
                    () -> assertThat(response.getBody().getContent().get(0).getTitle()).isEqualTo("제목2"),
                    () -> assertThat(response.getBody().getContent().get(0).getContent()).isEqualTo("내용2"),
                    () -> assertThat(response.getBody().getContent().get(0).getCreatedDate()).isEqualTo(localDateTime.plusHours(1))
            );
        }

    }

    @DisplayName("/api/notice/{noticeId} 세부 공지사항 조회")
    @Nested
    class findByNoticeId {
        private final LocalDateTime localDateTime = LocalDateTime.of(2023, 9, 28, 14, 30);

        @BeforeEach
        void setUp() {
            given(noticeService.findNoticeWithId(eq(1L))).willReturn(NoticeResponseDto.builder()
                    .id(1L)
                    .title("제목")
                    .content("내용")
                    .createdDate(localDateTime)
                    .build());

            given(noticeService.findNoticeWithId(eq(99L)))
                    .willThrow(NotFoundNoticeId.class);
        }

        @Test
        public void 세부_공지사항_조회_성공_200() throws Exception {
            //given
            String url = "/api/notice/{noticeId}";
            Long noticeId = 1L;
            //when
            ApiResponse<NoticeResponseDto> response = noticeMockApiCaller.findByNoticeId(url, noticeId);
            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(200),

                    () -> assertThat(response.getBody().getId()).isEqualTo(1L),
                    () -> assertThat(response.getBody().getTitle()).isEqualTo("제목"),
                    () -> assertThat(response.getBody().getContent()).isEqualTo("내용"),
                    () -> assertThat(response.getBody().getCreatedDate()).isEqualTo(localDateTime)
            );
        }

        @Test
        public void 세부_공지사항_조회_실패_noticeId_음수_400() throws Exception {
            //given
            String url = "/api/notice/-1";
            //when
            ApiResponse<ErrorResponse> response = noticeMockApiCaller.sendGetRequest_ExpectErrorResponse(url, null);
            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(400),
                    () -> assertThat(response.getBody().getCode()).isEqualTo("8500"),
                    () -> assertThat(response.getBody().getMessage()).isEqualTo("pathvariable은 양수로 처리를 해야됩니다.-1")
            );
        }


        @Test
        public void 세부_공지사항_조회_실패_noticeId를_찾을_수_없습니다_400() throws Exception {
            //given
            String url = "/api/notice/99";

            //Then
            ApiResponse<ErrorResponse> response = noticeMockApiCaller.sendGetRequest_ExpectErrorResponse(url, null);

            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(400),
                    () -> assertThat(response.getBody().getCode()).isEqualTo("7000")
            );
        }
    }

    @DisplayName("POST - api/notice/")
    @Nested
    class 공지사항_생성 {
        @BeforeEach
        void setUp() {

        }

        @Test
        public void 공지사항_생성_성공_200() throws Exception {
            //given
            NoticeSaveRequestDto requestDto = NoticeSaveRequestDto.builder()
                    .title("제목")
                    .content("내용")
                    .build();
            //when
            ApiResponse<String> response = noticeMockApiCaller.saveNotice(requestDto);
            //Then
            assertThat(response.getStatus()).isEqualTo(201);
        }

        @Test
        public void 공지사항_생성_실패_토큰_아님_403() throws Exception {
            //given
            String url = "/api/notice";
            NoticeSaveRequestDto requestDto = NoticeSaveRequestDto.builder()
                    .title("제목")
                    .content("내용")
                    .build();
            //when
            ApiResponse<ErrorResponse> response = noticeMockApiCaller.sendPostRequest_WithNoAuthorization_ParseErrorResponse(url, requestDto);
            //Then
            assertThat(response.getStatus()).isEqualTo(401);
            assertThat(response.getBody().getCode()).isEqualTo("401");
            assertThat(response.getBody().getMessage()).isEqualTo("Headers에 토큰 형식의 값 찾을 수 없음");
        }

        @Test
        public void 공지사항_생성_실패_유저토큰_403() throws Exception {
            //given
            String url = "/api/notice";
            NoticeSaveRequestDto requestDto = NoticeSaveRequestDto.builder()
                    .title("제목")
                    .content("내용")
                    .build();
            //when
            ApiResponse<ErrorResponse> response = noticeMockApiCaller.sendPostRequest_WithAuthorization_USER_ParseErrorResponse(url, requestDto);
            //Then
            assertThat(response.getStatus()).isEqualTo(403);
            assertThat(response.getBody().getCode()).isEqualTo("403");
            assertThat(response.getBody().getMessage()).isEqualTo("Access Denied: Access is denied");
        }

        @Test
        public void 공지사항_유효성_실패_제목_400() throws Exception {
            //given
            String url = "/api/notice";
            NoticeSaveRequestDto requestDto = NoticeSaveRequestDto.builder()
                    .content("내용")
                    .build();
            //when
            ApiResponse<ErrorResponse> response = noticeMockApiCaller.sendPostRequest_WithAuthorization_ParseErrorResponse(url, requestDto);
            //Then
            assertThat(response.getStatus()).isEqualTo(400);
            assertThat(response.getBody().getCode()).isEqualTo("400");
            assertThat(response.getBody().getMessage()).isEqualTo("잘못된 요청입니다.");
            assertThat(response.getBody().getValidation().get("title")).isEqualTo("공지사항 제목을 입력하세요.");
        }

        @Test
        public void 공지사항_유효성_실패_내용_400() throws Exception {
            //given
            String url = "/api/notice";
            NoticeSaveRequestDto requestDto = NoticeSaveRequestDto.builder()
                    .title("제목")
                    .build();
            //when
            ApiResponse<ErrorResponse> response = noticeMockApiCaller.sendPostRequest_WithAuthorization_ParseErrorResponse(url, requestDto);
            //Then
            assertThat(response.getStatus()).isEqualTo(400);
            assertThat(response.getBody().getCode()).isEqualTo("400");
            assertThat(response.getBody().getMessage()).isEqualTo("잘못된 요청입니다.");
            assertThat(response.getBody().getValidation().get("content")).isEqualTo("공지사항 내용을 입력하세요.");
        }
    }

    @DisplayName("patch /api/notice/{noticeId}")
    @Nested
    class 공지사항_업데이트 {

        @BeforeEach
        void setUp() {

        }

        @Test
        public void 공지사항_업데이트_성공_204() throws Exception {
            //given
            NoticeUpdateRequestDto requestDto = NoticeUpdateRequestDto.builder()
                    .title("제목")
                    .content("내용")
                    .build();

            Long noticeId = 1L;
            //when
            ApiResponse<String> response = noticeMockApiCaller.updateNotice(requestDto, noticeId);
            //Then
            assertThat(response.getStatus()).isEqualTo(204);
        }

        @Test
        public void 공지사항_업데이트_실패_noticeId_음수_400() throws Exception {
            //given
            String url = "/api/notice/-1";
            NoticeUpdateRequestDto requestDto = NoticeUpdateRequestDto.builder()
                    .title("제목")
                    .content("내용")
                    .build();
            //when
            ApiResponse<ErrorResponse> response = noticeMockApiCaller.sendPatchRequest_ExpectErrorResponse(url, requestDto);
            //Then
            assertThat(response.getStatus()).isEqualTo(400);
            assertThat(response.getBody().getCode()).isEqualTo("8500");
            assertThat(response.getBody().getMessage()).isEqualTo("pathvariable은 양수로 처리를 해야됩니다.-1");
        }

        @Test
        public void 공지사항_업데이트_유효성_실패_제목_400() throws Exception {
            //given
            String url = "/api/notice/1";
            NoticeUpdateRequestDto requestDto = NoticeUpdateRequestDto.builder()
                    .content("내용")
                    .build();
            //when
            ApiResponse<ErrorResponse> response = noticeMockApiCaller.sendPatchRequest_USER_ExpectErrorResponse(url, requestDto);
            //Then
            assertThat(response.getStatus()).isEqualTo(400);
            assertThat(response.getBody().getCode()).isEqualTo("400");
            assertThat(response.getBody().getMessage()).isEqualTo("잘못된 요청입니다.");
            assertThat(response.getBody().getValidation().get("title")).isEqualTo("제목을 입력하세요.");
        }

        @Test
        public void 공지사항_업데이트_유효성_실패_내용_400() throws Exception {
            //given
            String url = "/api/notice/1";
            NoticeUpdateRequestDto requestDto = NoticeUpdateRequestDto.builder()
                    .title("제목")
                    .build();
            //when
            ApiResponse<ErrorResponse> response = noticeMockApiCaller.sendPatchRequest_USER_ExpectErrorResponse(url, requestDto);
            //Then
            assertThat(response.getStatus()).isEqualTo(400);
            assertThat(response.getBody().getCode()).isEqualTo("400");
            assertThat(response.getBody().getMessage()).isEqualTo("잘못된 요청입니다.");
            assertThat(response.getBody().getValidation().get("content")).isEqualTo("내용을 입력하세요.");
        }
    }

    @DisplayName("Delete /api/notice")
    @Nested
    class 공지사항_삭제 {
        @BeforeEach
        void setUp() {

        }

        @Test
        public void 공지사항_삭제_성공_200() throws Exception {
            //given
            Long noticeId = 1L;
            //when
            ApiResponse<String> response = noticeMockApiCaller.deleteNotice(noticeId);
            //Then
            assertThat(response.getStatus()).isEqualTo(204);
        }

        @Test
        public void 공지사항_삭제_실패_유저_권한_403() throws Exception {
            //given
            String url = "/api/notice/{path}";
            Long noticeId = 1L;
            //when
            ApiResponse<ErrorResponse> response = noticeMockApiCaller.sendDeleteRequest_USER_ExpectErrorResponse(url, null, noticeId);
            //Then
            assertThat(response.getStatus()).isEqualTo(403);
            assertThat(response.getBody().getCode()).isEqualTo("403");
            assertThat(response.getBody().getMessage()).isEqualTo("Access Denied: Access is denied");
        }

        @Test
        public void 공지사항_삭제_실패_noticeId_음수_400() throws Exception {
            //given
            String url = "/api/notice/{path}";
            Long path = -1L;
            //when
            ApiResponse<ErrorResponse> response = noticeMockApiCaller.sendDeleteRequest_ExpectErrorResponse(url, null, path);
            //Then
            assertThat(response.getStatus()).isEqualTo(400);
            assertThat(response.getBody().getCode()).isEqualTo("8500");
            assertThat(response.getBody().getMessage()).isEqualTo("pathvariable은 양수로 처리를 해야됩니다.-1");
        }
    }
}