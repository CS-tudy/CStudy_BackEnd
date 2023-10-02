package com.cstudy.moduleapi.application.notice.application.Impl;

import com.cstudy.moduleapi.config.ServiceTestBase;
import com.cstudy.moduleapi.dto.notice.NoticeSaveRequestDto;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.role.RoleEnum;
import com.cstudy.modulecommon.error.member.NotFoundMemberId;
import com.cstudy.modulecommon.util.LoginUserDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class notivecserviceimpldel extends ServiceTestBase {


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
