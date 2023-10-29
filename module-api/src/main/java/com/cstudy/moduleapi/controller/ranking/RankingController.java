package com.cstudy.moduleapi.controller.ranking;

import com.cstudy.moduleapi.application.ranking.RankingService;
import com.cstudy.moduleapi.config.argumentResolver.IfLogin;
import com.cstudy.modulecommon.util.LoginUserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.util.List;

@Tag(name = "전체 랭킹 처리", description = "Redis Cache Aside 전략으로 랭킹보드")
@RestController
@RequestMapping("/api")
public class RankingController {

    private final RankingService rankingService;

    public RankingController(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @Operation(summary = "redis cache 랭킹 처리", description = "랭킹 sorted set / @PermitAll")
    @GetMapping("/ranks")
    @ResponseStatus(HttpStatus.OK)
    @PermitAll
    public List<ZSetOperations.TypedTuple<String>> findMemberAllAboutRankingBoard() {
        return rankingService.getRanking();
    }

    @Operation(summary = "나의 랭킹 알아보기", description = "token을 이용하여 자신의 랭킹 알아보기")
    @GetMapping("/rank")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOM', 'ROLE_ADMIN')")
    public Long findMyRank(@IfLogin LoginUserDto loginUserDto) {
        return rankingService.getMyRanking(loginUserDto);
    }

    @Operation(summary = "전체 랭킹 limit으로 동적으로 알아보기", description = "int limitRank로 나의 랭킹 알아보기")
    @GetMapping("/ranking")
    @ResponseStatus(HttpStatus.OK)
    @PermitAll
    public List<ZSetOperations.TypedTuple<String>> getMemberRankingWithRange(@RequestParam(defaultValue = "10", required = false) int limitRank) {
        return rankingService.getTopRanking(limitRank);
    }

}
