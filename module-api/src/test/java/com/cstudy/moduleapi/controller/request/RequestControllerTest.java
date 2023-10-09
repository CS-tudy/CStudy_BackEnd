package com.cstudy.moduleapi.controller.request;

import com.cstudy.moduleapi.ApiResponse;
import com.cstudy.moduleapi.config.ControllerTest;
import com.cstudy.moduleapi.dto.request.CreateRequestRequestDto;
import com.cstudy.moduleapi.dto.request.FlagRequestDto;
import com.cstudy.moduleapi.dto.request.RequestResponseDto;
import com.cstudy.moduleapi.exception.ErrorResponse;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
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
            assertThat(response.getBody().getTitle()).isEqualTo(200);
            assertThat(response.getBody().getCreateAt()).isEqualTo(200);
            assertThat(response.getBody().getDescription()).isEqualTo(200);
            assertThat(response.getBody().getMemberId()).isEqualTo(200);
            assertThat(response.getBody().getMemberName()).isEqualTo(200);
        }

    }
}