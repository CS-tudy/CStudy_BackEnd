package com.cstudy.moduleapi.controller.request;

import com.cstudy.moduleapi.ApiResponse;
import com.cstudy.moduleapi.config.ControllerTest;
import com.cstudy.moduleapi.dto.request.CreateRequestRequestDto;
import com.cstudy.moduleapi.dto.request.FlagRequestDto;
import com.cstudy.moduleapi.dto.request.RequestResponseDto;
import com.cstudy.moduleapi.exception.ErrorResponse;
import com.cstudy.modulecommon.dto.UpdateRequestRequestDto;
import org.junit.jupiter.api.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.cstudy.moduleapi.dto.request.RequestResponseDto.createRequestDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

class RequestControllerTest extends ControllerTest {
    @BeforeEach
    void setUp() throws Exception {
        super.setup();
    }

    @AfterEach
    void cleanUp() {
        super.cleanup();
    }

    @DisplayName("/api/request 게시판 요청 게시글 생성하기")
    @Nested
    class 요청_문제_요청하기 {

        @Test
        public void 문제_요청하기_성공_201() throws Exception {
            //given
            CreateRequestRequestDto requestDto = CreateRequestRequestDto.builder()
                    .title("제목")
                    .description("설명")
                    .build();
            //when
            ApiResponse<String> response = requestMockApiCaller.createRequest(requestDto);
            //Then
            assertThat(response.getStatus()).isEqualTo(201);
        }

        @Test
        public void 문제_요청하기_권한이_다름_403() throws Exception {
            //given
            String url = "/api/request";
            CreateRequestRequestDto requestDto = CreateRequestRequestDto.builder()
                    .title("제목")
                    .description("설명")
                    .build();
            //when
            ApiResponse<ErrorResponse> response = requestMockApiCaller.sendPostRequest_WithAuthorization_ParseErrorResponse(url, requestDto);
            //Then
            assertThat(response.getStatus()).isEqualTo(403);
            assertThat(response.getBody().getCode()).isEqualTo("403");
            assertThat(response.getBody().getMessage()).isEqualTo("Access Denied: Access is denied");
            assertThat(response.getBody().getValidation().get("Spring Security")).isEqualTo("권한이 일치하지 않습니다.");
        }

        @Test
        public void 문제_요청하기_생성_토큰_없음_401() throws Exception {
            //given
            String url = "/api/request";
            CreateRequestRequestDto requestDto = CreateRequestRequestDto.builder()
                    .title("제목")
                    .description("설명")
                    .build();
            //when
            ApiResponse<ErrorResponse> response = requestMockApiCaller.sendPostRequest_WithNoAuthorization_ParseErrorResponse(url, requestDto);
            //Then
            assertThat(response.getStatus()).isEqualTo(401);
            assertThat(response.getBody().getCode()).isEqualTo("401");
            assertThat(response.getBody().getMessage()).isEqualTo("Headers에 토큰 형식의 값 찾을 수 없음");
        }

        @Test
        public void 문제_요청하기_제목_유효성_실패() throws Exception {
            //given
            String url = "/api/request";
            CreateRequestRequestDto requestDto = CreateRequestRequestDto.builder()
                    .description("설명")
                    .build();
            //when
            ApiResponse<ErrorResponse> response = requestMockApiCaller.sendPostRequest_WithAuthorization_USER_ParseErrorResponse(url, requestDto);
            //Then
            assertThat(response.getStatus()).isEqualTo(400);
            assertThat(response.getBody().getCode()).isEqualTo("400");
            assertThat(response.getBody().getMessage()).isEqualTo("잘못된 요청입니다.");
        }

        @Test
        public void 문제_요청하기_설명_유효성_실패() throws Exception {
            //given
            String url = "/api/request";
            CreateRequestRequestDto requestDto = CreateRequestRequestDto.builder()
                    .title("제목")
                    .build();
            //when
            ApiResponse<ErrorResponse> response = requestMockApiCaller.sendPostRequest_WithAuthorization_USER_ParseErrorResponse(url, requestDto);
            //Then
            assertThat(response.getStatus()).isEqualTo(400);
            assertThat(response.getBody().getCode()).isEqualTo("400");
            assertThat(response.getBody().getMessage()).isEqualTo("잘못된 요청입니다.");
        }
    }

    @DisplayName("/api/request/approve {patch}")
    @Nested
    class 문제_요청_상태_수정 {

        @Test
        public void ㄱㄳㄹ효() throws Exception {
            //given
            FlagRequestDto flagRequestDto = FlagRequestDto.builder()
                    .id(1L)
                    .flag(false)
                    .build();
            //when
            ApiResponse<String> response = requestMockApiCaller.updateFlag(flagRequestDto);
            //Then
            assertThat(response.getStatus()).isEqualTo(204);
        }

        @Test
        public void 게시글_상태_수정_유효성_아이디_실패_400() throws Exception {
            //given
            String url = "/api/request/approve";
            FlagRequestDto flagRequestDto = FlagRequestDto.builder()
                    .flag(false)
                    .build();
            //when
            ApiResponse<ErrorResponse> response = requestMockApiCaller.sendPatchRequest_ExpectErrorResponse(url, flagRequestDto);
            //Then
            assertThat(response.getStatus()).isEqualTo(400);
            assertThat(response.getBody().getCode()).isEqualTo("400");
            assertThat(response.getBody().getMessage()).isEqualTo("잘못된 요청입니다.");
        }

        @Test
        public void 상태_수정_일반유저_접근() throws Exception {
            //given
            String url = "/api/request/approve";
            FlagRequestDto flagRequestDto = FlagRequestDto.builder()
                    .id(1L)
                    .flag(false)
                    .build();

            //when
            ApiResponse<ErrorResponse> response = requestMockApiCaller.sendPatchRequest_USER_ExpectErrorResponse(url, flagRequestDto);
            //Then
            assertThat(response.getStatus()).isEqualTo(403);
            assertThat(response.getBody().getCode()).isEqualTo("403");
            assertThat(response.getBody().getMessage()).isEqualTo("Access Denied: Access is denied");
        }
    }

    @DisplayName("/api/request/{requestId}")
    @Nested
    class 단일_요청_게시글_조회 {

        @Test
        public void 단일_게시글_요청_조회_성공_200() throws Exception {
            //given
            Long requestId = 1L;
            LocalDateTime time = LocalDateTime.of(2024, 5, 19, 20, 0);
            RequestResponseDto responseDto = RequestResponseDto.builder()
                    .id(1L)
                    .title("제목")
                    .description("설명")
                    .memberId(1L)
                    .createAt(time)
                    .memberName("김무건")
                    .flag(false)
                    .build();
            given(requestService.getRequest(anyLong()))
                    .willReturn(responseDto);
            //when
            ApiResponse<RequestResponseDto> response = requestMockApiCaller.getRequest(requestId);
            //Then
            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.getBody().getId()).isEqualTo(1L);
            assertThat(response.getBody().getTitle()).isEqualTo("제목");
            assertThat(response.getBody().getCreateAt()).isEqualTo(time);
            assertThat(response.getBody().getDescription()).isEqualTo("설명");
            assertThat(response.getBody().getMemberId()).isEqualTo(1L);
            assertThat(response.getBody().getMemberName()).isEqualTo("김무건");
        }

        @Test
        public void 게시판_글_조회_pathvariable_음수_실패() throws Exception {
            //given
            String url = "/api/request/-1";
            //when
            ApiResponse<ErrorResponse> response = requestMockApiCaller.sendGetRequest_WithNoAuthorization_ExpectErrorResponse(url, null);
            //Then
            assertThat(response.getStatus()).isEqualTo(400);
            assertThat(response.getBody().getCode()).isEqualTo("8500");
            assertThat(response.getBody().getMessage()).isEqualTo("pathvariable은 양수로 처리를 해야됩니다.-1");
        }

    }

    @DisplayName("/api/request/member")
    @Nested
    class 내가_요청한_문제_페이징 {
        private LocalDateTime time;

        @BeforeEach
        void setUp() {
            time = LocalDateTime.of(2024, 5, 19, 20, 0);
            RequestResponseDto paging1 = createRequestDto(1L, true, "제목1", "설명1", 1L, "유재석", time);
            RequestResponseDto paging2 = createRequestDto(2L, true, "제목2", "설명2", 2L, "하하", time.plusHours(1));
            RequestResponseDto paging3 = createRequestDto(3L, false, "제목3", "설명3", 3L, "송지효", time.plusHours(2));

            List<RequestResponseDto> list = new ArrayList<>(Arrays.asList(paging1, paging2, paging3));
            Page<RequestResponseDto> pageResponse = new PageImpl<>(list);

            given(requestService.getRequestList(anyLong(), any(Pageable.class))).willReturn(pageResponse);
        }

        @Test
        public void 내가_요청한_문제_조회_페이징_성공_200_반환() throws Exception {
            //given

            //when
            ApiResponse<Page<RequestResponseDto>> response = requestMockApiCaller.getRequestList();
            //Then
            assertThat(response.getStatus()).isEqualTo(200);

            assertAll(
                    () -> assertThat(response.getBody().getTotalPages()).isEqualTo(1),
                    () -> assertThat(response.getBody().getSize()).isEqualTo(3),

                    //1L, true, "제목1", "설명1", 1L, "유재석", time
                    () -> assertThat(response.getBody().getContent().get(0).getId()).isEqualTo(1L),
                    () -> assertThat(response.getBody().getContent().get(0).getTitle()).isEqualTo("제목1"),
                    () -> assertThat(response.getBody().getContent().get(0).getDescription()).isEqualTo("설명1"),
                    () -> assertThat(response.getBody().getContent().get(0).getCreateAt()).isEqualTo(time),
                    () -> assertThat(response.getBody().getContent().get(0).isFlag()).isEqualTo(true),
                    () -> assertThat(response.getBody().getContent().get(0).getMemberId()).isEqualTo(1L),
                    () -> assertThat(response.getBody().getContent().get(0).getMemberName()).isEqualTo("유재석"),

                    // RequestResponseDto paging2 = createRequestDto(2L, true, "제목2", "설명2", 2L, "하하", time.plusHours(1));
                    () -> assertThat(response.getBody().getContent().get(1).getId()).isEqualTo(2L),
                    () -> assertThat(response.getBody().getContent().get(1).getTitle()).isEqualTo("제목2"),
                    () -> assertThat(response.getBody().getContent().get(1).getDescription()).isEqualTo("설명2"),
                    () -> assertThat(response.getBody().getContent().get(1).getCreateAt()).isEqualTo(time.plusHours(1)),
                    () -> assertThat(response.getBody().getContent().get(1).isFlag()).isEqualTo(true),
                    () -> assertThat(response.getBody().getContent().get(1).getMemberId()).isEqualTo(2L),
                    () -> assertThat(response.getBody().getContent().get(1).getMemberName()).isEqualTo("하하"),

                    //RequestResponseDto paging3 = createRequestDto(3L, false, "제목3", "설명3", 3L, "송지효", time.plusHours(2));
                    () -> assertThat(response.getBody().getContent().get(2).getId()).isEqualTo(3L),
                    () -> assertThat(response.getBody().getContent().get(2).getTitle()).isEqualTo("제목3"),
                    () -> assertThat(response.getBody().getContent().get(2).getDescription()).isEqualTo("설명3"),
                    () -> assertThat(response.getBody().getContent().get(2).isFlag()).isEqualTo(false),
                    () -> assertThat(response.getBody().getContent().get(2).getCreateAt()).isEqualTo(time.plusHours(2)),
                    () -> assertThat(response.getBody().getContent().get(2).getMemberId()).isEqualTo(3L),
                    () -> assertThat(response.getBody().getContent().get(2).getMemberName()).isEqualTo("송지효")
            );
        }
    }

    @DisplayName("patch /api/request")
    @Nested
    class 문제_요청_게시판_업데이트 {
        @BeforeEach
        void setUp() {

        }

        @Test
        public void 게시판_정보_업데이트_성공_204() throws Exception {
            //given
            UpdateRequestRequestDto requestDto = UpdateRequestRequestDto.builder()
                    .id(1L)
                    .title("변경 제목")
                    .description("변경 내용")
                    .build();
            //when
            ApiResponse<String> response = requestMockApiCaller.updateRequest(requestDto);
            //Then
            assertThat(response.getStatus()).isEqualTo(204);
        }

        @Test
        public void 게시판_정보_업데이트_아이디_유효성_실패_400() throws Exception {
            //given
            String url = "/api/request";
            UpdateRequestRequestDto requestDto = UpdateRequestRequestDto.builder()
                    .id(-1L)
                    .title("변경 제목")
                    .description("변경 내용")
                    .build();
            //when
            ApiResponse<ErrorResponse> response = requestMockApiCaller.sendPatchRequest_USER_ExpectErrorResponse(url, requestDto);
            //Then
            assertThat(response.getStatus()).isEqualTo(400);
            assertThat(response.getBody().getCode()).isEqualTo("400");
            assertThat(response.getBody().getMessage()).isEqualTo("잘못된 요청입니다.");
        }

        @Test
        public void 게시판_업데이트_이름_유효성_실패_400() throws Exception {
            String url = "/api/request";
            UpdateRequestRequestDto requestDto = UpdateRequestRequestDto.builder()
                    .id(1L)
                    .description("변경 내용")
                    .build();
            //when
            ApiResponse<ErrorResponse> response = requestMockApiCaller.sendPatchRequest_USER_ExpectErrorResponse(url, requestDto);
            //Then
            assertThat(response.getStatus()).isEqualTo(400);
            assertThat(response.getBody().getCode()).isEqualTo("400");
            assertThat(response.getBody().getMessage()).isEqualTo("잘못된 요청입니다.");
        }

        @Test
        public void 게시판_업데이트_설명_유효성_실패_400() throws Exception {
            //given
            String url = "/api/request";
            UpdateRequestRequestDto requestDto = UpdateRequestRequestDto.builder()
                    .id(1L)
                    .title("변경 제목")
                    .build();
            //when
            ApiResponse<ErrorResponse> response = requestMockApiCaller.sendPatchRequest_USER_ExpectErrorResponse(url, requestDto);
            //Then
            assertThat(response.getStatus()).isEqualTo(400);
            assertThat(response.getBody().getCode()).isEqualTo("400");
            assertThat(response.getBody().getMessage()).isEqualTo("잘못된 요청입니다.");

        }
    }

    @DisplayName("Delete /api/request/{requestId}")
    @Nested
    class 요청_게시글_삭제 {

        @BeforeEach
        void setUp() {

        }

        @Test
        public void 게시판_삭제_성공_204() throws Exception {
            //given
            Long requestId = 1L;
            //when
            ApiResponse<String> response = requestMockApiCaller.deleteRequestById(requestId);
            //Then
            assertThat(response.getStatus()).isEqualTo(204);
        }

        @Test
        public void 게시판_삭제_path_음수() throws Exception {
            //given
            String url = "/api/request/{path}";
            Long path  = -1L;
            //when
            ApiResponse<ErrorResponse> response = requestMockApiCaller.sendDeleteRequest_USER_ExpectErrorResponse(url, null, path);
            //Then
            assertThat(response.getStatus()).isEqualTo(400);
            assertThat(response.getBody().getCode()).isEqualTo("8500");
            assertThat(response.getBody().getMessage()).isEqualTo("pathvariable은 양수로 처리를 해야됩니다.-1");
        }

    }


}