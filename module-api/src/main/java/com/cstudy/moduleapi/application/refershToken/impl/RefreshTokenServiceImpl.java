package com.cstudy.moduleapi.application.refershToken.impl;

import com.cstudy.moduleapi.application.refershToken.RefreshTokenService;
import com.cstudy.moduleapi.config.jwt.util.JwtTokenizer;
import com.cstudy.moduleapi.config.redis.RedisService;
import com.cstudy.moduleapi.dto.member.MemberLoginResponse;
import com.cstudy.moduleapi.dto.refresh.RefreshTokenDto;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.error.member.NotFoundMemberId;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final JwtTokenizer jwtTokenizer;
    private final MemberRepository memberRepository;
    private final RedisService redisService;

    public RefreshTokenServiceImpl(
            JwtTokenizer jwtTokenizer,
            MemberRepository memberRepository,
            RedisService redisService
    ) {
        this.jwtTokenizer = jwtTokenizer;
        this.memberRepository = memberRepository;
        this.redisService = redisService;
    }

    @Override
    @Transactional
    public void addRefreshToken(String refreshToken) {
        redisService.setValues(refreshToken);
    }

    @Override
    @Transactional
    public void deleteRefreshToken(String refreshToken) {
        redisService.delValues(refreshToken);
    }

    /**
     * REFRESH TOKEN이 있으면 ACCESS TOKEN을 재할당을 한다.
     */
    @Override
    public MemberLoginResponse AccessTokenWithRefreshToken(RefreshTokenDto refreshTokenDto) {

        String refreshToken = redisService.getValues(refreshTokenDto.getRefreshToken());

        Claims claims = jwtTokenizer.parseRefreshToken(refreshToken);
        Long userId = Long.valueOf((Integer) claims.get("memberId"));

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new NotFoundMemberId(userId));

        List roles = (List) claims.get("roles");
        String email = claims.getSubject();
        String accessToken = jwtTokenizer.createAccessToken(userId, email, roles);

        return MemberLoginResponse.of(member, accessToken, refreshTokenDto.getRefreshToken());
    }

}
