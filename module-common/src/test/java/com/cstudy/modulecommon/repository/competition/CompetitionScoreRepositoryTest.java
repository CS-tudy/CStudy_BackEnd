package com.cstudy.modulecommon.repository.competition;

import com.cstudy.modulecommon.config.QueryDslConfig;
import com.cstudy.modulecommon.domain.competition.Competition;
import com.cstudy.modulecommon.domain.competition.CompetitionScore;
import com.cstudy.modulecommon.domain.competition.MemberCompetition;
import com.cstudy.modulecommon.domain.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryDslConfig.class})
class CompetitionScoreRepositoryTest {

    @Autowired
    CompetitionScoreRepository competitionScoreRepository;
    @Autowired
    MemberCompetitionRepository memberCompetitionRepository;

    @Test
    public void 대회ID와_회원ID로_찾기() {
        Member member = new Member();
        Competition competition = new Competition();

        MemberCompetition memberCompetition = MemberCompetition.builder()
                .member(member)
                .competition(competition)
                .build();

        CompetitionScore competitionScore =  CompetitionScore.builder()
                .memberCompetition(memberCompetition)
                .build();


        List<CompetitionScore> foundScores = competitionScoreRepository.findByCompetitionIdAndMemberId(member.getId(), competition.getId());

        assertTrue(foundScores.isEmpty());
    }
}