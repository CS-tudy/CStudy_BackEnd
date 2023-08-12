package com.cstudy.modulecommon.repository.notice;

import com.cstudy.modulecommon.dto.NoticeResponseDto;
import com.cstudy.modulecommon.dto.NoticeSearchRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeCustomRepository {
    Page<NoticeResponseDto> findNoticePage(Pageable pageable, NoticeSearchRequestDto requestDto);
}
