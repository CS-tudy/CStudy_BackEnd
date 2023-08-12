package com.cstudy.moduleapi.dto.competition;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionScoreResponseDto {

    private int score;
    private int total;

    private List<ScoreDetail> details;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScoreDetail {
        Long questionId;
        Integer choiceNumber;
        boolean correct;
    }
}
