package com.cstudy.moduleapi.jwt;

import com.cstudy.moduleapi.config.jwt.util.JwtTokenizer;
import com.cstudy.modulecommon.domain.role.RoleEnum;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("local")
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
        assertThat(accessToken).isNotBlank();

        // 토큰 파싱하여 클레임 값 확인
        Claims claims = jwtTokenizer.parseAccessToken(accessToken);
        assertThat(claims.getSubject()).isEqualTo(email);
        assertThat(claims.get("memberId", Long.class)).isEqualTo(memberId);
        assertThat(claims.get("roles", List.class)).isEqualTo(roles);
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
        assertThat(token).isNotBlank();
    }

    @Test
    @DisplayName("트레이드 오프에 따른 Date 토큰 생성하기")
    public void createToken() throws Exception{
        // given
        Long id = 1L;
        String email = "test@example.com";
        List<String> roles = Arrays.asList("ROLE_USER", "ROLE_ADMIN");
        Long expire = 3600000L; // 1 hour
        byte[] secretKey = "0123456789012345678901234567890123456789".getBytes();
        Date date = new Date(1656789000000L);//Tue Jun 02 2022 12:30:00 GMT+0000
        // when
        String token = jwtTokenizer.createTokenWithDate(id, email, roles, expire, secretKey,date);

        //Then
        assertThat(token).isEqualTo("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIiwibWVtYmVySWQiO" +
                "jEsInJvbGVzIjpbIlJPTEVfVVNFUiIsIlJPTEVfQURNSU4iXSwiaWF0IjoxNjU2Nzg5MDAwLCJleHAiOjE2NTY3OTI2MDB9." +
                "l8ZHBTJj7hYS3jYdJHYp6Sd0Xf-igWTzWl74u6x75cU");
    }
}