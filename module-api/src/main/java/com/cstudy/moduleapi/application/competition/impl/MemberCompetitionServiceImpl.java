package com.cstudy.moduleapi.application.competition.impl;

import com.cstudy.moduleapi.application.alarm.AlarmService;
import com.cstudy.moduleapi.application.competition.MemberCompetitionService;
import com.cstudy.moduleapi.dto.competition.MyCompetitionRankingDto;
import com.cstudy.modulecommon.domain.alarm.AlarmArgs;
import com.cstudy.modulecommon.domain.alarm.AlarmType;
import com.cstudy.modulecommon.domain.competition.Competition;
import com.cstudy.modulecommon.domain.competition.MemberCompetition;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.error.competition.DuplicateMemberWithCompetition;
import com.cstudy.modulecommon.error.competition.NotFoundCompetitionId;
import com.cstudy.modulecommon.error.competition.ParticipantsWereInvitedParticipateException;
import com.cstudy.modulecommon.error.member.NotFoundMemberId;
import com.cstudy.modulecommon.repository.competition.CompetitionRepository;
import com.cstudy.modulecommon.repository.competition.MemberCompetitionRepository;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import com.cstudy.modulecommon.util.LoginUserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class MemberCompetitionServiceImpl implements MemberCompetitionService {

    private final static Long ADMIN_ID = 1L;

    private final MemberCompetitionRepository memberCompetitionRepository;
    private final MemberRepository memberRepository;
    private final CompetitionRepository competitionRepository;
    private final AlarmService alarmService;

    public MemberCompetitionServiceImpl(MemberCompetitionRepository memberCompetitionRepository, MemberRepository memberRepository, CompetitionRepository competitionRepository, AlarmService alarmService) {
        this.memberCompetitionRepository = memberCompetitionRepository;
        this.memberRepository = memberRepository;
        this.competitionRepository = competitionRepository;
        this.alarmService = alarmService;
    }

    /**
     * 경기를 참가한다.
     * 일단 대회를 참가하면 MEMBER_COMPETITION에 추가를 한다.
     * 대회의 참가 가능한 인원의 수를 줄인다.
     *
     * 많은 사용자가 동시에 요청을 했을 때 동시성을 막기 위해서 낙관적 락을 선택을 하였다.
     */
    @Override
    @Transactional
    public void joinCompetition(LoginUserDto loginUserDto, Long competitionId) {

        assert loginUserDto != null;

        Optional.of(loginUserDto)
                .filter(dto -> dto.getMemberId() != 1L)
                .ifPresent(dto -> preventionDuplicateParticipation(dto, competitionId));

        Member member = memberRepository.findByIdForUpdateOptimistic(loginUserDto.getMemberId())
                .orElseThrow(() -> new NotFoundMemberId(loginUserDto.getMemberId()));

        Competition competition = competitionRepository.findByIdForUpdateOptimistic(competitionId)
                .orElseThrow(() -> new NotFoundCompetitionId(competitionId));

        decreaseParticipantsCountIfPossible(competition);

        memberCompetitionRepository.save(MemberCompetition.builder()
                .member(member)
                .competition(competition)
                .build());

        Long loginMemberId = loginUserDto.getMemberId();

        alarmService.send(AlarmType.JOIN_COMPETITION, new AlarmArgs(loginMemberId, ADMIN_ID, competition.getCompetitionTitle()), ADMIN_ID);
    }

    /**
     *  경기의 ID를 받아서 참가 가능한 인원을 조회하낟.
     */
    @Override
    @Transactional
    public int getJoinMemberCount(Long competitionId) {
        List<MemberCompetition> memberCompetitions =
                memberCompetitionRepository.findAllWithMemberAndCompetition(competitionId);
        return memberCompetitions.size();
    }

    /**
     *  경기마다 랭킹이 존재한다.
     *  이때 회원의 아이디와 경기의 ID를 기반으로 해당 회원의 랭킹을 조회할 수 있다.
     */
    @Override
    @Transactional
    public MyCompetitionRankingDto myRanking(Long memberId, Long competitionId) {

        List<Long> finishMember = memberCompetitionRepository
                .findFinishMember(competitionId);
        Integer myRank = null;

        for (int i = 0; i < finishMember.size(); i++) {
            if (Objects.equals(finishMember.get(i), memberId)) {
                myRank = i + 1;
                break;
            }
        }

        return new MyCompetitionRankingDto(myRank);
    }

    private void decreaseParticipantsCountIfPossible(Competition competition) {
        if (competition.getParticipants() != 0) {
            competition.decreaseParticipantsCount();
        } else {
            throw new ParticipantsWereInvitedParticipateException();
        }
    }

    private void preventionDuplicateParticipation(LoginUserDto loginUserDto, Long competitionId) {
        boolean isMemberParticipating = memberCompetitionRepository.existsByMemberIdAndCompetitionId(
                loginUserDto.getMemberId(), competitionId);

        checkMemberParticipation(loginUserDto, isMemberParticipating);
    }

    private static void checkMemberParticipation(LoginUserDto loginUserDto, boolean isMemberParticipating) {
        Optional.of(isMemberParticipating)
                .filter(participating -> participating)
                .ifPresent(participating -> {
                    throw new DuplicateMemberWithCompetition(loginUserDto.getMemberId());
                });
    }
}
