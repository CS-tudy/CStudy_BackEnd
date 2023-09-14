package com.cstudy.moduleapi.dto.competition;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionScoreRequestDto {

    @Positive(message = "경기 아이디는 음수가 불가능 합니다.")
    private Long competitionId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    private List<CompetitionAnswerRequestDto> questions;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompetitionAnswerRequestDto {
        private Integer questionNumber;
        private Long questionId;
        private Integer choiceNumber;
    }
}
