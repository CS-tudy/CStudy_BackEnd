package com.cstudy.moduleapi.application.competition;

import com.cstudy.moduleapi.dto.competition.*;
import com.cstudy.modulecommon.dto.CompetitionQuestionDto;
import com.cstudy.modulecommon.util.LoginUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CompetitionService {
    Long createCompetition(CreateCompetitionRequestDto createCompetitionRequestDto);

    CompetitionResponseDto getCompetition(Long id);

    Page<CompetitionListResponseDto> getCompetitionList(boolean finish, Pageable pageable);

    Page<CompetitionRankingResponseDto> getCompetitionRanking(Long id, Pageable pageable);

    List<CompetitionQuestionDto> getCompetitionQuestion(Long competitionId, LoginUserDto loginUserDto);

    void addCompetitionQuestion(CompetitionQuestionRequestDto requestDto);
    void deleteCompetitionQuestion(CompetitionQuestionRequestDto requestDto);

    void checkCompetitionTime(Long competitionId);
}
