package com.cstudy.moduleapi.application.notice.Impl;

import com.cstudy.moduleapi.application.notice.NoticeService;
import com.cstudy.moduleapi.dto.comment.NoticeWithCommentDto;
import com.cstudy.modulecommon.dto.NoticeResponseDto;
import com.cstudy.moduleapi.dto.notice.NoticeSaveRequestDto;
import com.cstudy.modulecommon.dto.NoticeSearchRequestDto;
import com.cstudy.modulecommon.dto.NoticeUpdateRequestDto;
import com.cstudy.modulecommon.error.member.NotFoundMemberId;
import com.cstudy.modulecommon.error.notice.NotFoundNoticeId;
import com.cstudy.modulecommon.error.notice.NotMatchAdminIpException;
import com.cstudy.modulecommon.util.LoginUserDto;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.notice.Notice;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import com.cstudy.modulecommon.repository.notice.NoticeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class NoticeServiceImpl implements NoticeService {

    private final MemberRepository memberRepository;
    private final NoticeRepository noticeRepository;

    public NoticeServiceImpl(
            MemberRepository memberRepository,
            NoticeRepository noticeRepository
    ) {
        this.memberRepository = memberRepository;
        this.noticeRepository = noticeRepository;
    }

    @Override
    @Transactional
    public void saveNotice(NoticeSaveRequestDto noticeSaveRequestDto, LoginUserDto loginUserDto) {
        noticeRepository.save(Notice.builder()
                .title(noticeSaveRequestDto.getTitle())
                .content(noticeSaveRequestDto.getContent())
                .member(memberRepository.findById(loginUserDto.getMemberId())
                        .orElseThrow(() -> new NotFoundMemberId(loginUserDto.getMemberId())))
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

        Member member = memberRepository.findById(loginUserDto.getMemberId())
                .orElseThrow(() -> new NotFoundMemberId(loginUserDto.getMemberId()));

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
}
