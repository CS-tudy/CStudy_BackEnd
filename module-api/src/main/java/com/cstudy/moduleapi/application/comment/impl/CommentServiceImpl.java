package com.cstudy.moduleapi.application.comment.impl;

import com.cstudy.moduleapi.application.comment.CommentService;
import com.cstudy.moduleapi.dto.comment.NoticeCommentRequestDto;
import com.cstudy.modulecommon.error.member.NotFoundMemberId;
import com.cstudy.modulecommon.util.LoginUserDto;
import com.cstudy.modulecommon.domain.comment.Comment;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.notice.Notice;
import com.cstudy.modulecommon.repository.comment.CommentRepository;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import com.cstudy.modulecommon.repository.notice.NoticeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;


    public CommentServiceImpl(CommentRepository commentRepository, NoticeRepository noticeRepository, MemberRepository memberRepository) {
        this.commentRepository = commentRepository;
        this.noticeRepository = noticeRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    @Transactional
    public void saveNoticeComment(NoticeCommentRequestDto dto, LoginUserDto loginUserDto) {
        try {
            Notice notice = noticeRepository.getReferenceById(dto.getNoticeId());
            Member member = memberRepository.findById(loginUserDto.getMemberId()).orElseThrow(() -> new NotFoundMemberId(loginUserDto.getMemberId()));
            Comment noticeComment = dto.toEntity(notice, member);

            if (dto.getParentCommentId() != null) {
                Comment parentComment = commentRepository.getReferenceById(dto.getParentCommentId());
                parentComment.addChildComment(noticeComment);
            } else {
                commentRepository.save(noticeComment);
            }
        } catch (EntityNotFoundException e) {
            log.warn("댓글 저장 실패. 댓글 작성에 필요한 정보를 찾을 수 없습니다 - {}", e.getLocalizedMessage());
        }
    }
}
