package com.cstudy.moduleapi.controller.competition;

import com.cstudy.moduleapi.application.competition.CompetitionService;
import com.cstudy.moduleapi.application.competition.MemberCompetitionService;
import com.cstudy.moduleapi.argumentResolver.IfLogin;
import com.cstudy.moduleapi.dto.competition.*;
import com.cstudy.modulecommon.dto.CompetitionQuestionDto;
import com.cstudy.modulecommon.util.LoginUserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.data.web.SortDefault.SortDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Competition(경기 API)", description = "경기 생성 및 Get")
@Slf4j
@RestController
@RequestMapping("/api/competitions")
public class CompetitionController {

    private final CompetitionService competitionService;
    private final MemberCompetitionService memberCompetitionService;

    public CompetitionController(
            CompetitionService competitionService,
            MemberCompetitionService memberCompetitionService
    ) {
        this.competitionService = competitionService;
        this.memberCompetitionService = memberCompetitionService;
    }

    @Operation(summary = "대회 생성하기", description = "대회 생성하기 / ROLE_ADMIN")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public void createCompetition(@Parameter(name = "CreateCompetitionRequestDto", description = "대회 생성 제목, 참가인원, 시작, 끝 시작 ")
                                  @RequestBody CreateCompetitionRequestDto createCompetitionRequestDto) {
        competitionService.createCompetition(createCompetitionRequestDto);
    }

    @Operation(summary = "대회 참여하기", description = "대회 id를 이용해 로그인 한 유저가 해당 대회 참여하기 / ROLE_CUSTOM', 'ROLE_ADMIN ")
    @PostMapping("/{competitionId}/join")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOM', 'ROLE_ADMIN')")
    public void joinCompetitionById(@Parameter(hidden = true)
                                    @IfLogin LoginUserDto loginUserDto,
                                    @Parameter(name = "competitionId", description = "경기 아이디")
                                    @PathVariable(name = "competitionId") Long competitionId) {
        competitionService.checkCompetitionTime(competitionId);
        memberCompetitionService.joinCompetition(loginUserDto, competitionId);
    }

    @Operation(summary = "대회 정보", description = "대회 id를 이용해 대회 정보 조회 / PermitAll")
    @GetMapping("/{competitionId}")
    @ResponseStatus(HttpStatus.OK)
    @PermitAll
    public CompetitionResponseDto getCompetition(@Parameter(name = "competitionId", description = "경기 아이디")
                                                 @PathVariable Long competitionId) {
        return competitionService.getCompetition(competitionId);
    }

    @Operation(summary = "대회 문제 조회", description = "대회 id를 이용해 대회 문제 조회 / ROLE_CUSTOM', 'ROLE_ADMIN")
    @GetMapping("/{competitionId}/questions")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOM', 'ROLE_ADMIN')")
    public List<CompetitionQuestionDto> getCompetitionQuestion(@Parameter(name = "competitionId", description = "경기 아이디")
                                                               @PathVariable Long competitionId,
                                                               @Parameter(hidden = true)
                                                               @IfLogin LoginUserDto loginUserDto) {
        return competitionService.getCompetitionQuestion(competitionId, loginUserDto);
    }

    @Operation(summary = "참여 가능 대회 리스트", description = "참여 가능 대회 리스트 / PermitAll")
    @GetMapping("/active")
    @ResponseStatus(HttpStatus.OK)
    @PermitAll
    public Page<CompetitionListResponseDto> getActiveCompetitions(@Parameter(name = "pageable", description = "경기 시작 시간에 따른 오름차순")
                                                                  @PageableDefault(sort = {"competitionStart"}, direction = Direction.ASC) Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        return competitionService.getCompetitionList(false, pageable, now);
    }

    @Operation(summary = "종료된 대회 리스트", description = "종료된 대회 리스트 / PermitAll")
    @GetMapping("/finished")
    @ResponseStatus(HttpStatus.OK)
    @PermitAll
    public Page<CompetitionListResponseDto> getFinishCompetition(@Parameter(name = "pageable", description = "시작 시간에 따른 내림차순")
                                                                 @PageableDefault(sort = {"competitionStart"}, direction = Direction.DESC) Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        return competitionService.getCompetitionList(true, pageable, now);
    }


    @Operation(summary = "대회 문제 추가", description = "대회 문제 추가 / ROLE_ADMIN")
    @PostMapping("/questions/add")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public void addCompetitionQuestion(@Parameter(name = "CompetitionQuestionRequestDto", description = "대회 문제 추가 정보")
                                       @Valid @RequestBody CompetitionQuestionRequestDto requestDto) {
        competitionService.addCompetitionQuestion(requestDto);
    }


    @Operation(summary = "대회 문제 삭제", description = "대회 문제 삭제 / ROLE_ADMIN")
    @DeleteMapping("/questions/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public void deleteCompetitionQuestion(@Parameter(name = "CompetitionQuestionRequestDto", description = "대회 문제 삭제 정보")
                                          @RequestBody CompetitionQuestionRequestDto requestDto
    ) {
        competitionService.deleteCompetitionQuestion(requestDto);
    }


    @Operation(summary = "대회 랭킹", description = "대회 id를 이용해 대회에 참여한 회원들의 랭킹 조회 / ROLE_CUSTOM', 'ROLE_ADMIN")
    @GetMapping("/{competitionId}/ranks")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOM', 'ROLE_ADMIN')")
    public Page<CompetitionRankingResponseDto> getCompetitionRanking(
            @Parameter(name = "pageable", description = "점수에 따른 내림차순, 끝나는 시간에 따른 오름차순")
            @PageableDefault @SortDefaults({
                    @SortDefault(sort = "score", direction = Direction.DESC),
                    @SortDefault(sort = "endTime", direction = Direction.ASC)}) Pageable pageable,
            @Parameter(name = "competitionId", description = "경기 아이디")
            @PathVariable Long competitionId
    ) {
        return competitionService.getCompetitionRanking(competitionId, pageable);
    }

    @Operation(summary = "대회 내 랭킹", description = "대회에서 내 랭킹 조회 / ROLE_CUSTOM', 'ROLE_ADMIN")
    @GetMapping("/{competitionId}/member/rank")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOM', 'ROLE_ADMIN')")
    public MyCompetitionRankingDto getMyCompetitionRanking(@Parameter(name = "competitionId", description = "경기 아이디")
                                                           @PathVariable Long competitionId,
                                                           @Parameter(hidden = true)
                                                           @IfLogin LoginUserDto loginUserDto) {
        return memberCompetitionService.myRanking(loginUserDto.getMemberId(), competitionId);
    }
}
