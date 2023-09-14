package com.cstudy.moduleapi.config;

import com.cstudy.moduleapi.application.member.MemberService;
import com.cstudy.moduleapi.config.jwt.util.JwtTokenizer;
import com.cstudy.moduleapi.controller.apiCaller.MemberMockApiCaller;
import com.cstudy.moduleapi.dto.member.MemberLoginRequest;
import com.cstudy.moduleapi.dto.member.MemberLoginResponse;
import com.cstudy.moduleapi.enums.MemberTestEnum;
import com.cstudy.modulecommon.domain.role.RoleEnum;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected JwtTokenizer jwtTokenizer;

    @Autowired
    protected MemberRepository memberRepository;

    @MockBean
    protected MemberService memberService;

    protected MemberMockApiCaller memberMockApiCaller;

    public static String CUSTOM_USER = null;
    public static String ADMIN_USER = null;
    public static String INVALID_TOKEN = null;


    protected void setup() throws Exception {
        memberMockApiCaller = new MemberMockApiCaller(mockMvc, objectMapper);


        CUSTOM_USER = jwtTokenizer.createAccessToken(1L, MemberTestEnum.CUSTOM_EMAIL.getMessage(), List.of(RoleEnum.CUSTOM.getRoleName()));
        ADMIN_USER = jwtTokenizer.createAccessToken(1L, MemberTestEnum.ADMIN_EMAIL.getMessage(), List.of(RoleEnum.ADMIN.getRoleName()));
        INVALID_TOKEN = jwtTokenizer.createAccessToken(2L, MemberTestEnum.VALID_EMAIL.getMessage(), List.of("INVALID_ROLE"));

        given(memberService.login(any(MemberLoginRequest.class))).willReturn(MemberLoginResponse.builder()
                .email("test@example.com")
                .accessToken("sampleAccessToken")
                .refreshToken("sampleRefreshToken")
                .name("Test User")
                .build());
    }

    protected void cleanup() {
        memberRepository.deleteAll();
    }

}

