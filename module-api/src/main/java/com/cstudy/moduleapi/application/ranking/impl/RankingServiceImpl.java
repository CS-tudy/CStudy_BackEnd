package com.cstudy.moduleapi.application.ranking.impl;

import com.cstudy.moduleapi.application.member.MemberLoadComponent;
import com.cstudy.moduleapi.application.ranking.RankingService;
import com.cstudy.moduleapi.config.redis.RedisCacheKey;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.question.MemberQuestion;
import com.cstudy.modulecommon.error.member.NotFoundMemberId;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import com.cstudy.modulecommon.util.LoginUserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RankingServiceImpl implements RankingService {

    private final static String RANKING_KEY = "MemberRank";

    private final RedisTemplate<String, String> redisTemplate;
    private final MemberRepository memberRepository;
    private final MemberLoadComponent memberLoadComponent;


    public RankingServiceImpl(
            RedisTemplate<String, String> redisTemplate,
            MemberRepository memberRepository,
            MemberLoadComponent memberLoadComponent
    ) {
        this.redisTemplate = redisTemplate;
        this.memberRepository = memberRepository;
        this.memberLoadComponent = memberLoadComponent;
    }

    /**
     * redis에 회원의 정보를 가져와 포인트를 0~10까지 가져온다.
     */
    @Cacheable(key = "1", value = RedisCacheKey.Ranking, cacheManager = "redisCacheManager")
    @Transactional(readOnly = true)
    public List<ZSetOperations.TypedTuple<String>> getRanking() {

        List<Member> memberList = memberRepository.findAllWithQuestions();

        ZSetOperations<String, String> stringStringZSetOperations = redisTemplate.opsForZSet();

        memberList.forEach(member -> {
            double rankingPoint = member.getRankingPoint();
            stringStringZSetOperations.add("ranking", member.getName(), rankingPoint);
        });

        return new ArrayList<>(Objects.requireNonNull(stringStringZSetOperations.reverseRangeWithScores("ranking", 0, 9), "Ranking Board Data null"));
    }

    /**
     * 랭킹에서 자신의 랭킹을 조회한다.
     */
    @Override
    @Transactional(readOnly = true)
    public Long getMyRanking(LoginUserDto loginUserDto) {
        List<Member> allMembers = memberRepository.findAll();

        // 현재 로그인한 사용자 정보 가져오기
        Member currentMember = memberRepository.findById(loginUserDto.getMemberId())
                .orElseThrow(() -> new NotFoundMemberId(loginUserDto.getMemberId()));
        log.info("회원의 랭킹 포인트 : {}", currentMember.getRankingPoint());


        // rankingPoint를 기준으로 내림차순 정렬
        List<Member> sortedMembers = allMembers.stream()
                .sorted((m1, m2) -> Double.compare(m2.getRankingPoint(), m1.getRankingPoint()))
                .collect(Collectors.toList());
        // 내 랭킹 찾기
        long myRanking = (sortedMembers.indexOf(currentMember) + 1);
        log.info("현재 나의 랭킹 : {}", myRanking);

        return myRanking;
//        return redisTemplate.opsForZSet()
//                .reverseRank(RANKING_KEY, memberLoadComponent.loadMemberByEmail(loginUserDto.getMemberEmail())
//                        .getName());
    }

    /**
     * 랭킹의 범위를 선정하여 동적으로 랭킹 보드를 조작할 수 있다.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ZSetOperations.TypedTuple<String>> getTopRanking(int limitRank) {
        return new ArrayList<>(Objects.requireNonNull(redisTemplate.opsForZSet().reverseRangeWithScores(RANKING_KEY, 0, limitRank)));
    }

    private long calculateSolveTime(Member member) {
        return member.getQuestions().stream()
                .mapToLong(MemberQuestion::getSolveTime)
                .sum();
    }
}
