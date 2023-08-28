package com.cstudy.moduleapi.domain.application.competition.impl;

import com.cstudy.moduleapi.application.competition.impl.CompetitionServiceImpl;
import com.cstudy.moduleapi.dto.competition.CompetitionListResponseDto;
import com.cstudy.modulecommon.domain.competition.Competition;
import com.cstudy.modulecommon.repository.competition.CompetitionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("local")
@ExtendWith(MockitoExtension.class)
class CompetitionServiceImplTest {

    @InjectMocks
    private CompetitionServiceImpl competitionService;
    @Mock
    private CompetitionRepository competitionRepository;

    @BeforeEach
    void setUp() {

    }

    @Test
    public void testGetCompetitionListFinishTrue() {
        // given
        LocalDateTime now = LocalDateTime.of(2023, 8, 15, 12, 0);
        boolean finish = true;
        Pageable pageable = Pageable.ofSize(10).withPage(0);

        List<Competition> competitionList = new ArrayList<>();

        Competition competition = Competition.builder()
                .competitionStart(now)
                .participants(5)
                .competitionEnd(now.plusHours(1))
                .competitionTitle("제목")
                .build();
        competitionList.add(competition);

        Page<Competition> competitionPage = new PageImpl<>(competitionList);

        //when
        when(competitionRepository.findByCompetitionEndBefore(any(), any())).thenReturn(competitionPage);
        Page<CompetitionListResponseDto> result = competitionService.getCompetitionList(finish, pageable, now);

        //then
        CompetitionListResponseDto responseDto = result.getContent().get(0);
        assertThat(responseDto.getTitle()).isEqualTo("제목");
        assertThat(responseDto.getParticipants()).isEqualTo(5);

        // 여러 다른 필드에 대한 추가적인 검증 추가 가능

        verify(competitionRepository, times(1)).findByCompetitionEndBefore(any(), any());
    }


    @Test
    public void testGetCompetitionListFinishFalse() {
        // given
        LocalDateTime now = LocalDateTime.of(2023, 8, 15, 12, 0);
        boolean finish = false;
        Pageable pageable = Pageable.ofSize(10).withPage(0);

        List<Competition> competitionList = new ArrayList<>();

        Competition competition = Competition.builder()
                .competitionStart(now)
                .participants(5)
                .competitionEnd(now.plusHours(1))
                .competitionTitle("제목")
                .build();

        competitionList.add(competition);

        Page<Competition> competitionPage = new PageImpl<>(competitionList);

        when(competitionRepository.findByCompetitionEndAfter(any(), any())).thenReturn(competitionPage);

        // when
        Page<CompetitionListResponseDto> result = competitionService.getCompetitionList(finish, pageable, now);

        //then
        CompetitionListResponseDto responseDto = result.getContent().get(0);
        assertThat(responseDto.getTitle()).isEqualTo("제목");
        assertThat(responseDto.getParticipants()).isEqualTo(5);

        // 여러 다른 필드에 대한 추가적인 검증 추가 가능

        verify(competitionRepository, times(1)).findByCompetitionEndAfter(any(), any());
    }
}