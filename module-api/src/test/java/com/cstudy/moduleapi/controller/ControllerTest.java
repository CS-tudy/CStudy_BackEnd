package com.cstudy.moduleapi.controller;

import com.cstudy.moduleapi.application.member.MemberService;
import com.cstudy.moduleapi.dto.member.MemberLoginRequest;
import com.cstudy.moduleapi.dto.member.MemberLoginResponse;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

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
    protected MemberRepository memberRepository;

    @MockBean
    protected MemberService memberService;

    protected MemberMockApiCaller memberMockApiCaller;


    protected void setup() throws Exception {
        memberMockApiCaller = new MemberMockApiCaller(mockMvc, objectMapper);

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

