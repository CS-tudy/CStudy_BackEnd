package com.cstudy.moduleapi.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DuplicateResponseDto {
    private String verify;

    public static DuplicateResponseDto of(String verify) {
        return DuplicateResponseDto.builder()
                .verify(verify)
                .build();
    }
}
