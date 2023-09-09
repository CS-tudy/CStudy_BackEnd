package com.cstudy.moduleapi.controller;

import com.cstudy.moduleapi.ApiResponse;
import com.cstudy.moduleapi.dto.member.MemberLoginRequest;
import com.cstudy.moduleapi.dto.member.MemberLoginResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MemberControllerTest extends ControllerTest {

    @BeforeEach
    void setUp() throws Exception {
        super.setup();
    }

    @AfterEach
    void cleanUp() {
        super.cleanup();
    }

    @DisplayName("GET /api/member/login 테스트")
    @Test
    void testLogin() throws Exception {
        MemberLoginResponse mockResponse = MemberLoginResponse.builder()
                .email("test@example.com")
                .accessToken("sampleAccessToken")
                .refreshToken("sampleRefreshToken")
                .name("Test User")
                .build();

        ApiResponse<MemberLoginResponse> response = memberMockApiCaller.login();

        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(response.getBody().getName()).isEqualTo("Test User");
        assertThat(response.getBody().getEmail()).isEqualTo("test@example.com");
        assertThat(response.getBody().getAccessToken()).isEqualTo("sampleAccessToken");
        assertThat(response.getBody().getRefreshToken()).isEqualTo("sampleRefreshToken");
    }
}
