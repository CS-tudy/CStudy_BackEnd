package com.cstudy.moduleapi.dto.member;

import com.cstudy.modulecommon.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginResponse {

    private String accessToken;
    private String refreshToken;

    private String email;
    private String name;

    public static MemberLoginResponse of(Member member, String accessToken, String refreshToken) {
        return MemberLoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .email(member.getEmail())
                .name(member.getName())
                .build();
    }
}
