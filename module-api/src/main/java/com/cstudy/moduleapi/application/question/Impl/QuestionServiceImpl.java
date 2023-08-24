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
     * Create problems that are appropriate for the category.
     * You also create a view of the subsequent problems.
     *
     * @param requestDto the request DTO containing the question and category information
     * @throws NotFoundCategoryTile if the specified category title is not found
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
     * Create a problem that is appropriate for the category.
     * Create a view (list) of the following problems.
     *
     * @param requestDtos 단일 파일이 아닌 다중 문제를 List 형태로 전달
     */
    @Override
    @Transactional
    public void recursiveCreateQuestionChoice(List<CreateQuestionAndCategoryRequestDto> requestDtos) {
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


    @Override
    @Transactional
    public QuestionResponseDto findQuestionWithChoiceAndCategory(Long questionId) {
        return QuestionResponseDto.of(questionRepository.findQuestionWithChoicesAndCategoryById(questionId)
                .orElseThrow(() -> new NotFoundQuestionWithChoicesAndCategoryById(questionId)));
    }

    /**
     * After inquiring about a single question, select the correct answer for the 4-point multiple question.
     * Save the correct or incorrect answers afterwards.
     *
     * @param loginUserDto 로그인 회원의 정보를 저장
     * @param questionId   단일 회원의 질문 아이디
     * @param choiceNumber 문제에 대한 정답을 선택을 한다.
     */
    @Override
    @Transactional
    public void choiceQuestion(LoginUserDto loginUserDto, Long questionId, ChoiceAnswerRequestDto choiceNumber) {

        Integer choiceAnswerNumber = questionRepository.findQuestionWithChoicesAndCategoryById(questionId)
                .orElseThrow(() -> new NotFoundQuestionId(questionId)).getChoices().stream()
                .filter(Choice::isAnswer)
                .map(Choice::getNumber)
                .findFirst().orElseThrow();

        log.warn("정답 info {}",choiceAnswerNumber);


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
        redisPublisher.publish(ChannelTopic.of(RANKING_INVALIDATION), RANKING);
    }


    /**
     * Paging the problem.
     *
     * @param searchCondition Select 조건
     * @param page            페이징 페이지
     * @param size            페이징 사이즈
     * @param loginUserDto
     * @return questionRepository
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