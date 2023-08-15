package com.cstudy.modulecommon.domainEntity.refresh;

import com.cstudy.modulecommon.domain.refresh.RefreshToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RefreshTokenTest {
    @Test
    public void createRefreshToken() {
        RefreshToken refreshToken = RefreshToken.builder()
                .memberId(1L)
                .value("token")
                .build();

        Assertions.assertEquals(1L, refreshToken.getMemberId());
        Assertions.assertEquals("token", refreshToken.getValue());
    }
}