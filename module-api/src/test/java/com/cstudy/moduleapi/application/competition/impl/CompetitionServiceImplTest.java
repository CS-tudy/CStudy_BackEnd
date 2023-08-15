package com.cstudy.moduleapi.application.competition.impl;

import com.cstudy.moduleapi.application.competition.CompetitionService;
import com.cstudy.moduleapi.application.competition.MemberCompetitionService;
import com.cstudy.moduleapi.application.workbook.WorkbookService;
import com.cstudy.moduleapi.dto.competition.CompetitionListResponseDto;
import com.cstudy.modulecommon.repository.competition.CompetitionRepository;
import com.cstudy.modulecommon.repository.competition.MemberCompetitionRepository;
import com.cstudy.modulecommon.repository.question.QuestionRepository;
import com.cstudy.modulecommon.repository.workbook.WorkbookRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;

@ActiveProfiles("local")
@ExtendWith(MockitoExtension.class)
class CompetitionServiceImplTest {

    @InjectMocks
    private CompetitionServiceImpl competitionService;
    @Mock
    private CompetitionRepository competitionRepository;
    @Mock
    private WorkbookRepository workbookRepository;
    @Mock
    private QuestionRepository questionRepository;
    @Mock
    private MemberCompetitionRepository memberCompetitionRepository;
    @Mock
    private MemberCompetitionService memberCompetitionService;
    @Mock
    private WorkbookService workbookService;

    @BeforeEach
    void setUp(){

    }

    @Test
    @DisplayName("test")
    public void test() throws Exception{
        //given
        LocalDateTime now = LocalDateTime.now();
        PageRequest pageRequest = PageRequest.of(0, 10);
        given(competitionRepository.findByCompetitionEndBefore(now,pageRequest)).willReturn(null);
        //when
        Page<CompetitionListResponseDto> competitionList = competitionService.getCompetitionList(true, pageRequest, now);
        //Then
        Assertions.assertThat(competitionList).isNull();
    }
}