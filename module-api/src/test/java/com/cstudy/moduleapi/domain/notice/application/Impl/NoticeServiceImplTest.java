package com.cstudy.moduleapi.domain.notice.application.Impl;

import com.cstudy.moduleapi.config.ServiceTestBase;
import com.cstudy.moduleapi.dto.member.MemberSignupRequest;
import com.cstudy.moduleapi.dto.notice.NoticeSaveRequestDto;
import com.cstudy.modulecommon.domain.role.RoleEnum;
import com.cstudy.modulecommon.error.member.NotFoundMemberId;
import com.cstudy.modulecommon.util.LoginUserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@Transactional
class NoticeServiceImplTest extends ServiceTestBase {

    @BeforeEach
    void setUp() {
        MemberSignupRequest memberSignupRequest = MemberSignupRequest.builder()
                .email("test1234@test.com")
                .password("test1234!")
                .name("김무건")
                .build();

        memberService.signUpForTest(memberSignupRequest);
    }


    @Nested
    @DisplayName("공지사항 write 에러")
    class firstCreateNoticeAndUpdate {

        @BeforeEach
        void setUp() {
            NoticeSaveRequestDto noticeSaveRequestDto = NoticeSaveRequestDto.builder()
                    .title("제목")
                    .content("내용")
                    .build();

            LoginUserDto loginUserDto = LoginUserDto.builder()
                    .memberId(1L)
                    .roles(List.of(RoleEnum.ADMIN.getRoleName()))
                    .build();


            noticeService.saveNotice(noticeSaveRequestDto, loginUserDto);
        }

        @Test
        @DisplayName("공지사항 삭제 권한 오류")
        public void InvalidNoticeBadRequest() throws Exception {
            //given
            LoginUserDto loginUserDto = LoginUserDto.builder()
                    .memberId(2L)
                    .roles(List.of(RoleEnum.CUSTOM.getRoleName()))
                    .build();
            //when
            //Then
            assertThatThrownBy(() -> noticeService.deleteNotice(1L, loginUserDto))
                    .isInstanceOf(NotFoundMemberId.class)
                    .hasMessage("Not Found Member With:2");
        }

    }

    @Test
    @DisplayName("공지사항 생성하기")
    public void createNoticeWithValid() throws Exception {
        //given
        NoticeSaveRequestDto noticeSaveRequestDto = NoticeSaveRequestDto.builder()
                .title("공지사항 제목")
                .content("공지사항 내용")
                .build();

        LoginUserDto loginUserDto = LoginUserDto.builder()
                .memberId(1L)
                .roles(List.of(RoleEnum.ADMIN.getRoleName()))
                .build();

        //when
        noticeService.saveNotice(noticeSaveRequestDto, loginUserDto);

        //Then
        assertThat(noticeRepository.count()).isNotNull();
    }
}