package com.cstudy.moduleapi.dto.competition;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCompetitionRequestDto {
    private String competitionTitle;
    private int participants;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime competitionStart;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime competitionEnd;
}
