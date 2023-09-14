package com.cstudy.moduleapi.controller.competition;

import com.cstudy.moduleapi.application.competition.CompetitionScoreService;
import com.cstudy.moduleapi.config.argumentResolver.IfLogin;
import com.cstudy.moduleapi.dto.competition.CompetitionScoreRequestDto;
import com.cstudy.moduleapi.dto.competition.CompetitionScoreResponseDto;
import com.cstudy.modulecommon.error.pathvariable.PositivePatriarchal;
import com.cstudy.modulecommon.util.LoginUserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Tag(name = "CompetitionScore(경기 점수 API)", description = "경기 점수 관련 API")
@Slf4j
@RestController
@RequestMapping("/api/competitions/scores")
public class CompetitionScoreController {

    private final CompetitionScoreService competitionScoreService;

    public CompetitionScoreController(CompetitionScoreService competitionScoreService) {
        this.competitionScoreService = competitionScoreService;
    }

    @Operation(summary = "대회 답안 제출", description = "문제를 다 풀고 답안 제출 / ROLE_CUSTOM', 'ROLE_ADMIN")
    @PostMapping("/submit")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOM', 'ROLE_ADMIN')")
    public void submit(@Parameter(description = "competitionId: 대회 id, endTime: 제출한 시간, questions.questionId: 문제 id, questions.choiceNumber: 고른 선택지 번호")
                       @Valid @RequestBody CompetitionScoreRequestDto requestDto,
                       @Parameter(hidden = true)
                       @IfLogin LoginUserDto loginUserDto) {
        competitionScoreService.scoring(requestDto, loginUserDto);
    }

    @Operation(summary = "대회 답안 조회", description = "문제를 다 풀고 답안 조회 / ROLE_CUSTOM', 'ROLE_ADMIN")
    @GetMapping("/{competitionId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOM', 'ROLE_ADMIN')")
    public CompetitionScoreResponseDto getScore(@Parameter(description = "competitionId: 대회 id", name = "competitionId")
                                                @PathVariable Long competitionId,
                                                @Parameter(hidden = true)
                                                @IfLogin LoginUserDto loginUserDto) {
        Optional.of(competitionId)
                .filter(id -> id >= 0)
                .orElseThrow(() -> new PositivePatriarchal(competitionId));
        return competitionScoreService.getAnswer(loginUserDto.getMemberId(), competitionId);
    }
}
