package com.cstudy.moduleapi.dto.competition;

import com.cstudy.moduleapi.dto.workbook.QuestionIdRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionQuestionRequestDto {
    @NotNull(message = "경기 아이디 입력하세요.")
    private Long competitionId;
    private List<QuestionIdRequestDto> questionIds;
}
