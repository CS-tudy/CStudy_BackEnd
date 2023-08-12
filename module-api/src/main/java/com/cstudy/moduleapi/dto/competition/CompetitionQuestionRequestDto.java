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
    @NotNull(message = "문제접 번호를 입력하세요.")
    private Long competitionId;
    @NotEmpty(message = "문제 아이디 리스트를 입력하세요.")
    private List<QuestionIdRequestDto> questionIds;
}
