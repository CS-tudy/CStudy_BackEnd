package com.cstudy.moduleapi.controller.ranking;

import com.cstudy.moduleapi.ApiResponse;
import com.cstudy.moduleapi.config.ControllerTest;
import org.junit.jupiter.api.*;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class RankingControllerTest extends ControllerTest {
    @BeforeEach
    void setUp() throws Exception {
        super.setup();
    }

    @AfterEach
    void cleanUp() {
        super.cleanup();
    }


    @Nested
    @DisplayName("/api/rank 회원의 전체 랭킹 조회하기")
    class Ranking {

        @Test
        public void 회원의_10까지_전체_랭킹을_조회한다_200_성공() throws Exception {
            //given
            List<ZSetOperations.TypedTuple<String>> rankingList = new ArrayList<>();
            rankingList.add(new DefaultTypedTuple<>("유재석", 100.0));
            rankingList.add(new DefaultTypedTuple<>("하하", 95.0));
            rankingList.add(new DefaultTypedTuple<>("송지효", 88.5));
            rankingList.add(new DefaultTypedTuple<>("김종국", 75.0));
            rankingList.add(new DefaultTypedTuple<>("양세찬", 60.5));
            rankingList.add(new DefaultTypedTuple<>("정준하", 50.0));
            rankingList.add(new DefaultTypedTuple<>("박명수", 45.5));
            rankingList.add(new DefaultTypedTuple<>("양세찬", 30.0));
            rankingList.add(new DefaultTypedTuple<>("노홍철", 20.5));
            rankingList.add(new DefaultTypedTuple<>("지석진", 10.0));

            // rankingService.getRanking() 호출 시 rankingList 반환하도록 설정
            when(rankingService.getRanking()).thenReturn(rankingList);
            //when\
            ApiResponse<String> response = rankingMockApiCaller.findMemberAllAboutRankingBoard();

            String expectedResponse =
                    "[{\"score\":100.0,\"value\":\"유재석\"}" +
                            ",{\"score\":95.0,\"value\":\"하하\"}" +
                            ",{\"score\":88.5,\"value\":\"송지효\"}" +
                            ",{\"score\":75.0,\"value\":\"김종국\"}" +
                            ",{\"score\":60.5,\"value\":\"양세찬\"}" +
                            ",{\"score\":50.0,\"value\":\"정준하\"}" +
                            ",{\"score\":45.5,\"value\":\"박명수\"}" +
                            ",{\"score\":30.0,\"value\":\"양세찬\"}" +
                            ",{\"score\":20.5,\"value\":\"노홍철\"}" +
                            ",{\"score\":10.0,\"value\":\"지석진\"}]";

            //Then
            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.getBody()).isEqualTo(expectedResponse);
        }

        @Test
        public void 동일_점수를_가지는_회원_시간에_따른_랭킹_처리() throws Exception {
            //given
            List<ZSetOperations.TypedTuple<String>> rankingList = new ArrayList<>();
            rankingList.add(new DefaultTypedTuple<>("유재석", 100.0));
            rankingList.add(new DefaultTypedTuple<>("하하", 95.35));
            rankingList.add(new DefaultTypedTuple<>("송지효", 95.15));
            rankingList.add(new DefaultTypedTuple<>("김종국", 95.05));
            rankingList.add(new DefaultTypedTuple<>("양세찬", 60.5));
            rankingList.add(new DefaultTypedTuple<>("정준하", 50.0));
            rankingList.add(new DefaultTypedTuple<>("박명수", 45.5));
            rankingList.add(new DefaultTypedTuple<>("양세찬", 30.0));
            rankingList.add(new DefaultTypedTuple<>("노홍철", 20.5));
            rankingList.add(new DefaultTypedTuple<>("지석진", 10.0));
            // rankingService.getRanking() 호출 시 rankingList 반환하도록 설정
            when(rankingService.getRanking()).thenReturn(rankingList);
            //when
            ApiResponse<String> response = rankingMockApiCaller.findMemberAllAboutRankingBoard();

            String expectedResponse =
                            "[{\"score\":100.0,\"value\":\"유재석\"}" +
                            ",{\"score\":95.35,\"value\":\"하하\"}" +
                            ",{\"score\":95.15,\"value\":\"송지효\"}" +
                            ",{\"score\":95.05,\"value\":\"김종국\"}" +
                            ",{\"score\":60.5,\"value\":\"양세찬\"}" +
                            ",{\"score\":50.0,\"value\":\"정준하\"}" +
                            ",{\"score\":45.5,\"value\":\"박명수\"}" +
                            ",{\"score\":30.0,\"value\":\"양세찬\"}" +
                            ",{\"score\":20.5,\"value\":\"노홍철\"}" +
                            ",{\"score\":10.0,\"value\":\"지석진\"}]";

            //Then
            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.getBody()).isEqualTo(expectedResponse);
        }


    }
}