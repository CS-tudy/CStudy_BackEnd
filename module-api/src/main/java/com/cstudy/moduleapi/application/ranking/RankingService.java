package com.cstudy.moduleapi.application.ranking;


import com.cstudy.modulecommon.util.LoginUserDto;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;

public interface RankingService {
    List<ZSetOperations.TypedTuple<String>> getRanking();
    Long getMyRanking(LoginUserDto loginUserDto);
    List<ZSetOperations.TypedTuple<String>> getTopRanking(int limitRank);
}
