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
public class MyPageResponseDto {

    private Long id;
    private String name;
    private String email;

    public static MyPageResponseDto of(Member member) {
        return MyPageResponseDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .build();
    }

}
