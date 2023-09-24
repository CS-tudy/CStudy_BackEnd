package com.cstudy.modulecommon.repository.question;

import com.cstudy.modulecommon.dto.*;
import com.cstudy.modulecommon.util.LoginUserDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.cstudy.modulecommon.domain.choice.QChoice.choice;
import static com.cstudy.modulecommon.domain.competition.QCompetition.competition;
import static com.cstudy.modulecommon.domain.member.QMember.member;
import static com.cstudy.modulecommon.domain.question.QCategory.category;
import static com.cstudy.modulecommon.domain.question.QMemberQuestion.memberQuestion;
import static com.cstudy.modulecommon.domain.question.QQuestion.question;
import static com.cstudy.modulecommon.domain.workbook.QWorkbook.workbook;
import static com.cstudy.modulecommon.domain.workbook.QWorkbookQuestion.workbookQuestion;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.types.Projections.list;

public class QuestionRepositoryCustomImpl implements QuestionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public QuestionRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<QuestionPageWithCategoryAndTitle> findQuestionPageWithCategory(Pageable pageable, QuestionSearchCondition questionSearchCondition, LoginUserDto loginUserDto) {

        List<QuestionPageWithCategoryAndTitle> content = queryFactory.select(
                        new QQuestionPageWithCategoryAndTitle(
                                question.id.as("questionId"),
                                question.title.as("questionTitle"),
                                category.categoryTitle.as("categoryTitle"),
                                divisionStatusAboutMemberId(loginUserDto)
                        )).from(question)
                .distinct()
                .leftJoin(question.category, category)
                .leftJoin(question.questions, memberQuestion)
                .leftJoin(memberQuestion.member, member)
                .where(
                        questionTitleEq(questionSearchCondition.getQuestionTitle()),
                        memberIdEq(questionSearchCondition.getMemberId()),
                        statusEq(questionSearchCondition.getStatus()),
                        question.category.id.eq(category.id)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, content.size());
    }


    @Override
    public long getTotalCount(QuestionSearchCondition condition, Pageable pageable) {
        return queryFactory
                .select(Wildcard.count)
                .from(member)
                .fetch().get(0);
    }


    private static NumberExpression<Integer> divisionStatusAboutMemberId(LoginUserDto loginUserDto) {
        return Expressions.cases()
                .when(memberQuestion.member.id.eq(loginUserDto.getMemberId())).then(
                        Expressions.cases()
                                .when(memberQuestion.success.ne(0)).then(1)
                                .when(memberQuestion.fail.ne(0)).then(2)
                                .otherwise(Expressions.constant(0))
                )
                .otherwise(Expressions.constant(0))
                .as("status");
    }


    @Override
    public List<CompetitionQuestionDto> findQuestionWithCompetitionById(Long id) {
        List<CompetitionQuestionDto> content = queryFactory
                .selectFrom(question)
                .leftJoin(question.workbookQuestions, workbookQuestion)
                .leftJoin(workbookQuestion.workbook, workbook)
                .leftJoin(workbook.competition, competition)
                .leftJoin(question.choices, choice)
                .where(competition.id.eq(id))
                .transform(
                        groupBy(question.id).list(
                                Projections.constructor(CompetitionQuestionDto.class,
                                        question.id,
                                        question.description,
                                        list(
                                                Projections.constructor(ChoiceQuestionResponseDto.class,
                                                        choice.number, choice.content
                                                )
                                        ))
                        )
                );
        return content;
    }


    private BooleanExpression categoryTitleEq(String categoryTitle) {
        return StringUtils.hasText(categoryTitle) ? category.categoryTitle.eq(categoryTitle) : null;
    }

    private BooleanExpression questionTitleEq(String questionTitle) {
        return StringUtils.hasText(questionTitle) ? question.title.eq(questionTitle) : null;
    }

    private BooleanExpression memberIdEq(Long memberId) {
        return memberId != null ? member.id.eq(memberId) : null;
    }

    private BooleanExpression statusEq(Integer status) {
        if (status != null) {
            if (status.equals(1)) {
                return memberQuestion.success.ne(0);
            } else if (status.equals(2)) {
                return memberQuestion.fail.ne(0);
            }
        }
        return null;
    }


}
