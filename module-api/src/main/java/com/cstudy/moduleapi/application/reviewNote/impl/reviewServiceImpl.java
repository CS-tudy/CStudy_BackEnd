package com.cstudy.moduleapi.application.reviewNote.impl;

import com.cstudy.moduleapi.application.reviewNote.ReviewService;
import com.cstudy.moduleapi.dto.review.ReviewUserResponseDto;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.question.Question;
import com.cstudy.modulecommon.domain.reviewQuestion.ReviewNote;
import com.cstudy.modulecommon.domain.reviewQuestion.ReviewUser;
import com.cstudy.modulecommon.error.member.NotFoundMemberId;
import com.cstudy.modulecommon.error.question.NotFoundQuestionId;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import com.cstudy.modulecommon.repository.question.QuestionRepository;
import com.cstudy.modulecommon.repository.reviewNote.ReviewNoteRepository;
import com.cstudy.modulecommon.repository.reviewNote.ReviewUserRepository;
import com.cstudy.modulecommon.util.LoginUserDto;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


@Slf4j
@Service
public class reviewServiceImpl implements ReviewService {


    private final ReviewUserRepository userRepository;
    private final ReviewNoteRepository reviewNoteRepository;
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;


    public reviewServiceImpl(ReviewUserRepository userRepository, ReviewNoteRepository reviewNoteRepository, MemberRepository memberRepository, QuestionRepository questionRepository) {
        this.userRepository = userRepository;
        this.reviewNoteRepository = reviewNoteRepository;
        this.memberRepository = memberRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    @Transactional
    public void createUserWhenSignupSaveMongodb(String userName) {

        Objects.requireNonNull(userName, "유저 이름이 Null 입니다.");

        ReviewUser reviewUser = ReviewUser.builder()
                .userName(userName)
                .successQuestion(new LinkedList<>())
                .failQuestion(new LinkedList<>())
                .build();

        userRepository.save(reviewUser);
    }

    @Override
    @Transactional
    public void solveQuestionWithValid(
            long questionId,
            int choiceNumber,
            boolean isAnswer,
            LoginUserDto loginUserDto,
            Integer choiceAnswerNumber
    ) {


        LocalDateTime now = LocalDateTime.now();

        ReviewUser byName = userRepository.findByUserName(memberRepository.findById(loginUserDto.getMemberId())
                .orElseThrow(() -> new NotFoundMemberId(loginUserDto.getMemberId())).getName())
                .orElseThrow(RuntimeException::new);

        boolean questionExistsInFailList = byName.getFailQuestion().stream()
                .anyMatch(failQuestionId -> failQuestionId.equals(String.valueOf(questionId)));


        boolean questionExistsInSuccessList = byName.getSuccessQuestion().stream()
                .anyMatch(successQuestionId -> successQuestionId.equals(String.valueOf(questionId)));

        divisionListSuccessOrFail(questionId, questionExistsInSuccessList, byName, questionExistsInFailList);

        if (questionExistsInFailList || questionExistsInSuccessList) {
            ReviewNote match = byName.getReviewNotes().stream()
                    .filter(reviewNote -> reviewNote.getQuestionId() == questionId)
                    .findFirst().orElseThrow(() -> new RuntimeException("mongodb"));

            ObjectId objectId = new ObjectId(match.getId());

            reviewNoteRepository.deleteById(objectId.toString());
        }

        updateQuestionStatus(questionId, isAnswer, byName);

        userRepository.save(byName);

        Question question = questionRepository.findByIdFetchJoinCategory(questionId)
                .orElseThrow(() -> new NotFoundQuestionId(questionId));

        saveNote(questionId, choiceNumber, isAnswer, now, question, byName);

        userRepository.save(byName);
    }


    @Override
    @Transactional
    public ReviewUserResponseDto findMongoAboutReviewNote(LoginUserDto loginUserDto) {
        return ReviewUserResponseDto.of(userRepository.findByUserName(memberRepository.findById(loginUserDto.getMemberId())
                .orElseThrow(() -> new NotFoundMemberId(loginUserDto.getMemberId())).getName()).orElseThrow(RuntimeException::new));
    }


    @Override
    @Transactional
    public Page<ReviewUserResponseDto> findReviewPaging(LoginUserDto loginUserDto, Pageable pageable) {
        return userRepository.findByUserName(memberRepository.findById(loginUserDto.getMemberId())
                .orElseThrow(() -> new NotFoundMemberId(loginUserDto.getMemberId())).getName(), pageable).map(ReviewUserResponseDto::of);
    }


    private void saveNote(long questionId, int choiceNumber, boolean isAnswer, LocalDateTime now, Question question, ReviewUser byName) {
        ReviewNote reviewNote = isAnswer ?
                ReviewNote.createNote(questionId, choiceNumber, now, question, true) :
                ReviewNote.createNote(questionId, choiceNumber, now, question, false);
        reviewNoteRepository.save(reviewNote);
        byName.getReviewNotes().add(reviewNote);
    }

    private  void updateQuestionStatus(long questionId, boolean isAnswer, ReviewUser byName) {
        if (isAnswer) {
            byName.getSuccessQuestion().add(String.valueOf(questionId));
        } else {
            byName.getFailQuestion().add(String.valueOf(questionId));
        }
    }

    private  void divisionListSuccessOrFail(long questionId, boolean questionExistsInSuccessList, ReviewUser byName, boolean questionExistsInFailList) {
        if (questionExistsInSuccessList) {
            List<String> successQuestion = byName.getSuccessQuestion();
            successQuestion.removeIf(successQuestionId -> successQuestionId.equals(String.valueOf(questionId)));
        } else if (questionExistsInFailList) {
            List<String> failQuestion = byName.getFailQuestion();
            failQuestion.removeIf(successQuestionId -> successQuestionId.equals(String.valueOf(questionId)));
        }
    }
}