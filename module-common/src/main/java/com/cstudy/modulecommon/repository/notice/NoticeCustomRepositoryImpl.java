package com.cstudy.modulecommon.repository.notice;

import com.cstudy.modulecommon.domain.notice.Notice;
import com.cstudy.modulecommon.domain.notice.QNotice;
import com.cstudy.modulecommon.dto.NoticeResponseDto;
import com.cstudy.modulecommon.dto.NoticeSearchRequestDto;
import com.cstudy.modulecommon.dto.QNoticeResponseDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.cstudy.modulecommon.domain.notice.QNotice.*;


public class NoticeCustomRepositoryImpl implements NoticeCustomRepository {

    private final JPAQueryFactory queryFactory;

    public NoticeCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<NoticeResponseDto> findNoticePage(Pageable pageable, NoticeSearchRequestDto noticeSearchRequestDto) {
        List<NoticeResponseDto> content = queryFactory
                .select(
                        new QNoticeResponseDto(
                                notice.id,
                                notice.title.as("noticeTitle"),
                                notice.content.as("noticeContent"),
                                notice.createdDate.as("createdDate")
                        )
                )
                .from(notice)
                .where(
                        noticeTitleEq(noticeSearchRequestDto.getTitle()),
                        noticeContentEq(noticeSearchRequestDto.getContent())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(notice.createdDate.desc())
                .fetch();

        JPAQuery<Notice> countQuery = queryFactory
                .selectFrom(notice)
                .where(
                        noticeTitleEq(noticeSearchRequestDto.getTitle()),
                        noticeContentEq(noticeSearchRequestDto.getContent())
                );
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }


    private BooleanExpression noticeContentEq(String content) {
        return StringUtils.hasText(content) ? notice.content.contains(content) : null;
    }

    private BooleanExpression noticeTitleEq(String title) {
        return StringUtils.hasText(title) ? notice.title.contains(title) : null;
    }
}