package com.cstudy.moduleapi.controller;

import com.cstudy.moduleapi.ApiResponse;
import com.cstudy.moduleapi.config.ControllerTest;
import com.cstudy.moduleapi.dto.member.MemberLoginRequest;
import com.cstudy.moduleapi.dto.member.MemberLoginResponse;
import com.cstudy.moduleapi.dto.member.MemberSignupRequest;
import com.cstudy.moduleapi.dto.refresh.RefreshTokenDto;
import com.cstudy.moduleapi.enums.MemberTestEnum;
import com.cstudy.moduleapi.exception.ErrorResponse;
import org.junit.jupiter.api.*;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
            ApiResponse<ErrorResponse> response = memberMockApiCaller.fail(url, request);

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
            ApiResponse<ErrorResponse> response = memberMockApiCaller.fail(url, request);

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
            ApiResponse<ErrorResponse> response = memberMockApiCaller.fail(url, request);

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
            ApiResponse<ErrorResponse> response = memberMockApiCaller.fail(url, request);

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
            ApiResponse<ErrorResponse> response = memberMockApiCaller.fail(url, request);

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

}
