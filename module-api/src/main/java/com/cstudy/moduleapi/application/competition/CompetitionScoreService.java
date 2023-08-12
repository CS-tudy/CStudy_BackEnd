package com.cstudy.moduleapi.application.competition;


import com.cstudy.moduleapi.dto.competition.CompetitionScoreRequestDto;
import com.cstudy.moduleapi.dto.competition.CompetitionScoreResponseDto;
import com.cstudy.modulecommon.util.LoginUserDto;

public interface CompetitionScoreService {

    void scoring(CompetitionScoreRequestDto requestDto, LoginUserDto userDto);

    CompetitionScoreResponseDto getAnswer(Long memberId, Long competitionId);

    int getScore(Long memberId, Long competitionId);
}
