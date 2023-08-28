package com.cstudy.modulecommon.repository.comment;

import com.cstudy.modulecommon.domain.comment.Comment;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

import static com.cstudy.modulecommon.domain.comment.QComment.comment;

public class CommentCustomRepositoryImpl implements CommentCustomRepository {

    private final JPAQueryFactory queryFactory;

    public CommentCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<Comment> findNoticePage(Long noticeId) {
        return queryFactory.selectFrom(comment)
                .join(comment.notice).fetchJoin()
                .where(comment.notice.id.eq(noticeId))
                .orderBy(
                        comment.parentCommentId.asc().nullsFirst(),
                        comment.createdDate.asc()
                )
                .fetch();
    }
}