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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class NoticeServiceImpl implements NoticeService {

    private static final Long ADMIN_ID = 1L;

    private final NoticeRepository noticeRepository;
    private final MemberLoadComponent memberLoadComponent;

    public NoticeServiceImpl(NoticeRepository noticeRepository, MemberLoadComponent memberLoadComponent) {
        this.noticeRepository = noticeRepository;
        this.memberLoadComponent = memberLoadComponent;
    }

    /**
     *  공지사항을 생성을 한다.
     *  관리자만 생성할 수 있다.
     */
    @Override
    @Transactional
    public void saveNotice(NoticeSaveRequestDto noticeSaveRequestDto, LoginUserDto loginUserDto) {
        log.info("제목 : {}", noticeSaveRequestDto.getTitle());
        log.info("내용 : {}", noticeSaveRequestDto.getContent());
        noticeRepository.save(Notice.builder()
                .title(noticeSaveRequestDto.getTitle())
                .content(noticeSaveRequestDto.getContent())
                .member(memberLoadComponent.loadMemberById(loginUserDto.getMemberId()))
                .build());
    }

    /**
     * 공지사항의 아이디를 기반으로 업데이트를 한다.
     */
    @Override
    @Transactional
    public void updateNotice(Long noticeId, NoticeUpdateRequestDto noticeUpdateRequestDto, LoginUserDto loginUserDto) {
        log.info("noticeId : {}", noticeId);
        log.info("변경을 원하는 제목 : {}", noticeUpdateRequestDto.getTitle());
        log.info("변경을 원하는 내용 : {}", noticeUpdateRequestDto.getContent());
        noticeRepository.findById(noticeId)
                .orElseThrow(()->new NotFoundNoticeId(noticeId))
                .updateNotice(noticeUpdateRequestDto);
    }

    /**
     * 공지사항을 삭제한다.
     * 이때 공지사항을 삭제하는 권한은 관리자만 제거가 가능하다.
     */
    @Override
    @Transactional
    public void deleteNotice(Long noticeId, LoginUserDto loginUserDto) {
        log.info("noticeId : {}", noticeId);
        Member member = memberLoadComponent.loadMemberById(loginUserDto.getMemberId());

        Optional.of(member.getId())
                .filter(id -> id.equals(ADMIN_ID))
                .orElseThrow(() -> new NotMatchAdminIpException(ADMIN_ID));

        noticeRepository.deleteById(noticeId);
    }

    /**
     *  처음 공지사항을 조회하는 페이징 처리
     *  PAGE, SIZE, REQUEST를 통해서 페이징을 처리를 할 수 있다.
     *   제목, 내용, 생성일을 통해서 조회를 할 수 있다.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<NoticeResponseDto> findNoticePage(int page, int size, NoticeSearchRequestDto requestDto) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return noticeRepository.findNoticePage(pageRequest, requestDto);
    }

    /**
     * 공지사항 페이징에서 세부 공지사항의 정보를 조회한다.
     */
    @Override
    @Transactional(readOnly = true)
    public NoticeResponseDto findNoticeWithId(Long noticeId) {
        return NoticeResponseDto.of(noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NotFoundNoticeId(noticeId)));
    }
}
