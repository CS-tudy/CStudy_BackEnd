package com.cstudy.moduleapi.controller.ranking;

import com.cstudy.moduleapi.application.ranking.impl.RankingServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Ranking", description = "Redis Cache Aside 전략으로 랭킹보드")

@RestController
@RequestMapping("api")
public class RankingController {

    private final RankingServiceImpl rankingServiceImpl;

    public RankingController(
            RankingServiceImpl rankingServiceImpl
    ) {
        this.rankingServiceImpl = rankingServiceImpl;
    }


    @Operation(summary = "redis cache 랭킹 처리", description = "랭킹 sorted set")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "랭킹 가져오기 성공"),
            @ApiResponse(responseCode = "500", description = "랭킹 가져오기 실패")

    })
    @GetMapping("members/ranks")
    @ResponseStatus(HttpStatus.OK)
    public List<ZSetOperations.TypedTuple<String>> findMemberAllAboutRankingBoard() {
        return rankingServiceImpl.getRanking();
    }
}
