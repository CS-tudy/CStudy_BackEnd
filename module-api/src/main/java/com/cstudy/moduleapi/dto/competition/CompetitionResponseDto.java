package com.cstudy.moduleapi.dto.competition;

import com.cstudy.modulecommon.domain.competition.Competition;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompetitionResponseDto {

    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;

    private int MaxParticipants;
    private int participants;

    public static CompetitionResponseDto of(Competition competition, int participants) {
        return CompetitionResponseDto.builder()
                .title(competition.getCompetitionTitle())
                .startTime(competition.getCompetitionStart())
                .endTime(competition.getCompetitionEnd())
                .MaxParticipants(competition.getParticipants() + participants)
                .participants(participants)
                .build();
    }

}
