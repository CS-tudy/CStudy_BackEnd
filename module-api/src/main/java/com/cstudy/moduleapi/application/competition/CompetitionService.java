package com.cstudy.moduleapi.application.competition;

import com.cstudy.moduleapi.dto.competition.*;
import com.cstudy.modulecommon.domain.competition.CompetitionJoinStatus;
import com.cstudy.modulecommon.dto.CompetitionQuestionDto;
import com.cstudy.modulecommon.util.LoginUserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface CompetitionService {
    Long createCompetition(CreateCompetitionRequestDto createCompetitionRequestDto);

    CompetitionResponseDto getCompetition(Long id);

    CompetitionJoinStatus isJoined(LoginUserDto loginUserDto, Long competitionId);

    Page<CompetitionListResponseDto> getCompetitionList(boolean finish, Pageable pageable, LocalDateTime now);

    Page<CompetitionRankingResponseDto> getCompetitionRanking(Long id, Pageable pageable);

    List<CompetitionQuestionDto> getCompetitionQuestion(Long competitionId, LoginUserDto loginUserDto) throws JsonProcessingException;

    void addCompetitionQuestion(CompetitionQuestionRequestDto requestDto);

    void deleteCompetitionQuestion(CompetitionQuestionRequestDto requestDto);

    void checkCompetitionTime(Long competitionId);
}
