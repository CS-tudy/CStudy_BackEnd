package com.cstudy.moduleapi.controller.competition;

import com.cstudy.moduleapi.application.competition.CompetitionScoreService;
import com.cstudy.moduleapi.dto.competition.CompetitionScoreRequestDto;
import com.cstudy.moduleapi.dto.competition.CompetitionScoreResponseDto;
import com.cstudy.moduleapi.util.IfLogin;
import com.cstudy.modulecommon.util.LoginUserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "CompetitionScore(경기 점수 API)", description = "경기 점수 관련 API")
@Slf4j
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class CompetitionScoreController {

    private final CompetitionScoreService competitionScoreService;

    @Operation(summary = "대회 답안 제출", description = "문제를 다 풀고 답안 제출")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "제출 성공")
    })
    @PostMapping("competition/submit")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void submit(
            @Parameter(description = "competitionId: 대회 id, endTime: 제출한 시간, questions.questionId: 문제 id, questions.choiceNumber: 고른 선택지 번호")
            @RequestBody CompetitionScoreRequestDto requestDto,
            @Parameter(hidden = true)
            @IfLogin LoginUserDto loginUserDto
    ) {
        competitionScoreService.scoring(requestDto, loginUserDto);
    }

    @Operation(summary = "대회 답안 조회", description = "문제를 다 풀고 답안 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("competition/result/{competitionId}")
    @ResponseStatus(HttpStatus.OK)
    public CompetitionScoreResponseDto getScore(
            @Parameter(description = "competitionId: 대회 id")
            @PathVariable("competitionId") Long id,
            @Parameter(hidden = true)
            @IfLogin LoginUserDto loginUserDto
    ) {

        return competitionScoreService.getAnswer(loginUserDto.getMemberId(), id);
    }
}
