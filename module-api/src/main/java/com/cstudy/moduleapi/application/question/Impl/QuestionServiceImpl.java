package com.cstudy.moduleapi.application.question.Impl;

import com.cstudy.moduleapi.application.question.MemberQuestionService;
import com.cstudy.moduleapi.application.question.QuestionService;
import com.cstudy.moduleapi.application.reviewNote.ReviewService;
import com.cstudy.moduleapi.config.redis.RedisPublisher;
import com.cstudy.moduleapi.dto.choice.CreateChoicesAboutQuestionDto;
import com.cstudy.moduleapi.dto.question.CreateQuestionAndCategoryRequestDto;
import com.cstudy.moduleapi.dto.question.CreateQuestionRequestDto;
import com.cstudy.moduleapi.dto.question.QuestionResponseDto;
import com.cstudy.modulecommon.domain.choice.Choice;
import com.cstudy.modulecommon.domain.question.Category;
import com.cstudy.modulecommon.domain.question.Question;
import com.cstudy.modulecommon.dto.ChoiceAnswerRequestDto;
import com.cstudy.modulecommon.dto.QuestionPageWithCategoryAndTitle;
import com.cstudy.modulecommon.dto.QuestionSearchCondition;
import com.cstudy.modulecommon.error.category.NotFoundCategoryTile;
import com.cstudy.modulecommon.error.question.NotFoundQuestionId;
import com.cstudy.modulecommon.error.question.NotFoundQuestionWithChoicesAndCategoryById;
import com.cstudy.modulecommon.repository.choice.ChoiceRepository;
import com.cstudy.modulecommon.repository.question.CategoryRepository;
import com.cstudy.modulecommon.repository.question.QuestionRepository;
import com.cstudy.modulecommon.util.LoginUserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository;
    private final ChoiceRepository choiceRepository;
    private final MemberQuestionService memberQuestionService;
    private final RedisPublisher redisPublisher;
    private final JdbcTemplate jdbcTemplate;
    private final ReviewService reviewService;

    public QuestionServiceImpl(
            QuestionRepository questionRepository,
            CategoryRepository categoryRepository,
            ChoiceRepository choiceRepository,
            MemberQuestionService memberQuestionService,
            RedisPublisher redisPublisher,
            JdbcTemplate jdbcTemplate,
            ReviewService reviewService
    ) {
        this.questionRepository = questionRepository;
        this.categoryRepository = categoryRepository;
        this.choiceRepository = choiceRepository;
        this.memberQuestionService = memberQuestionService;
        this.redisPublisher = redisPublisher;
        this.jdbcTemplate = jdbcTemplate;
        this.reviewService = reviewService;
    }

    private static final String COLLECT_ANSWER = "정답";
    private static final String RANKING_INVALIDATION ="ranking-invalidation";
    private static final String RANKING ="ranking";

    /**
     * 문제를 생성한다.
     * 1. 이때 문제를 생성할 때 카테고리의 정보를 조회하여 같은 카테고리에서 선택한다.
     * 2. 문제를 REQUEST에 대하여 문제를 생성한다.
     * 3. 문제와 카테고리의 데이터를 기반으로 보기를 선택한다. 이때 보기는 4지선다로 선정한다.
     *
     * {
     *  	 "createQuestionRequestDto": {
     *  	   "questionTitle": "문제 제목",
     *  	   "questionDesc": "문제 설명",
     *  	   "questionExplain": "문제 정답 설명"
     *          },
     *  	 "categoryRequestDto": {
     *  	   "category": "운영체제"
     *     },
     *  	 "createChoicesAboutQuestionDto": [
     *       {
     *  	     "number": 1,
     *  	     "content": "보기1"
     *       },
     *       {
     *  	     "number": 2,
     *  	     "content": "보기2"
     *       },
     *       {
     *  	     "number": 3,
     *  	     "content": "보기3",
     *  	     "answer":"정답"
     *       },
     *       {
     *  	     "number": 4,
     *  	     "content": "보기4"
     *       }
     *  	 ]
     * }
     */
    @Override
    @Transactional
    public void createQuestionChoice(CreateQuestionAndCategoryRequestDto requestDto) {

        List<Choice> choices = new ArrayList<>();

        String findCategoryTitle = requestDto.getCategoryRequestDto().getCategory();

        Category category = categoryRepository.findByTitle(findCategoryTitle)
                .orElseThrow(() -> new NotFoundCategoryTile(findCategoryTitle));

        Question question = createQuestion(requestDto.getCreateQuestionRequestDto(), category);

        for (CreateChoicesAboutQuestionDto choiceDto : requestDto.getCreateChoicesAboutQuestionDto()) {
            boolean answer = isCollectAnswer(choiceDto.getAnswer());

            Choice choice = createChoice(choiceDto, question, answer);

            choices.add(choice);
        }

        question.setChoices(choices);
        questionRepository.save(question);
        choiceRepository.saveAll(new ArrayList<>(choices));
    }

    /**
     * 대량의 문제를 생성하기를 하였을 때 단일 문제를 반복하여 호출을 하였을 때 성능적으로 큰 이슈가 발생 10,000건의 데이터를 기준으로 약 1500초
     * 이걸 BATCH SIZE로 문제를 조회했다.
     * JDBCTEMPLATE을 사용하여 BATCH SIZE를 선택한 이유는 키 전략 패턴이 IDENTIFY여서 HIBERNATE에서 BATCH INSERT는 비활성화 된다.
     * 영속화 하지 않는 식별자 값을 미리 알 수 없기 때문에 BATCH INSERT를 진행하면 FLUSH의 트랜잭션이 WRITE BEHIND와 충돌이 발생하기 때문에
     * 정상적으로 동작하지 않는다.
     */
    @Override
    @Transactional
    public void bulkCreateQuestionChoice(List<CreateQuestionAndCategoryRequestDto> requestDtos) {
        String questionSql = "" +
                "INSERT INTO question" +
                " (category_id," +
                " question_description," +
                " question_explain," +
                " question_title) " +
                "VALUES (?, ?, ?, ?)";

        String choiceSql = "" +
                "INSERT INTO choice" +
                " (answer," +
                " content," +
                " choice_number," +
                " question_id) " +
                "VALUES (?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(questionSql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(@NotNull PreparedStatement preparedStatement, int i) throws SQLException {
                CreateQuestionAndCategoryRequestDto questionDto = requestDtos.get(i);

                Long categoryId = getCategoryIdByTitle(questionDto.getCategoryRequestDto().getCategory());

                preparedStatement.setLong(1, categoryId);
                preparedStatement.setString(2, questionDto.getCreateQuestionRequestDto().getQuestionDesc());
                preparedStatement.setString(3, questionDto.getCreateQuestionRequestDto().getQuestionExplain());
                preparedStatement.setString(4, questionDto.getCreateQuestionRequestDto().getQuestionTitle());
            }

            @Override
            public int getBatchSize() {
                return requestDtos.size();
            }
        });


        for (CreateQuestionAndCategoryRequestDto questionDto : requestDtos) {
            Long questionId = getQuestionIdByTitle(questionDto.getCreateQuestionRequestDto().getQuestionTitle());

            List<CreateChoicesAboutQuestionDto> choiceDtos = questionDto.getCreateChoicesAboutQuestionDto();

            jdbcTemplate.batchUpdate(choiceSql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@NotNull PreparedStatement preparedStatement, int i) throws SQLException {
                    CreateChoicesAboutQuestionDto choiceDto = choiceDtos.get(i);
                    boolean answer = isCollectAnswer(choiceDto.getAnswer());

                    preparedStatement.setBoolean(1, answer);
                    preparedStatement.setString(2, choiceDto.getContent());
                    preparedStatement.setInt(3, choiceDto.getNumber());
                    preparedStatement.setLong(4, questionId);
                }

                @Override
                public int getBatchSize() {
                    return choiceDtos.size();
                }
            });
        }
    }

    private Long getCategoryIdByTitle(String categoryTitle) {
        String sql = "SELECT category_id" +
                     " FROM category " +
                     "WHERE category_title = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, categoryTitle);
    }

    private Long getQuestionIdByTitle(String questionTitle) {
        String sql = "SELECT question_id " +
                     "FROM question " +
                     "WHERE question_title = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, questionTitle);
    }


    /**
     *  문제에 대한 카테고리를 조회한다.
     */
    @Override
    @Transactional
    public QuestionResponseDto findQuestionWithChoiceAndCategory(Long questionId) {
        return QuestionResponseDto.of(questionRepository.findQuestionWithChoicesAndCategoryById(questionId)
                .orElseThrow(() -> new NotFoundQuestionWithChoicesAndCategoryById(questionId)));
    }

    /**
     * 문제에 대한 아이디를 기반으로 회원이 정답을 선택한다.
     *
     * 현재 MySQL 부분에서 문제에 대한 정답 및 오답을 처리한다.
     * 만약에 문제가 성공이면 MEMBER_QUESTION 테이블에 SUCCESS 처리를 하며
     * 이후 만약에 실패가 발생되면 FAIL을 처리를 한다.
     * -> 현재 이 부분을 MongoDB로 분리를 시킬려고 한다.
     * -> 현재 테스트 코드를 통해서 사이드 이펙트를 체크하면 다음과 같다.
     * 1. 오답노트
     * 2. 전체 문제 페이징 부분에서 STATUS 부분을 추가적으로 작성
     *
     *  MONGODB 부분에서 문제에 대한 정답 및 오답을 처리한다.
     */
    @Transactional
    public void choiceQuestion(LoginUserDto loginUserDto, Long questionId, ChoiceAnswerRequestDto choiceNumber) {

        log.info("questionId :  {}", questionId);
        Integer choiceAnswerNumber = questionRepository.findQuestionWithChoicesAndCategoryById(questionId)
                .orElseThrow(() -> new NotFoundQuestionId(questionId)).getChoices().stream()
                .filter(Choice::isAnswer)
                .map(Choice::getNumber)
                .findFirst().orElseThrow();

        log.warn("정답 info {}",choiceAnswerNumber);


        //현재 MySQL 부분에서 문제에 대한 정답 및 오답을 처리한다.
        questionRepository.findQuestionWithChoicesAndCategoryById(questionId)
                .orElseThrow(() -> new NotFoundQuestionId(questionId)).getChoices().stream()
                .filter(Choice::isAnswer)
                .forEach(choice -> {
                    if (choice.getNumber() == choiceNumber.getChoiceNumber()) {
                        memberQuestionService.findMemberAndMemberQuestionSuccess(
                                loginUserDto.getMemberId(),
                                questionId,
                                choiceNumber
                        );
                    } else {
                        memberQuestionService.findMemberAndMemberQuestionFail(
                                loginUserDto.getMemberId(),
                                questionId,
                                choiceNumber
                        );
                    }
                });

         // MONGODB 부분에서 문제에 대한 정답 및 오답을 처리한다.
        questionRepository.findQuestionWithChoicesAndCategoryById(questionId)
                .orElseThrow(() -> new NotFoundQuestionId(questionId)).getChoices().stream()
                .filter(Choice::isAnswer)
                .forEach(choice -> {
                    if (choice.getNumber() == choiceNumber.getChoiceNumber()) {
                        reviewService.solveQuestionWithValid(
                                questionId,
                                choiceNumber.getChoiceNumber(),
                                true,
                                loginUserDto,
                                choiceAnswerNumber);
                    } else {
                        reviewService.solveQuestionWithValid(
                                questionId,
                                choiceNumber.getChoiceNumber(),
                                false,
                                loginUserDto,
                                choiceAnswerNumber
                        );
                    }

                });

        /**
         * 분산환경에서 REDIS 캐싱을 맞출려면 아마도 동기화 문제와 캐싱 오버헤드가 발생한다.
         * 이를 해결하기 위해서 현재는 PUBSUB으로 문제를 해결

         TODO : 이후 이 부분을 캐싱 오버헤드를 생각하면 WRITE BACK 전략으로 변경을 하거나 SSE 방식으로 변경을 생각을 해보자
         */
        redisPublisher.publish(ChannelTopic.of(RANKING_INVALIDATION), RANKING);
    }


    /**
     * 전체 문제를 조회하는 로직
     * 이때 페이징을 처리하여 문제를 처리한다.
     * 여기서 PAGE, SIZE를 DEFAULT로 설정이 되어져 있다.
     * 여기서 SEARCH 조건에 따라서 필터링이 가능하다.
     *
     */
    @Override
    @Transactional(readOnly = true)
    public Page<QuestionPageWithCategoryAndTitle> questionPageWithCategory(
            QuestionSearchCondition searchCondition,
            int page,
            int size,
            LoginUserDto loginUserDto
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return questionRepository.findQuestionPageWithCategory(pageable, searchCondition, loginUserDto);
    }

    private Question createQuestion(
            CreateQuestionRequestDto questionDto,
            Category category
    ) {
        return Question.builder()
                .title(questionDto.getQuestionTitle())
                .description(questionDto.getQuestionDesc())
                .explain(questionDto.getQuestionExplain())
                .category(category)
                .build();
    }

    private boolean isCollectAnswer(String answer) {
        return COLLECT_ANSWER.equals(answer);
    }

    private Choice createChoice(
            CreateChoicesAboutQuestionDto choiceDto,
            Question question,
            boolean isAnswer
    ) {
        return Choice.builder()
                .number(choiceDto.getNumber())
                .content(choiceDto.getContent())
                .question(question)
                .answer(isAnswer)
                .build();
    }
}