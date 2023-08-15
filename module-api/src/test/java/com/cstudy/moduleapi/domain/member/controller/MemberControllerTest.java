package com.cstudy.moduleapi.domain.member.controller;

import com.cstudy.moduleapi.application.member.FileService;
import com.cstudy.moduleapi.application.member.MemberService;
import com.cstudy.moduleapi.application.refershToken.RefreshTokenService;
import com.cstudy.moduleapi.config.jwt.util.JwtTokenizer;
import com.cstudy.moduleapi.dto.member.*;
import com.cstudy.moduleapi.dto.refresh.RefreshTokenDto;
import com.cstudy.moduleapi.enums.MemberTestEnum;
import com.cstudy.modulecommon.domain.role.RoleEnum;
import com.cstudy.modulecommon.error.member.NotFoundMemberEmail;
import com.cstudy.modulecommon.util.LoginUserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@ActiveProfiles("local")
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @MockBean
    private FileService fileService;

    @MockBean
    private MemberService memberService;

    @MockBean
    private RefreshTokenService refreshTokenService;

    private String token;

    @BeforeEach
    void setUp() {
        token = jwtTokenizer.createAccessToken(1L, MemberTestEnum.VALID_EMAIL.getMessage(), List.of(RoleEnum.CUSTOM.getRoleName()));
    }

    @Test
    @DisplayName("회원가입 성공")
    void signupWithValidParameter() throws Exception {
        //given
        MemberSignupRequest memberSignupRequest = MemberSignupRequest.builder()
                .email(MemberTestEnum.VALID_EMAIL.getMessage())
                .password(MemberTestEnum.VALID_PASSWORD.getMessage())
                .name(MemberTestEnum.VALID_NAME.getMessage())
                .build();
        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(memberSignupRequest))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(print());
        //then
    }

    @Test
    @DisplayName("회원가입 실패 (이메일 유효성 실패)")
    void signupWithInvalidCauseNotValidEmail() throws Exception {
        //given
        MemberSignupRequest memberSignupRequest = MemberSignupRequest.builder()
                .email("invalidEmail")
                .password(MemberTestEnum.VALID_PASSWORD.getMessage())
                .name(MemberTestEnum.VALID_NAME.getMessage())
                .build();
        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(memberSignupRequest))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andDo(print());
        //then
        //verify()
    }

    @Test
    @DisplayName("회원가입 실패 (비밀번호 유효성 실패)")
    void signupWithInvalidCauseNotValidPassword() throws Exception {
        //given
        MemberSignupRequest memberSignupRequest = MemberSignupRequest.builder()
                .email(MemberTestEnum.VALID_PASSWORD.getMessage())
                .password("1234")
                .name(MemberTestEnum.VALID_NAME.getMessage())
                .build();
        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(memberSignupRequest))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andDo(print());
        //then
        //verify()
    }

    @Test
    @DisplayName("회원가입 실패 (이름 유효성 실패)")
    void signupWithInvalidCauseBlackName() throws Exception {
        //given
        MemberSignupRequest memberSignupRequest = MemberSignupRequest.builder()
                .email(MemberTestEnum.VALID_EMAIL.getMessage())
                .password(MemberTestEnum.VALID_PASSWORD.getMessage())
                .build();
        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(memberSignupRequest))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andDo(print());
        //then
        //verify()
    }

    @Test
    @DisplayName("로그인 성공")
    void loginWithValidEmailAndPassword() throws Exception {
        //given
        MemberLoginRequest memberLoginRequest = MemberLoginRequest.builder()
                .email(MemberTestEnum.VALID_EMAIL.getMessage())
                .password(MemberTestEnum.VALID_PASSWORD.getMessage())
                .build();

        MemberLoginResponse memberLoginResponse = MemberLoginResponse.builder()
                .email(MemberTestEnum.VALID_EMAIL.getMessage())
                .accessToken(token)
                .refreshToken("refreshToken")
                .name(MemberTestEnum.VALID_NAME.getMessage())
                .build();
        // when

        given(memberService.login(any(MemberLoginRequest.class))).willReturn(memberLoginResponse);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(memberLoginRequest))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.email").value(MemberTestEnum.VALID_EMAIL.getMessage()))
                .andExpect(jsonPath("$.accessToken").value(token))
                .andExpect(jsonPath("$.refreshToken").value("refreshToken"))
                .andExpect(jsonPath("$.name").value(MemberTestEnum.VALID_NAME.getMessage()))
                .andDo(print());
        //then
        verify(memberService).login(any(MemberLoginRequest.class));
    }

    @Test
    @DisplayName("로그인 성공 - 이메일 불일치")
    void loginWithINValidEmailAndValidPassword() throws Exception {
        //given
        MemberLoginRequest memberLoginRequest = MemberLoginRequest.builder()
                .email(MemberTestEnum.INVALID_EMAIL.getMessage())
                .password(MemberTestEnum.VALID_PASSWORD.getMessage())
                .build();
        // when

        given(memberService.login(any(MemberLoginRequest.class))).willThrow(new NotFoundMemberEmail(MemberTestEnum.INVALID_EMAIL.getMessage()));

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(memberLoginRequest))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(print());
        //then
    }

    @Test
    @DisplayName("로그인 성공 - 비밀번호 불일치")
    void loginWithValidEmailAndInValidPassword() throws Exception {
        //given
        MemberLoginRequest memberLoginRequest = MemberLoginRequest.builder()
                .email(MemberTestEnum.VALID_EMAIL.getMessage())
                .password(MemberTestEnum.INVALID_PASSWORD.getMessage())
                .build();
        // when

        given(memberService.login(any(MemberLoginRequest.class)))
                .willThrow(new NotFoundMemberEmail(MemberTestEnum.INVALID_EMAIL.getMessage()));

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(memberLoginRequest))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(print());
        //then
    }

    @Test
    @DisplayName("로그아웃 성공")
    void logoutWithValid() throws Exception {
        //given
        RefreshTokenDto refreshTokenDto = RefreshTokenDto.builder()
                .refreshToken("refreshToken")
                .build();
        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/logout")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                                .content(objectMapper.writeValueAsBytes(refreshTokenDto))
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print());
        //then
        //verify()
    }

    @Test
    @DisplayName("토큰 재발급 성공")
    void refreshTokenWithValid() throws Exception {
        //given
        String refreshToken = "refreshToken";
        RefreshTokenDto refreshTokenDto = RefreshTokenDto.builder()
                .refreshToken("refresh")
                .build();

        MemberLoginResponse memberLoginResponse = MemberLoginResponse.builder()
                .email(MemberTestEnum.VALID_EMAIL.getMessage())
                .accessToken(token)
                .refreshToken("refreshToken")
                .name(MemberTestEnum.VALID_NAME.getMessage())
                .build();

        given(refreshTokenService.AccessTokenWithRefreshToken(refreshTokenDto)).willReturn(memberLoginResponse);

        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/refreshToken")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                                .content(objectMapper.writeValueAsBytes(refreshTokenDto))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.email").value(MemberTestEnum.VALID_EMAIL.getMessage()))
                .andExpect(jsonPath("$.accessToken").value(token))
                .andExpect(jsonPath("$.refreshToken").value(refreshToken))
                .andExpect(jsonPath("$.name").value(MemberTestEnum.VALID_NAME.getMessage()))
                .andDo(print());
        //then
        verify(refreshTokenService).AccessTokenWithRefreshToken(any(RefreshTokenDto.class));
    }

    @Test
    @DisplayName("마이페이지 성공")
    void myPageWithValid() throws Exception {
        //given
        LoginUserDto loginUserDto = LoginUserDto.builder()
                .memberId(1L)
                .roles(List.of(RoleEnum.CUSTOM.getRoleName()))
                .build();

        MyPageResponseDto myPageResponseDto = MyPageResponseDto.builder()
                .id(1L)
                .email(MemberTestEnum.VALID_PASSWORD.getMessage())
                .name(MemberTestEnum.VALID_NAME.getMessage())
                .build();

        given(memberService.getMyPage(any(Long.class))).willReturn(myPageResponseDto);

        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/mypage")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

        //then
        //verify()
    }

    @Test
    @DisplayName("비밀번호 수정")
    void changePassword() throws Exception {
        //given
        MemberPasswordChangeRequest memberPasswordChangeRequest = MemberPasswordChangeRequest.builder()
                .oldPassword(MemberTestEnum.VALID_PASSWORD.getMessage())
                .newPassword("changePassword1234!")
                .build();
        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/mypage/password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                                .content(objectMapper.writeValueAsBytes(memberPasswordChangeRequest))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
        //then
        //verify()
    }
}