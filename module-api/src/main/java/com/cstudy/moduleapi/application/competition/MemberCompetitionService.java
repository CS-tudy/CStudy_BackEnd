package com.cstudy.moduleapi.application.competition;


import com.cstudy.moduleapi.dto.competition.MyCompetitionRankingDto;
import com.cstudy.modulecommon.util.LoginUserDto;

public interface MemberCompetitionService {
     void joinCompetition(LoginUserDto loginUserDto , Long competitionId);

     int getJoinMemberCount(Long competitionId);

     MyCompetitionRankingDto myRanking(Long memberId, Long competitionId);
}
