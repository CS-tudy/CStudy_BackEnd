package com.cstudy.modulecommon.repository.workbook;

import com.cstudy.modulecommon.domain.competition.QCompetition;
import com.cstudy.modulecommon.domain.workbook.QWorkbook;
import com.cstudy.modulecommon.domain.workbook.QWorkbookQuestion;
import com.cstudy.modulecommon.dto.WorkbookQuestionResponseDto;
import com.cstudy.modulecommon.dto.WorkbookResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.cstudy.modulecommon.domain.competition.QCompetition.*;
import static com.cstudy.modulecommon.domain.question.QQuestion.question;
import static com.cstudy.modulecommon.domain.workbook.QWorkbook.*;
import static com.cstudy.modulecommon.domain.workbook.QWorkbookQuestion.*;


public class WorkbookRepositoryCustomImpl implements WorkbookRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public WorkbookRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<WorkbookResponseDto> findWorkbookList(Pageable pageable, String title, String description, String titleDesc) {
        LocalDateTime now = LocalDateTime.now();
        List<WorkbookResponseDto> content = queryFactory.select(
                Projections.fields(WorkbookResponseDto.class,
                        workbook.id,
                        workbook.title,
                        workbook.description,
                        workbook.createdAt
                ))
                .from(workbook)
                .leftJoin(workbook.competition, competition)
                .where(
                    titleContains(title),
                    descriptionContains(description),
                    titleAndDescContains(titleDesc),
                    competition.competitionEnd.between(LocalDateTime.MIN, now).or(competition.isNull())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(workbook.createdAt.desc())
                .fetch();
        long total = queryFactory.selectFrom(workbook)
                .where(
                    titleContains(title),
                    descriptionContains(description),
                    titleAndDescContains(titleDesc),
                    workbook.competitionEndTime.between(LocalDateTime.MIN, now)
                )
                .fetchCount();
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<WorkbookQuestionResponseDto> findWorkbookQuestionList(Pageable pageable,
                                                                      Long id) {
        List<WorkbookQuestionResponseDto> content = queryFactory.select(
                Projections.fields(WorkbookQuestionResponseDto.class,
                        workbookQuestion.id.as("workbookQuestionId"),
                        question.id.as("questionId"),
                        question.title
                ))
                .from(workbook)
                .leftJoin(workbook.questions, workbookQuestion)
                .on(workbookQuestion.workbook.eq(workbook))
                .leftJoin(workbookQuestion.question, question)
                .on(workbookQuestion.question.eq(question))
                .where(workbook.id.eq(id))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(question.id.desc())
                .fetch();
        long total = queryFactory.selectFrom(workbook)
                .leftJoin(workbook.questions, workbookQuestion)
                .leftJoin(workbookQuestion.question, question)
                .where(workbook.id.eq(id))
                .fetchCount();
        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression titleContains(String title) {
        return StringUtils.hasText(title) ? workbook.title.contains(title) : null;
    }

    private BooleanExpression descriptionContains(String description) {
        return StringUtils.hasText(description) ? workbook.description.contains(description) : null;
    }

    private BooleanExpression titleAndDescContains(String content) {
        return StringUtils.hasText(content) && StringUtils.hasText(content) ?
                workbook.title.contains(content).or(workbook.description.contains(content)) : null;
    }

    private BooleanExpression competitionEndBetween() {
        return workbook.competition.competitionEnd != null ?
                workbook.competition.competitionEnd.between(LocalDateTime.MIN, LocalDateTime.now()) : null;
    }
}
