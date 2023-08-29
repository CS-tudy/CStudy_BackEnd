package com.cstudy.modulecommon.domainEntity.comment;

import com.cstudy.modulecommon.domain.comment.Comment;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.notice.Notice;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommentTest {
    private Comment parentComment;
    private Member member;
    private Notice notice;

    @BeforeEach
    public void setUp() {
        member = new Member();
        notice = new Notice();
        parentComment = Comment.of(notice, member, "부모 Comment");
    }

    @Test
    public void createComment() {
        Assertions.assertNotNull(parentComment);
        Assertions.assertEquals("Parent Comment", parentComment.getContent());
        Assertions.assertEquals(member, parentComment.getMember());
        Assertions.assertEquals(notice, parentComment.getNotice());
        Assertions.assertNull(parentComment.getParentCommentId());
        Assertions.assertEquals(0, parentComment.getChildComments().size());
    }

    @Test
    public void addChildComment() {
        Comment childComment = Comment.of(notice, member, "자식 Comment");
        parentComment.addChildComment(childComment);

        Assertions.assertEquals(1, parentComment.getChildComments().size());
        Assertions.assertEquals(parentComment.getId(), childComment.getParentCommentId());
    }

}