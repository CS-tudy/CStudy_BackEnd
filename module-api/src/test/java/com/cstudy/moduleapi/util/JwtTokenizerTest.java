package com.cstudy.moduleapi.util;

import com.cstudy.moduleapi.config.jwt.util.JwtTokenizer;
import com.cstudy.modulecommon.domain.role.RoleEnum;
import io.jsonwebtoken.Claims;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

@ActiveProfiles("test")
class JwtTokenizerTest {
    private static final String secretKey = "0123456789012345678901234567890123456789";
    private static final String refreshKey = "0123456789012345678901234567890123456789";

    private JwtTokenizer jwtTokenizer;

    @BeforeEach
    void setUp() {
        jwtTokenizer = new JwtTokenizer(secretKey, refreshKey);
    }

    @Test
    @DisplayName("Token 생성하기")
    public void createValidToken() throws Exception {
        // given
        Long memberId = 1L;
        String email = "test1234@email.com";
        List<String> roles = List.of(RoleEnum.ADMIN.getRoleName());

        // when
        String accessToken = jwtTokenizer.createAccessToken(memberId, email, roles);

        // Then
        Assertions.assertThat(accessToken).isNotBlank();

        // 토큰 파싱하여 클레임 값 확인
        Claims claims = jwtTokenizer.parseAccessToken(accessToken);
        Assertions.assertThat(claims.getSubject()).isEqualTo(email);
        Assertions.assertThat(claims.get("memberId", Long.class)).isEqualTo(memberId);
        Assertions.assertThat(claims.get("roles", List.class)).isEqualTo(roles);
    }

    @Test
    @DisplayName("토큰 생성하기")
    public void createTokenTest() {
        // given
        Long id = 1L;
        String email = "test@example.com";
        List<String> roles = Arrays.asList("ROLE_USER", "ROLE_ADMIN");
        Long expire = 3600000L; // 1 hour
        byte[] secretKey = "0123456789012345678901234567890123456789".getBytes();

        // when
        String token = jwtTokenizer.createToken(id, email, roles, expire, secretKey);

        // then
        Assertions.assertThat(token).isNotBlank();
    }
}