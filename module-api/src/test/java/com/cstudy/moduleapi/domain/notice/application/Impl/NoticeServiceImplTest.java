package com.cstudy.moduleapi.domain.notice.application.Impl;

import com.cstudy.moduleapi.application.member.MemberService;
import com.cstudy.moduleapi.application.notice.NoticeService;
import com.cstudy.moduleapi.dto.member.MemberSignupRequest;
import com.cstudy.moduleapi.dto.notice.NoticeSaveRequestDto;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.notice.Notice;
import com.cstudy.modulecommon.domain.role.RoleEnum;
import com.cstudy.modulecommon.dto.NoticeUpdateRequestDto;
import com.cstudy.modulecommon.error.member.NotFoundMemberId;
import com.cstudy.modulecommon.error.notice.NotMatchAdminIpException;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import com.cstudy.modulecommon.repository.notice.NoticeRepository;
import com.cstudy.modulecommon.util.LoginUserDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
@ActiveProfiles("test")
class NoticeServiceImplTest {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        MemberSignupRequest memberSignupRequest = MemberSignupRequest.builder()
                .email("test1234@test.com")
                .password("test1234!")
                .name("김무건")
                .build();

        memberService.signUp(memberSignupRequest);
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
        Notice notice = noticeRepository.findById(1L).orElseThrow();
        assertThat(notice.getTitle()).isEqualTo("공지사항 제목");
        assertThat(notice.getContent()).isEqualTo("공지사항 내용");
        assertThat(noticeRepository.count()).isNotEqualTo(0);
    }

    @Test
    @Transactional
    @DisplayName("공지사항 업데이트")
    public void updateNotice() throws Exception {
        //given
        Member member = memberRepository.findById(1L).orElseThrow(() -> new NotFoundMemberId(1L));
        Notice notice2 = Notice.builder()
                .title("제목")
                .content("내용")
                .member(member)
                .build();

        noticeRepository.save(notice2);

        NoticeUpdateRequestDto noticeUpdateRequestDto = NoticeUpdateRequestDto.builder()
                .title("변경된 제목")
                .content("변경된 내용")
                .build();

        LoginUserDto loginUserDto = LoginUserDto.builder()
                .memberId(1L)
                .roles(List.of(RoleEnum.ADMIN.getRoleName()))
                .build();
        //when
        noticeService.updateNotice(1L, noticeUpdateRequestDto, loginUserDto);
        Notice notice = noticeRepository.findById(1L).orElseThrow();
        //Then
        Assertions.assertThat(notice.getTitle()).isEqualTo("변경된 제목");
        Assertions.assertThat(notice.getContent()).isEqualTo("변경된 내용");
    }

    @Test
    @DisplayName("공지사항 삭제")
    public void deleteNotice() throws Exception {
        //given
        Member member = memberRepository.findById(1L).orElseThrow(() -> new NotFoundMemberId(1L));
        Notice notice2 = Notice.builder()
                .title("제목")
                .content("내용")
                .member(member)
                .build();

        noticeRepository.save(notice2);


        LoginUserDto loginUserDto = LoginUserDto.builder()
                .memberId(1L)
                .roles(List.of(RoleEnum.ADMIN.getRoleName()))
                .build();
        //when
        noticeService.deleteNotice(1L, loginUserDto);
        //Then
        Assertions.assertThat(noticeRepository.count()).isNotEqualTo(1);
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
                    .roles(List.of(RoleEnum.ADMIN.getRoleName()))
                    .build();
            //when
            //Then
            assertThatThrownBy(() -> noticeService.deleteNotice(1L, loginUserDto))
                    .isInstanceOf(NotMatchAdminIpException.class)
                    .hasMessage("Not Equals Admin ID: 1");
        }


    }
}