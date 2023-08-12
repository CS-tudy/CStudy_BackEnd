package com.cstudy.moduleapi.dto.competition;

import com.cstudy.modulecommon.domain.competition.MemberCompetition;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionRankingResponseDto {

    private String name;
    private Long memberId;
    private Integer score;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    public static CompetitionRankingResponseDto of(MemberCompetition memberCompetition){
        return CompetitionRankingResponseDto.builder()
                .name(memberCompetition.getMember().getName())
                .memberId(memberCompetition.getMember().getId())
                .score(memberCompetition.getScore())
                .endTime(memberCompetition.getEndTime())
                .build();
    }
}
