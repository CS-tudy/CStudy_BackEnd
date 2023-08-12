package com.cstudy.moduleapi.dto.member;

import com.cstudy.modulecommon.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberSignupResponse {
    private String email;
    private String name;

    public static MemberSignupResponse of(Member member) {
        return MemberSignupResponse.builder()
                .email(member.getEmail())
                .name(member.getName())
                .build();
    }
}
