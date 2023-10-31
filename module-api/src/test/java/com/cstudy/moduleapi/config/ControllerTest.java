package com.cstudy.moduleapi.config;

import com.cstudy.moduleapi.apiCaller.*;
import com.cstudy.moduleapi.application.comment.CommentService;
import com.cstudy.moduleapi.application.member.DuplicateServiceFinder;
import com.cstudy.moduleapi.application.member.FileService;
import com.cstudy.moduleapi.application.member.MemberService;
import com.cstudy.moduleapi.application.notice.NoticeService;
import com.cstudy.moduleapi.application.question.MemberQuestionService;
import com.cstudy.moduleapi.application.question.QuestionService;
import com.cstudy.moduleapi.application.ranking.impl.RankingServiceImpl;
import com.cstudy.moduleapi.application.refershToken.RefreshTokenService;
import com.cstudy.moduleapi.application.request.RequestService;
import com.cstudy.moduleapi.application.reviewNote.ReviewService;
import com.cstudy.moduleapi.application.workbook.WorkbookService;
import com.cstudy.moduleapi.config.jwt.util.JwtTokenizer;
import com.cstudy.moduleapi.dto.member.MemberLoginRequest;
import com.cstudy.moduleapi.dto.member.MemberLoginResponse;
import com.cstudy.moduleapi.enums.MemberTestEnum;
import com.cstudy.modulecommon.domain.role.RoleEnum;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class ControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    public JwtTokenizer jwtTokenizer;

    @Autowired
    public MemberRepository memberRepository;

    @MockBean
    public MemberService memberService;

    @MockBean
    public DuplicateServiceFinder duplicateServiceFinder;

    @MockBean
    public RefreshTokenService refreshTokenService;

    @MockBean
    public FileService fileService;

    @MockBean
    public CommentService commentService;

    @MockBean
    public NoticeService noticeService;

    @MockBean
    public RankingServiceImpl rankingService;

    @MockBean
    public QuestionService questionService;

    @MockBean
    public MemberQuestionService memberQuestionService;

    @MockBean
    public ReviewService reviewService;

    @MockBean
    public RequestService requestService;

    @MockBean
    public WorkbookService workbookService;

    public MemberMockApiCaller memberMockApiCaller;
    public CommentMockApiCaller commentMockApiCaller;
    public NoticeMockApiCaller noticeMockApiCaller;
    public RankingMockApiCaller rankingMockApiCaller;
    public QuestionMockApiCaller questionMockApiCaller;
    public ReviewMockApiCaller reviewMockApiCaller;
    public RequestMockApiCaller requestMockApiCaller;
    public WorkbookMockApiCaller workbookMockApiCaller;

    public static String CUSTOM_USER = null;
    public static String ADMIN_USER = null;
    public static String INVALID_TOKEN = null;

    @BeforeEach
    public void setup() throws Exception {
        memberMockApiCaller = new MemberMockApiCaller(mockMvc, objectMapper);
        commentMockApiCaller = new CommentMockApiCaller(mockMvc, objectMapper);
        noticeMockApiCaller = new NoticeMockApiCaller(mockMvc, objectMapper);
        rankingMockApiCaller = new RankingMockApiCaller(mockMvc, objectMapper);
        questionMockApiCaller = new QuestionMockApiCaller(mockMvc, objectMapper);
        reviewMockApiCaller = new ReviewMockApiCaller(mockMvc, objectMapper);
        requestMockApiCaller = new RequestMockApiCaller(mockMvc, objectMapper);
        workbookMockApiCaller = new WorkbookMockApiCaller(mockMvc, objectMapper);

        ADMIN_USER = jwtTokenizer.createAccessToken(1L, MemberTestEnum.ADMIN_EMAIL.getMessage(), List.of(RoleEnum.ADMIN.getRoleName()));
        CUSTOM_USER = jwtTokenizer.createAccessToken(2L, MemberTestEnum.VALID_EMAIL.getMessage(), List.of(RoleEnum.CUSTOM.getRoleName()));
        INVALID_TOKEN = jwtTokenizer.createAccessToken(2L, MemberTestEnum.VALID_EMAIL.getMessage(), List.of("INVALID_ROLE"));

        given(memberService.login(any(MemberLoginRequest.class))).willReturn(MemberLoginResponse.builder()
                .email("test@example.com")
                .accessToken("sampleAccessToken")
                .refreshToken("sampleRefreshToken")
                .name("Test User")
                .build());
    }



}

