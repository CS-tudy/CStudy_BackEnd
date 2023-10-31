package com.cstudy.moduleapi.application.comment.impl;

import com.cstudy.moduleapi.application.comment.CommentService;
import com.cstudy.moduleapi.application.member.MemberLoadComponent;
import com.cstudy.moduleapi.dto.comment.NoticeCommentRequestDto;
import com.cstudy.moduleapi.dto.comment.NoticeCommentResponse;
import com.cstudy.modulecommon.domain.comment.Comment;
import com.cstudy.modulecommon.domain.notice.Notice;
import com.cstudy.modulecommon.error.comment.NotFoundCommentId;
import com.cstudy.modulecommon.error.comment.NotFoundCommentParentId;
import com.cstudy.modulecommon.repository.comment.CommentRepository;
import com.cstudy.modulecommon.repository.notice.NoticeRepository;
import com.cstudy.modulecommon.util.LoginUserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final NoticeRepository noticeRepository;
    private final MemberLoadComponent memberLoadComponent;

    public CommentServiceImpl(CommentRepository commentRepository, NoticeRepository noticeRepository, MemberLoadComponent memberLoadComponent) {
        this.commentRepository = commentRepository;
        this.noticeRepository = noticeRepository;
        this.memberLoadComponent = memberLoadComponent;
    }

    /**
     * 계층형 댓글을 생성한다.
     * 처음에 부모 댓글은 parentId가 없다.
     * 이후 부모 댓글에 대댓글을 작성하고 싶으면 parentId를 추가해서 작성한다.
     * @param dto
     * @param loginUserDto
     */
    @Override
    @Transactional
    public void saveNoticeComment(NoticeCommentRequestDto dto, LoginUserDto loginUserDto) {
        try {
            Notice notice = noticeRepository.getReferenceById(dto.getNoticeId());
            Comment noticeComment = dto.toEntity(notice, memberLoadComponent.loadMemberByEmail(loginUserDto.getMemberEmail()));

            if (dto.getParentCommentId() != null) {
                Comment parentComment = commentRepository.getReferenceById(dto.getParentCommentId());
                parentComment.addChildComment(noticeComment);
            } else {
                commentRepository.save(noticeComment);
            }
        } catch (EntityNotFoundException e) {
            log.warn("댓글 저장 실패. 댓글 작성에 필요한 정보를 찾을 수 없습니다 - {}", e.getLocalizedMessage());
            throw new NotFoundCommentParentId(e.getLocalizedMessage());
        }
    }

    /**
     * 해당 공지사항에 대한 아이디에 대한 댓글을 조회를 한다.
     * @param noticeId 공지사항 아이디
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<NoticeCommentResponse> getCommentsForNotice(Long noticeId) {
        List<Comment> comments = commentRepository.findNoticePage(noticeId);
        Map<Long, NoticeCommentResponse> responseMap = new HashMap<>();

        for (Comment comment : comments) {
            NoticeCommentResponse response = NoticeCommentResponse.builder()
                    .id(comment.getId())
                    .content(comment.getContent())
                    .author(comment.getMember().getName())
                    .memberId(comment.getMember().getId())
                    .parentCommentId(comment.getParentCommentId())
                    .build();
            responseMap.put(comment.getId(), response);
        }

        List<NoticeCommentResponse> topLevelResponses = new ArrayList<>();
        for (NoticeCommentResponse response : responseMap.values()) {
            Long parentCommentId = response.getParentCommentId();
            if (parentCommentId == null) {
                topLevelResponses.add(response);
            } else {
                NoticeCommentResponse parentResponse = responseMap.get(parentCommentId);
                if (parentResponse != null) {
                    parentResponse.getChildComments().add(response);
                }
            }
        }

        return topLevelResponses;
    }

    /**
     * 해당 공지사항의 댓글을 삭제를 합니다.
     * 댓글 부모 아이디를 판단하고 만약에 있으면 자식 댓글도 삭제를 합니다.
     * @param commentId 댓글 아이디
     */
    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        Comment commentToDelete = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundCommentId(commentId));

        if (commentToDelete.getParentCommentId() == null) {
            commentRepository.delete(commentToDelete);
        } else {
            Comment parentComment = commentRepository.findById(commentToDelete.getParentCommentId())
                    .orElseThrow(() -> new NotFoundCommentParentId(String.valueOf(commentId)));
            parentComment.getChildComments().remove(commentToDelete);
        }
    }
}
