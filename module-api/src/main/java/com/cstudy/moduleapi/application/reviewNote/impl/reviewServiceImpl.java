package com.cstudy.moduleapi.application.reviewNote.impl;

import com.cstudy.moduleapi.application.reviewNote.ReviewService;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.reviewQuestion.ReviewNote;
import com.cstudy.modulecommon.domain.reviewQuestion.ReviewUser;
import com.cstudy.modulecommon.error.member.NotFoundMemberId;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import com.cstudy.modulecommon.repository.reviewNote.ReviewNoteRepository;
import com.cstudy.modulecommon.repository.reviewNote.ReviewUserRepository;
import com.cstudy.modulecommon.util.LoginUserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedList;

import static com.cstudy.modulecommon.domain.reviewQuestion.ReviewNote.createFailNote;

@Slf4j
@Service
public class reviewServiceImpl implements ReviewService {


    private final ReviewUserRepository userRepository;
    private final ReviewNoteRepository reviewNoteRepository;
    private final MemberRepository memberRepository;

    public reviewServiceImpl(ReviewUserRepository userRepository, ReviewNoteRepository reviewNoteRepository, MemberRepository memberRepository) {
        this.userRepository = userRepository;
        this.reviewNoteRepository = reviewNoteRepository;
        this.memberRepository = memberRepository;
    }

    /**
     * 회원가입을 하였을 때 몽고디비에 기본적인 세팅을 만든다.
     *
     * @param userName 회원 이름
     */
    @Override
    @Transactional
    public void createUserWhenSignupSaveMongodb(String userName) {
        userRepository.save(ReviewUser.builder()
                .userName(userName)
                .successQuestion(new LinkedList<>())
                .failQuestion(new LinkedList<>())
                .build());
    }

    @Override
    @Transactional
    public void solveQuestionWithValid(long questionId, int choiceNumber, boolean isAnswer, LoginUserDto loginUserDto) {
        LocalDateTime now = LocalDateTime.now();

        Member member = memberRepository.findById(loginUserDto.getMemberId())
                .orElseThrow(() -> new NotFoundMemberId(loginUserDto.getMemberId()));

        ReviewUser byName = userRepository.findByUserName(member.getName())
                .orElseThrow(RuntimeException::new);

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
            ReviewNote failNote = createFailNote(now, questionId, false, choiceNumber);
            reviewNoteRepository.save(failNote);
            byName.getReviewNotes().add(failNote);
        }

        userRepository.save(byName);
    }


}
