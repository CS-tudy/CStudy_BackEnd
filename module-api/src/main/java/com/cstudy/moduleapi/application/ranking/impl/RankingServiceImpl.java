package com.cstudy.moduleapi.application.ranking.impl;

import com.cstudy.moduleapi.application.ranking.RankingService;
import com.cstudy.moduleapi.config.redis.RedisCacheKey;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.question.MemberQuestion;
import com.cstudy.modulecommon.error.member.NotFoundMemberId;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import com.cstudy.modulecommon.util.LoginUserDto;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class RankingServiceImpl implements RankingService {

    private final static String RANKING_KEY = "MemberRank";

    private final RedisTemplate<String, String> redisTemplate;
    private final MemberRepository memberRepository;


    public RankingServiceImpl(RedisTemplate<String, String> redisTemplate, MemberRepository memberRepository) {
        this.redisTemplate = redisTemplate;
        this.memberRepository = memberRepository;
    }

    /**
     * @return redis에 회원의 정보를 가져와 포인트를 0~10까지 가져온다.
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

    @Override
    @Transactional(readOnly = true)
    public Long getMyRanking(LoginUserDto loginUserDto) {
        return redisTemplate.opsForZSet()
                .reverseRank(RANKING_KEY, memberRepository.findById(loginUserDto.getMemberId())
                        .orElseThrow(() -> new NotFoundMemberId(loginUserDto.getMemberId()))
                        .getName());
    }

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
