package com.cstudy.moduleapi.application.notice;

import com.cstudy.modulecommon.dto.NoticeResponseDto;
import com.cstudy.moduleapi.dto.notice.NoticeSaveRequestDto;
import com.cstudy.modulecommon.dto.NoticeSearchRequestDto;
import com.cstudy.modulecommon.dto.NoticeUpdateRequestDto;
import com.cstudy.modulecommon.util.LoginUserDto;
import org.springframework.data.domain.Page;

public interface NoticeService {
    void saveNotice(NoticeSaveRequestDto noticeSaveRequestDto, LoginUserDto loginUserDto);
    void updateNotice(Long noticeId, NoticeUpdateRequestDto noticeUpdateRequestDto, LoginUserDto loginUserDto);
    void deleteNotice(Long noticeId, LoginUserDto loginUserDto);
    Page<NoticeResponseDto> findNoticePage(int page, int size, NoticeSearchRequestDto requestDto);
}
