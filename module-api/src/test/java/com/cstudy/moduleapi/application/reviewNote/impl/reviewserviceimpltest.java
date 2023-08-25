package com.cstudy.moduleapi.application.reviewNote.impl;

import com.cstudy.moduleapi.application.reviewNote.ReviewService;
import com.cstudy.modulecommon.domain.reviewQuestion.ReviewUser;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import com.cstudy.modulecommon.repository.reviewNote.ReviewNoteRepository;
import com.cstudy.modulecommon.repository.reviewNote.ReviewUserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("local")
@Transactional
class reviewserviceimpltest {

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ReviewUserRepository userRepository;
    @Autowired
    private ReviewNoteRepository reviewNoteRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("몽고디비 로그인 시 생성")
    public void createUserWhenSignupSaveMongoDB() throws Exception {
        //given
        //when
        reviewService.createUserWhenSignupSaveMongodb("김무건");
        //Then
        assertThat(userRepository.count()).isNotNull();

        ReviewUser user = userRepository.findByUserName("김무건")
                .orElseThrow();
        assertThat(user.getUserName()).isEqualTo("김무건");
    }


    @Test
    @DisplayName("몽고디비 userName Null 오류")
    public void createUserWhenSignupSaveMongoDBWithInvalid() throws Exception{
        //given

        //Then
            assertThatThrownBy(() -> reviewService.createUserWhenSignupSaveMongodb(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("유저 이름이 Null 입니다.");
    }

}