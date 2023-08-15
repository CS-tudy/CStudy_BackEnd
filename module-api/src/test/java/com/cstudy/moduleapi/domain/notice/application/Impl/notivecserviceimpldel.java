package com.cstudy.moduleapi.domain.notice.application.Impl;

import com.cstudy.moduleapi.application.member.MemberService;
import com.cstudy.moduleapi.application.notice.NoticeService;
import com.cstudy.moduleapi.dto.notice.NoticeSaveRequestDto;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.role.RoleEnum;
import com.cstudy.modulecommon.error.member.NotFoundMemberId;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import com.cstudy.modulecommon.repository.notice.NoticeRepository;
import com.cstudy.modulecommon.util.LoginUserDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("local")
public class notivecserviceimpldel {
    @Autowired
    private NoticeService noticeService;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("공지사항 삭제")
    public void deleteNotice() throws Exception {
        //given
        Member member = memberRepository.findById(1L)
                .orElseThrow(() -> new NotFoundMemberId(1L));


        LoginUserDto loginUserDto = LoginUserDto.builder()
                .memberId(1L)
                .roles(List.of(RoleEnum.ADMIN.getRoleName()))
                .build();

        NoticeSaveRequestDto notice2 = NoticeSaveRequestDto.builder()
                .title("공지사항")
                .content("content")
                .build();
        noticeService.saveNotice(notice2,loginUserDto);

        //when
        noticeService.deleteNotice(1L, loginUserDto);
        //Then
        Assertions.assertThat(noticeRepository.count()).isNotEqualTo(1);
    }

}
