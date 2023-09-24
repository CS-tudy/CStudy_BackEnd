package com.cstudy.moduleapi.application.competition.impl;

import com.cstudy.moduleapi.application.competition.MemberCompetitionService;
import com.cstudy.moduleapi.dto.competition.MyCompetitionRankingDto;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberCompetitionServiceImpl implements MemberCompetitionService {

    private final MemberCompetitionRepository memberCompetitionRepository;
    private final MemberRepository memberRepository;
    private final CompetitionRepository competitionRepository;

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

        MemberCompetition memberCompetition = MemberCompetition.builder()
                .member(member)
                .competition(competition)
                .build();

        memberCompetitionRepository.save(memberCompetition);
    }

    @Override
    @Transactional
    public int getJoinMemberCount(Long competitionId) {
        List<MemberCompetition> memberCompetitions =
                memberCompetitionRepository.findAllWithMemberAndCompetition(competitionId);
        return memberCompetitions.size();
    }

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
