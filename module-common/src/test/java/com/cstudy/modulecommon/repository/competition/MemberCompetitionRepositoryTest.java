package com.cstudy.modulecommon.repository.competition;

import com.cstudy.modulecommon.config.QueryDslConfig;
import com.cstudy.modulecommon.domain.competition.Competition;
import com.cstudy.modulecommon.domain.competition.MemberCompetition;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryDslConfig.class})
@Transactional
class MemberCompetitionRepositoryTest {

    @Autowired
    private MemberCompetitionRepository memberCompetitionRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CompetitionRepository competitionRepository;

    @Test
    public void testFindAllWithMemberAndCompetition() {

        memberRepository.save(Member.builder()
                .email("test@example.com")
                .password("test1234!")
                .name("김무건")
                .build());

        LocalDateTime now = LocalDateTime.now();

        Competition competition =Competition.builder()
                .competitionTitle("제목")
                .competitionStart(now)
                .competitionEnd(now.plusHours(1))
                .participants(5)
                .build();

        competitionRepository.save(competition);

        MemberCompetition memberCompetition = MemberCompetition.builder()
                .member(Member.builder()
                        .email("test@example.com")
                        .password("test1234!")
                        .name("김무건")
                        .build())
                .competition(competition)
                .build();

        memberCompetitionRepository.save(memberCompetition);
    }
}