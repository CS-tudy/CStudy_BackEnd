package com.cstudy.moduleapi.config;

import com.cstudy.moduleapi.application.competition.CompetitionScoreService;
import com.cstudy.moduleapi.application.competition.CompetitionService;
import com.cstudy.moduleapi.application.competition.MemberCompetitionService;
import com.cstudy.moduleapi.application.competition.impl.OptimisticFacade;
import com.cstudy.moduleapi.application.member.MemberService;
import com.cstudy.moduleapi.application.notice.NoticeService;
import com.cstudy.moduleapi.application.question.QuestionService;
import com.cstudy.moduleapi.application.refershToken.RefreshTokenService;
import com.cstudy.moduleapi.application.request.RequestService;
import com.cstudy.moduleapi.application.workbook.WorkbookService;
import com.cstudy.moduleapi.config.redis.RedisService;
import com.cstudy.modulecommon.repository.choice.ChoiceRepository;
import com.cstudy.modulecommon.repository.competition.CompetitionRepository;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import com.cstudy.modulecommon.repository.notice.NoticeRepository;
import com.cstudy.modulecommon.repository.question.MemberQuestionRepository;
import com.cstudy.modulecommon.repository.question.QuestionRepository;
import com.cstudy.modulecommon.repository.refresh.RefreshTokenRepositry;
import com.cstudy.modulecommon.repository.role.RoleRepository;
import com.cstudy.modulecommon.repository.workbook.WorkbookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
public abstract class ServiceTestBase {
    @Autowired
    protected RequestService requestService;

    @Autowired
    protected MemberService memberService;

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected RefreshTokenService refreshTokenService;

    @Autowired
    protected RefreshTokenRepositry refreshTokenRepositry;

    @Autowired
    protected RedisService redisService;

    @Autowired
    protected MemberQuestionRepository memberQuestionRepository;

    @Autowired
    protected RoleRepository roleRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected CompetitionService competitionService;

    @Autowired
    protected CompetitionScoreService competitionScoreService;

    @Autowired
    protected MemberCompetitionService memberCompetitionService;

    @Autowired
    protected QuestionRepository questionRepository;

    @Autowired
    protected WorkbookService workbookService;

    @Autowired
    protected WorkbookRepository workbookRepository;

    @Autowired
    protected ChoiceRepository choiceRepository;

    @Autowired
    protected CompetitionRepository competitionRepository;

    @Autowired
    protected OptimisticFacade facade;

    @Autowired
    protected NoticeService noticeService;

    @Autowired
    protected NoticeRepository noticeRepository;

    @Autowired
    protected QuestionService questionService;


}