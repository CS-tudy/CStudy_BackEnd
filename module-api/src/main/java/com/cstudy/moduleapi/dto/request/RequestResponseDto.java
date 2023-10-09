package com.cstudy.moduleapi.dto.request;

import com.cstudy.modulecommon.domain.request.Request;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestResponseDto {

    private Long id;
    private boolean flag;

    private String title;

    private String description;

    private Long memberId;

    private String memberName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createAt;

    public static RequestResponseDto of(Request request) {
        return RequestResponseDto.builder()
                .id(request.getId())
                .flag(request.isFlag())
                .title(request.getTitle())
                .description(request.getDescription())
                .memberId(request.getMember().getId())
                .memberName(request.getMember().getName())
                .createAt(request.getCreatedAt())
                .build();
    }

    public static RequestResponseDto createRequestDto(Long id, boolean flag, String title, String description, Long memberId, String memberName, LocalDateTime createAt) {
        return new RequestResponseDto(id, flag, title, description, memberId, memberName, createAt);
    }
}
