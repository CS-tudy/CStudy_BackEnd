package com.cstudy.moduleapi.application.refershToken;


import com.cstudy.moduleapi.dto.member.MemberLoginResponse;
import com.cstudy.moduleapi.dto.refresh.RefreshTokenDto;

public interface RefreshTokenService {
    void addRefreshToken(String refreshToken);

    void deleteRefreshToken(String refreshToken);

    MemberLoginResponse AccessTokenWithRefreshToken(RefreshTokenDto refreshTokenDto);
}
