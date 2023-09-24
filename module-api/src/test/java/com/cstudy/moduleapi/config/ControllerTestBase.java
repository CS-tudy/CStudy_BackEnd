package com.cstudy.moduleapi.config;

import com.cstudy.moduleapi.application.competition.CompetitionService;
import com.cstudy.moduleapi.application.competition.MemberCompetitionService;
import com.cstudy.moduleapi.application.member.FileService;
import com.cstudy.moduleapi.application.member.MemberService;
import com.cstudy.moduleapi.application.notice.NoticeService;
import com.cstudy.moduleapi.application.question.MemberQuestionService;
import com.cstudy.moduleapi.application.question.QuestionService;
import com.cstudy.moduleapi.application.refershToken.RefreshTokenService;
import com.cstudy.moduleapi.application.request.RequestService;
import com.cstudy.moduleapi.config.jwt.util.JwtTokenizer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
//@ActiveProfiles("local")
@AutoConfigureMockMvc
public abstract class ControllerTestBase {
    @Autowired
    protected MockMvc mockMvc;
    @MockBean
    protected RequestService requestService;

    @MockBean
    protected NoticeService noticeService;

    @MockBean
    protected MemberQuestionService memberQuestionService;

    @MockBean
    protected QuestionService questionService;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected JwtTokenizer jwtTokenizer;

    @MockBean
    protected FileService fileService;

    @MockBean
    protected MemberService memberService;

    @MockBean
    protected RefreshTokenService refreshTokenService;


    @MockBean
    protected CompetitionService competitionService;

    @MockBean
    protected MemberCompetitionService memberCompetitionService;

}
