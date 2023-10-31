package com.cstudy.moduleapi.application.notice.application.Impl;

import com.cstudy.moduleapi.config.ServiceTestBase;
import com.cstudy.moduleapi.dto.notice.NoticeSaveRequestDto;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.notice.Notice;
import com.cstudy.modulecommon.domain.role.RoleEnum;
import com.cstudy.modulecommon.dto.NoticeUpdateRequestDto;
import com.cstudy.modulecommon.error.member.NotFoundMemberId;
import com.cstudy.modulecommon.util.LoginUserDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class NoticeUpdateServiceImpl extends ServiceTestBase {


    @BeforeEach
    void setUp() {
//        NoticeSaveRequestDto noticeSaveRequestDto = NoticeSaveRequestDto.builder()
//                .title("공지사항 제목")
//                .content("공지사항 내용")
//                .build();
//
//        LoginUserDto loginUserDto = LoginUserDto.builder()
//                .memberId(1L)
//                .roles(List.of(RoleEnum.ADMIN.getRoleName()))
//                .build();
//
//        //when
//        noticeService.saveNotice(noticeSaveRequestDto, loginUserDto);
    }

//    @Test
//    @DisplayName("공지사항 업데이트 실패")
//    public void updateNoticeInvalid() throws Exception {
//        //given
//        Member member = memberRepository.findById(1L).orElseThrow(() -> new NotFoundMemberId(1L));
//        Notice notice2 = Notice.builder()
//                .title("제목")
//                .content("내용")
//                .member(member)
//                .build();
//
//        noticeRepository.save(notice2);
//
//        NoticeUpdateRequestDto noticeUpdateRequestDto = NoticeUpdateRequestDto.builder()
//                .title("변경된 제목")
//                .content("변경된 내용")
//                .build();
//
//        LoginUserDto loginUserDto = LoginUserDto.builder()
//                .memberId(1L)
//                .roles(List.of(RoleEnum.ADMIN.getRoleName()))
//                .build();
//        //when
//        noticeService.updateNotice(1L, noticeUpdateRequestDto, loginUserDto);
//        Notice notice = noticeRepository.findById(1L).orElseThrow();
//        //Then
//        Assertions.assertThat(notice.getTitle()).isEqualTo("변경된 제목");
//        Assertions.assertThat(notice.getContent()).isEqualTo("변경된 내용");
//    }


}
