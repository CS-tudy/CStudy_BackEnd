package com.cstudy.moduleapi.domain.refresh.application.impl;

import com.cstudy.moduleapi.application.refershToken.RefreshTokenService;
import com.cstudy.moduleapi.config.redis.RedisService;
import com.cstudy.moduleapi.dto.refresh.RefreshTokenDto;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.repository.refresh.RefreshTokenRepositry;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
public class RefreshTokenServiceImplTest {


    private static final String REFRESH_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGVtYWlsLmNvbSIsIm1lbWJlcklkIjoxLCJyb2xlcy" +
            "I6WyJVU0VSIl0sImlhdCI6MTY4MTcyNjI0NywiZXhwIjoxNjgyMzMxMDQ3fQ.YCQ1euB_cHdfLOzporKX3JaaqvRsOs7-x5y1QvqyFvs";

    private static final String email = "test1234@email.com";
    private static final String password = "1234!";


    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private RefreshTokenRepositry refreshTokenRepositry;

    @Autowired
    private RedisService redisService;

    @BeforeEach
    void setUp() {
        RefreshTokenDto refreshTokenDto = RefreshTokenDto.builder()
                .refreshToken(REFRESH_TOKEN)
                .build();

        redisService.setValues(REFRESH_TOKEN);
    }

    @Test
    @DisplayName("Refresh Token 생성")
    public void addRefreshTokenWithValid() throws Exception {
        //given
        Member member = Member.builder()
                .email(email)
                .password(password)
                .build();
        //when
        refreshTokenService.addRefreshToken(REFRESH_TOKEN);
        //Then
        assertThat(refreshTokenRepositry.count()).isNotNull();
    }

    @Test
    @DisplayName("delete Refresh Token Valid")
    public void deleteRefreshTokenWithValidToken() throws Exception {
        //given
        redisService.setValues(REFRESH_TOKEN);

        //when
        refreshTokenService.deleteRefreshToken(REFRESH_TOKEN);

        //Then
        Assertions.assertThat(redisService.getValues(REFRESH_TOKEN)).isNull();
    }

    @Test
    @DisplayName("재발급 토큰 찾기")
    public void findRefreshToken() throws Exception {
        RefreshTokenDto refreshTokenDto = RefreshTokenDto.builder()
                .refreshToken(REFRESH_TOKEN)
                .build();

        String values = redisService.getValues(REFRESH_TOKEN);
        //When, Then
        assertThat(values).isEqualTo(REFRESH_TOKEN);
    }
}