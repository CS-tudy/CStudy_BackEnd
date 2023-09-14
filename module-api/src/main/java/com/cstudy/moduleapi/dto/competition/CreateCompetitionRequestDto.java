package com.cstudy.moduleapi.dto.competition;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCompetitionRequestDto {
    @NotNull(message = "경기 제목은 필수 항목입니다.")
    private String competitionTitle;
    @Positive(message = "참가자 수는 양수 항목입니다.")
    private int participants;

    @NotNull(message = "경기 시작 일자는 필수 항목입니다.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime competitionStart;

    @NotNull(message = "경기 마감 일자는 필수 항목입니다.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime competitionEnd;
}
