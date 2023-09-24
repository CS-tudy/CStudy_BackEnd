package com.cstudy.moduleapi.application.reviewNote.impl;

import com.cstudy.moduleapi.application.reviewNote.ReviewService;
import com.cstudy.moduleapi.dto.review.ReviewUserResponseDto;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.reviewQuestion.ReviewNote;
import com.cstudy.modulecommon.domain.reviewQuestion.ReviewUser;
import com.cstudy.modulecommon.error.member.NotFoundMemberId;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import com.cstudy.modulecommon.repository.reviewNote.ReviewNoteRepository;
import com.cstudy.modulecommon.repository.reviewNote.ReviewUserRepository;
import com.cstudy.modulecommon.util.LoginUserDto;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
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

    public reviewServiceImpl(
            ReviewUserRepository userRepository,
            ReviewNoteRepository reviewNoteRepository,
            MemberRepository memberRepository
    ) {
        this.userRepository = userRepository;
        this.reviewNoteRepository = reviewNoteRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    @Transactional
    public void createUserWhenSignupSaveMongodb(String userName) {

        Objects.requireNonNull(userName, "유저 이름이 Null 입니다.");


        userRepository.save(ReviewUser.builder()
                .userName(userName)
                .successQuestion(new LinkedList<>())
                .failQuestion(new LinkedList<>())
                .build());
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

        Member member = memberRepository.findById(loginUserDto.getMemberId())
                .orElseThrow(() -> new NotFoundMemberId(loginUserDto.getMemberId()));

        ReviewUser byName = userRepository.findByUserName(member.getName())
                .orElseThrow(RuntimeException::new);

        boolean questionExistsInFailList = byName.getFailQuestion().stream()
                .anyMatch(failQuestionId -> failQuestionId.equals(String.valueOf(questionId)));


        boolean questionExistsInSuccessList = byName.getSuccessQuestion().stream()
                .anyMatch(successQuestionId -> successQuestionId.equals(String.valueOf(questionId)));

        log.info("questionExistsInFailList : {}", questionExistsInFailList);
        log.info("questionExistsInSuccessList : {}", questionExistsInSuccessList);

        if (questionExistsInSuccessList) {
            List<String> successQuestion = byName.getSuccessQuestion();
            successQuestion.removeIf(successQuestionId -> successQuestionId.equals(String.valueOf(questionId)));
        } else if (questionExistsInFailList) {
            List<String> failQuestion = byName.getFailQuestion();
            failQuestion.removeIf(successQuestionId -> successQuestionId.equals(String.valueOf(questionId)));
        }

        if (questionExistsInFailList || questionExistsInSuccessList) {
            ReviewNote match = byName.getReviewNotes().stream()
                    .filter(reviewNote -> reviewNote.getQuestionId() == questionId)
                    .findFirst().orElseThrow(() -> new RuntimeException("fds"));

            ObjectId objectId = new ObjectId(match.getId());

            log.info("id : {}", objectId);

            reviewNoteRepository.deleteById(objectId.toString());
        }


        log.info("reviewUser_Name : {}", byName.getUserName());

        if (isAnswer) {
            byName.getSuccessQuestion().add(String.valueOf(questionId));
        } else {
            byName.getFailQuestion().add(String.valueOf(questionId));
        }

        userRepository.save(byName);

        if (isAnswer) {
            ReviewNote successNote = ReviewNote.builder()
                    .questionId(questionId)
                    .successChoiceNumber(choiceNumber)
                    .createdDate(now)
                    .isAnswer(true)
                    .build();
            reviewNoteRepository.save(successNote);
            byName.getReviewNotes().add(successNote);
        } else {
            ReviewNote failNote = ReviewNote.builder()
                    .questionId(questionId)
                    .successChoiceNumber(choiceAnswerNumber)
                    .failChoiceNumber(choiceNumber)
                    .createdDate(now)
                    .isAnswer(false)
                    .build();
            reviewNoteRepository.save(failNote);
            byName.getReviewNotes().add(failNote);
        }

        userRepository.save(byName);
    }

    @Override
    @Transactional
    public ReviewUserResponseDto findMongoAboutReviewNote(LoginUserDto loginUserDto) {

        Member member = memberRepository.findById(loginUserDto.getMemberId())
                .orElseThrow(() -> new NotFoundMemberId(loginUserDto.getMemberId()));

        String memberName = member.getName();

        ReviewUser reviewUser = userRepository.findByUserName(memberName).orElseThrow(RuntimeException::new);

        // Create a ReviewUserResponseDto using the static method 'of' from ReviewUserResponseDto class
        return ReviewUserResponseDto.of(reviewUser);
    }
}