package com.cstudy.moduleapi.controller;

import com.cstudy.moduleapi.ApiResponse;
import com.cstudy.moduleapi.config.ControllerTest;
import com.cstudy.moduleapi.dto.member.*;
import com.cstudy.moduleapi.dto.refresh.RefreshTokenDto;
import com.cstudy.moduleapi.enums.MemberTestEnum;
import com.cstudy.moduleapi.exception.ErrorResponse;
import com.cstudy.modulecommon.util.LoginUserDto;
import org.junit.jupiter.api.*;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

class MemberControllerTest extends ControllerTest {

    @BeforeEach
    void setUp() throws Exception {
        super.setup();
    }

    @AfterEach
    void cleanUp() {
        super.cleanup();
    }

    @DisplayName("POST /api/members/signup" + "PermitAll")
    @Nested
    class signup {
        @Test
        public void ROLE_CUSTOM_권한을_가지는_회원가입() throws Exception {
            //given
            MemberSignupRequest request = MemberSignupRequest.builder()
                    .email(MemberTestEnum.CUSTOM_EMAIL.getMessage())
                    .name(MemberTestEnum.VALID_NAME.getMessage())
                    .password(MemberTestEnum.VALID_PASSWORD.getMessage())
                    .build();
            //when
            ApiResponse<String> response = memberMockApiCaller.signup(request);

            //Then
            assertThat(response.getStatus()).isEqualTo(201);
            assertThat(response.getBody()).isEqualTo("");
        }

        @Test
        public void 회원가입_유효성_실패_이메일() throws Exception {
            //given
            String url = "/api/member/signup";

            MemberSignupRequest request = MemberSignupRequest.builder()
                    .name(MemberTestEnum.VALID_NAME.getMessage())
                    .password(MemberTestEnum.VALID_PASSWORD.getMessage())
                    .build();
            //when
            ApiResponse<ErrorResponse> response = memberMockApiCaller.sendPostRequestAndParseErrorResponse(url, request);

            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(400),
                    () -> assertThat(response.getBody().getCode()).isEqualTo("400"),
                    () -> assertThat(response.getBody().getMessage()).isEqualTo("잘못된 요청입니다."),
                    () -> assertThat(response.getBody().getValidation().get("email")).isEqualTo(" 이메일을 입력하세요")
            );
        }

        @Test
        public void 회원가입_유효성_실패_이름() throws Exception {
            //given
            String url = "/api/member/signup";

            MemberSignupRequest request = MemberSignupRequest.builder()
                    .email(MemberTestEnum.CUSTOM_EMAIL.getMessage())
                    .password(MemberTestEnum.VALID_PASSWORD.getMessage())
                    .build();
            //when
            ApiResponse<ErrorResponse> response = memberMockApiCaller.sendPostRequestAndParseErrorResponse(url, request);

            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(400),
                    () -> assertThat(response.getBody().getCode()).isEqualTo("400"),
                    () -> assertThat(response.getBody().getMessage()).isEqualTo("잘못된 요청입니다."),
                    () -> assertThat(response.getBody().getValidation().get("name")).isEqualTo("이름을 입력하세요")
            );
        }

        @Test
        public void 회원가입_유효성_실패_비밀번호() throws Exception {
            //given
            String url = "/api/member/signup";

            MemberSignupRequest request = MemberSignupRequest.builder()
                    .email(MemberTestEnum.CUSTOM_EMAIL.getMessage())
                    .name(MemberTestEnum.VALID_NAME.getMessage())
                    .build();
            //when
            ApiResponse<ErrorResponse> response = memberMockApiCaller.sendPostRequestAndParseErrorResponse(url, request);

            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(400),
                    () -> assertThat(response.getBody().getCode()).isEqualTo("400"),
                    () -> assertThat(response.getBody().getMessage()).isEqualTo("잘못된 요청입니다."),
                    () -> assertThat(response.getBody().getValidation().get("password")).isEqualTo("비밀번호를 입력하세요")
            );
        }
    }

    @DisplayName("POST /api/member/login" + "PermitALL")
    @Nested
    class login {

        @Test
        public void 로그인_성공() throws Exception {
            //given
            String url = "/api/member/login";
            MemberLoginRequest request = MemberLoginRequest.builder()
                    .email(MemberTestEnum.VALID_EMAIL.getMessage())
                    .password(MemberTestEnum.VALID_PASSWORD.getMessage())
                    .build();
            //when
            ApiResponse<MemberLoginResponse> response = memberMockApiCaller.login(url, request);

            MemberLoginResponse memberLoginResponse = MemberLoginResponse.builder()
                    .accessToken("accessToken")
                    .refreshToken("refreshToken")
                    .name(MemberTestEnum.CUSTOM_NAME.getMessage())
                    .email(MemberTestEnum.VALID_EMAIL.getMessage())
                    .build();
            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(201),
                    () -> assertThat(response.getBody().getAccessToken()).isEqualTo("accessToken"),
                    () -> assertThat(response.getBody().getRefreshToken()).isEqualTo("refreshToken"),
                    () -> assertThat(response.getBody().getName()).isEqualTo(MemberTestEnum.CUSTOM_NAME.getMessage()),
                    () -> assertThat(response.getBody().getEmail()).isEqualTo(MemberTestEnum.VALID_EMAIL.getMessage())
            );
        }

        @Test
        public void 로그인_유효성_실패_이메일() throws Exception {
            //given
            String url = "/api/member/login";
            MemberLoginRequest request = MemberLoginRequest.builder()
                    .password(MemberTestEnum.VALID_PASSWORD.getMessage())
                    .build();
            //when
            ApiResponse<ErrorResponse> response = memberMockApiCaller.sendPostRequestAndParseErrorResponse(url, request);

            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(400),
                    () -> assertThat(response.getBody().getCode()).isEqualTo("400"),
                    () -> assertThat(response.getBody().getMessage()).isEqualTo("잘못된 요청입니다."),
                    () -> assertThat(response.getBody().getValidation().get("email")).isEqualTo("이메일은 필수적으로 입력하세요.")
            );
        }

        @Test
        public void 로그인_유효성_실패_비밀번호() throws Exception {
            //given
            String url = "/api/member/login";
            MemberLoginRequest request = MemberLoginRequest.builder()
                    .email(MemberTestEnum.VALID_EMAIL.getMessage())
                    .build();
            //when
            ApiResponse<ErrorResponse> response = memberMockApiCaller.sendPostRequestAndParseErrorResponse(url, request);

            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(400),
                    () -> assertThat(response.getBody().getCode()).isEqualTo("400"),
                    () -> assertThat(response.getBody().getMessage()).isEqualTo("잘못된 요청입니다."),
                    () -> assertThat(response.getBody().getValidation().get("password")).isEqualTo("비밀번호를 입력하세요.")
            );
        }
    }

    @DisplayName("DELETE /api/member/logout" + "ROLE_CUSTOM, ROLE_ADMIN")
    @Nested
    class logout {

        @Test
        public void 로그아웃_성공_테스트() throws Exception {
            //given
            String url = "/api/member/logout";
            RefreshTokenDto request = RefreshTokenDto.builder()
                    .refreshToken("refreshToken")
                    .build();
            //when
            ApiResponse<String> response = memberMockApiCaller.logout(url, request);

            //Then
            assertThat(response.getStatus()).isEqualTo(204);
        }

        @Test
        public void 로그아웃_유효성_리프레시토큰_없음() throws Exception {
            //given
            String url = "/api/member/logout";

            RefreshTokenDto request = RefreshTokenDto.builder()
                    .build();
            //when
            ApiResponse<ErrorResponse> response = memberMockApiCaller.deleteFail(url, request);

            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(400),
                    () -> assertThat(response.getBody().getCode()).isEqualTo("400"),
                    () -> assertThat(response.getBody().getMessage()).isEqualTo("잘못된 요청입니다."),
                    () -> assertThat(response.getBody().getValidation().get("refreshToken")).isEqualTo("refresh token을 입력하세요.")
            );
        }

        @Test
        public void 로그아웃_토큰_없음() throws Exception {
            //given
            String url = "/api/member/logout";

            RefreshTokenDto request = RefreshTokenDto.builder()
                    .build();
            //when
            ApiResponse<ErrorResponse> response = memberMockApiCaller.emptyTokenlogout(url, request);

            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(401),
                    () -> assertThat(response.getBody().getCode()).isEqualTo("401"),
                    () -> assertThat(response.getBody().getMessage()).isEqualTo("Headers에 토큰 형식의 값 찾을 수 없음")
            );
        }

    }


    @DisplayName("전략패턴으로 email , name 중복체크")
    @Nested
    class duplicateSignup {
        @Test
        public void 이메일_중복검사_중복된_이메일이_존재하지_않습니다() throws Exception {
            //given

            String type = "email";
            String email = MemberTestEnum.CUSTOM_NAME.getMessage();
            given(duplicateServiceFinder.getVerifyResponseDto(eq(type), eq(email)))
                    .willReturn(
                            DuplicateResponseDto.builder()
                                    .verify("true")
                                    .build()
                    );

            //when
            ApiResponse<String> response = memberMockApiCaller.checkEmailDuplication(email);
            //Then
            assertThat(response.getStatus()).isEqualTo(200);
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(200),
                    () -> assertThat(response.getBody()).isEqualTo("{\"verify\":\"true\"}")
            );
        }

        @Test
        public void 이메일_중복검사_중복된_이메일이_존재합니다() throws Exception {
            //given
            String type = "email";
            String value = "duplicate@email.com";

            given(duplicateServiceFinder.getVerifyResponseDto(eq(type), eq(value)))
                    .willReturn(
                            DuplicateResponseDto.builder()
                                    .verify("false")
                                    .build()
                    );
            //when
            ApiResponse<String> response = memberMockApiCaller.checkEmailDuplication(value);
            //Then
            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.getBody()).isEqualTo("{\"verify\":\"false\"}");
        }


        @Test
        public void 이메일_중복검사_이메일이_쿼리스트링에_없습니다() throws Exception {
            //when
            ApiResponse<String> response = memberMockApiCaller.checkEmailDuplication(null);
            //Then
            assertThat(response.getStatus()).isEqualTo(400);
        }
    }

    @DisplayName("/api/name 이름 중복 체크")
    @Nested
    class checkNameDuplicate {

        @Test
        public void 이름_중복검사_중복된_이름이_없습니다() throws Exception {
            //given

            String type = "name";
            String name = MemberTestEnum.CUSTOM_NAME.name();

            given(duplicateServiceFinder.getVerifyResponseDto(eq(type), eq(name)))
                    .willReturn(
                            DuplicateResponseDto.builder()
                                    .verify("true")
                                    .build()
                    );
            //when
            ApiResponse<String> response = memberMockApiCaller.checkNameDuplication(name);
            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(200),
                    () -> assertThat(response.getBody()).isEqualTo("{\"verify\":\"true\"}")
            );
        }

        @Test
        public void 이름_중복검사_쿼리스트링_없음() throws Exception {
            //when
            ApiResponse<String> response = memberMockApiCaller.checkNameDuplication(null);
            //Then
            assertThat(response.getStatus()).isEqualTo(400);
        }

        @Test
        public void 이름_중복검사_중복된_이름이_있습니다() throws Exception {
            //given
            String type = "name";
            String value = "duplicate@email.com";

            given(duplicateServiceFinder.getVerifyResponseDto(eq(type), eq(value)))
                    .willReturn(
                            DuplicateResponseDto.builder()
                                    .verify("false")
                                    .build()
                    );
            //when
            ApiResponse<String> response = memberMockApiCaller.checkNameDuplication(value);
            //Then
            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.getBody()).isEqualTo("{\"verify\":\"false\"}");
        }

    }

    @DisplayName("/api/member/refreshToken")
    @Nested
    class reCreateAccessTokenWithRefresh {

        private RefreshTokenDto request;

        @BeforeEach
        void setUp() {
            request = RefreshTokenDto.builder()
                    .refreshToken("refreshToken")
                    .build();

            MemberLoginResponse loginResponse = MemberLoginResponse.builder()
                    .accessToken("accessToken")
                    .refreshToken("refreshToken")
                    .email(MemberTestEnum.VALID_EMAIL.getMessage())
                    .name(MemberTestEnum.VALID_NAME.getMessage())
                    .build();

            given(refreshTokenService.AccessTokenWithRefreshToken(eq(request)))
                    .willReturn(loginResponse);
        }

        @Test
        public void 리프레시토큰_어세스토큰_재발급_성공() throws Exception {
            request = RefreshTokenDto.builder()
                    .refreshToken("refreshToken")
                    .build();
            ApiResponse<MemberLoginResponse> response = memberMockApiCaller.refreshTokenWithAccessToken(request);
            //Then
            assertThat(response.getStatus()).isEqualTo(200);
            assertAll(
                    () -> assertThat(response.getBody().getName()).isEqualTo(MemberTestEnum.VALID_NAME.getMessage()),
                    () -> assertThat(response.getBody().getEmail()).isEqualTo(MemberTestEnum.VALID_EMAIL.getMessage()),
                    () -> assertThat(response.getBody().getAccessToken()).isEqualTo("accessToken"),
                    () -> assertThat(response.getBody().getRefreshToken()).isEqualTo("refreshToken")
            );
        }

        @Test
        public void 리프레시토큰_유효성_검사_실패() throws Exception {
            //given
            String url = "/api/member/refreshToken";
            RefreshTokenDto refreshTokenDto = RefreshTokenDto.builder().build();
            //when
            ApiResponse<ErrorResponse> response = memberMockApiCaller.sendPostRequestAndParseErrorResponse(url, refreshTokenDto);
            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(400),
                    () -> assertThat(response.getBody().getCode()).isEqualTo("400"),
                    () -> assertThat(response.getBody().getMessage()).isEqualTo("잘못된 요청입니다."),
                    () -> assertThat(response.getBody().getValidation().get("refreshToken")).isEqualTo("refresh token을 입력하세요.")
            );
        }
    }

    @DisplayName("/api/upload, /api/download S3 관련 테스트")
    @Nested
    class S3_file {

        private MockMultipartFile mockFile;

        @Test
        public void S3_파일_업로드_성공() throws Exception {
            //given
            mockFile = new MockMultipartFile("files", "test.txt", "text/plain", "이미지".getBytes());

            //when
            ApiResponse<String> response = memberMockApiCaller.uploadFiles(mockFile);
            //Then
            assertThat(response.getStatus()).isEqualTo(200);
        }

        @Test
        @DisplayName("request에서 files로 하지 않으면 오류가 발생")
        public void S3_파일_업로드_request_요청값_유효성_실패() throws Exception {
            //given
            mockFile = new MockMultipartFile("error", "test.txt", "text/plain", "이미지".getBytes());

            //when
            ApiResponse<String> response = memberMockApiCaller.uploadFiles(mockFile);
            //Then
            assertThat(response.getStatus()).isEqualTo(400);
        }

        @Test
        public void S3_버켓에서_사진_가져오기_성공() throws Exception {
            //given
            String text = "success";
            byte[] byteArray = text.getBytes(StandardCharsets.UTF_8);

            given(fileService.getImageBytes(any(LoginUserDto.class)))
                    .willReturn(byteArray);

            //when
            ApiResponse<byte[]> response = memberMockApiCaller.downloadFile();
            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(200),

                    () -> assertThat(response.getBody().length).isEqualTo(7),
                    () -> assertThat(new String(response.getBody(), StandardCharsets.UTF_8)).isEqualTo("success")
            );
        }

        @Test
        public void S3_버켓에서_사진_가져오기_실패_토큰이_없음() throws Exception {
            //given
            String url = "/api/member/download";
            //when
            ApiResponse<ErrorResponse> response = memberMockApiCaller.sendGetRequest_WithNoAuthorization_ExpectErrorResponse(url, null);
            //Then

            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(401),
                    () -> assertThat(response.getBody().getCode()).isEqualTo("401"),
                    () -> assertThat(response.getBody().getMessage()).isEqualTo("Headers에 토큰 형식의 값 찾을 수 없음")
            );
        }

    }

    @DisplayName("/api/member/member")
    @Nested
    class MemberLoin_info {

        @Test
        public void 마이페이지_성공() throws Exception {
            //given

            given(memberService.getMyPage(anyLong()))
                    .willReturn(MyPageResponseDto.builder()
                            .id(1L)
                            .email(MemberTestEnum.CUSTOM_EMAIL.getMessage())
                            .name(MemberTestEnum.CUSTOM_NAME.getMessage())
                            .build());
            //when
            ApiResponse<MyPageResponseDto> response = memberMockApiCaller.getMyPage();
            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(200)
            );
        }

        @Test
        public void 마이페이지_토큰_없음() throws Exception {
            //given
            String url = "/api/member/member";
            //when
            ApiResponse<ErrorResponse> response = memberMockApiCaller.sendGetRequest_WithNoAuthorization_ExpectErrorResponse(url, null);
            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(401),

                    () -> assertThat(response.getBody().getCode()).isEqualTo("401"),
                    () -> assertThat(response.getBody().getMessage()).isEqualTo("Headers에 토큰 형식의 값 찾을 수 없음")
            );
        }

    }

    @Nested
    class changePassword {

        @Test
        public void 관리자_비밀번호_변경_200() throws Exception {
            //given
            MemberPasswordChangeRequest request = MemberPasswordChangeRequest.builder()
                    .newPassword("newPassword1234!")
                    .oldPassword("admin1234!")
                    .build();
            //when
            ApiResponse<String> response = memberMockApiCaller.changePasword(request);
            //Then
            assertThat(response.getStatus()).isEqualTo(204);
        }

        @Test
        public void 관리자_비밀번호_변경_401() throws Exception {
            //given
            String url = "/api/member/member";
            //when
            ApiResponse<ErrorResponse> response = memberMockApiCaller.sendPatchRequest_WithNoAuthorization_ExpectErrorResponse(url, null);
            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(401),

                    () -> assertThat(response.getBody().getCode()).isEqualTo("401"),
                    () -> assertThat(response.getBody().getMessage()).isEqualTo("Headers에 토큰 형식의 값 찾을 수 없음")
            );
        }

    }


}
