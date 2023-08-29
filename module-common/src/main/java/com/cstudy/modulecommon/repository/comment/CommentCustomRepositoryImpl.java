package com.cstudy.modulecommon.repository.comment;

import com.cstudy.modulecommon.domain.comment.Comment;
import com.cstudy.modulecommon.domain.comment.QComment;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;


public class CommentCustomRepositoryImpl implements CommentCustomRepository {

    private final JPAQueryFactory queryFactory;

    public CommentCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<Comment> findNoticePage(Long noticeId) {
        return queryFactory.selectFrom(QComment.comment)
                .join(QComment.comment.notice).fetchJoin()
                .where(QComment.comment.notice.id.eq(noticeId))
                .orderBy(
                        QComment.comment.parentCommentId.asc().nullsFirst(),
                        QComment.comment.createdDate.asc()
                )
                .fetch();
    }
}