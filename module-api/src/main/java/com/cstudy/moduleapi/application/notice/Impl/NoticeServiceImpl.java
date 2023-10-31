package com.cstudy.moduleapi.application.notice.Impl;

import com.cstudy.moduleapi.application.member.MemberLoadComponent;
import com.cstudy.moduleapi.application.notice.NoticeService;
import com.cstudy.moduleapi.dto.notice.NoticeSaveRequestDto;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.notice.Notice;
import com.cstudy.modulecommon.dto.NoticeResponseDto;
import com.cstudy.modulecommon.dto.NoticeSearchRequestDto;
import com.cstudy.modulecommon.dto.NoticeUpdateRequestDto;
import com.cstudy.modulecommon.error.notice.NotFoundNoticeId;
import com.cstudy.modulecommon.error.notice.NotMatchAdminIpException;
import com.cstudy.modulecommon.repository.notice.NoticeRepository;
import com.cstudy.modulecommon.util.LoginUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;
    private final MemberLoadComponent memberLoadComponent;

    public NoticeServiceImpl(NoticeRepository noticeRepository, MemberLoadComponent memberLoadComponent) {
        this.noticeRepository = noticeRepository;
        this.memberLoadComponent = memberLoadComponent;
    }

    @Override
    @Transactional
    public void saveNotice(NoticeSaveRequestDto noticeSaveRequestDto, LoginUserDto loginUserDto) {
        noticeRepository.save(Notice.builder()
                .title(noticeSaveRequestDto.getTitle())
                .content(noticeSaveRequestDto.getContent())
                .member(memberLoadComponent.loadMemberByEmail(loginUserDto.getMemberEmail()))
                .build());
    }

    @Override
    @Transactional
    public void updateNotice(Long noticeId, NoticeUpdateRequestDto noticeUpdateRequestDto, LoginUserDto loginUserDto) {
        noticeRepository.findById(noticeId)
                .orElseThrow(()->new NotFoundNoticeId(noticeId)).updateNotice(noticeUpdateRequestDto);
    }

    @Override
    @Transactional
    public void deleteNotice(Long noticeId, LoginUserDto loginUserDto) {
        Long adminId = 1L;

        Member member = memberLoadComponent.loadMemberByEmail(loginUserDto.getMemberEmail());

        Optional.of(member.getId())
                .filter(id -> id.equals(adminId))
                .orElseThrow(() -> new NotMatchAdminIpException(adminId));

        noticeRepository.deleteById(noticeId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NoticeResponseDto> findNoticePage(int page, int size, NoticeSearchRequestDto requestDto) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return noticeRepository.findNoticePage(pageRequest, requestDto);
    }

    @Override
    @Transactional(readOnly = true)
    public NoticeResponseDto findNoticeWithId(Long noticeId) {
        return NoticeResponseDto.of(noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NotFoundNoticeId(noticeId)));
    }
}
