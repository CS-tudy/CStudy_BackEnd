package com.cstudy.modulecommon.repository.question;

import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.question.MemberQuestion;
import com.cstudy.modulecommon.domain.question.Question;
import com.cstudy.modulecommon.error.member.NotFoundMemberId;
import com.cstudy.modulecommon.util.DataJpaCustomUtil;
import com.cstudy.modulecommon.util.TestEnum;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberQuestionRepositoryTest extends DataJpaCustomUtil {

    @Test
    public void 회원이_해당_문제를_풀었는지_성공_데이터가_있는지_판단_참() throws Exception{
        //given
        final long default_long = 1L;
        final int default_integer = 1;
        Member member = createMember(TestEnum.MEMBER_NAME.getMessage(), TestEnum.MEMBER_EMAIL.getMessage());
        Question question = createQuestion(TestEnum.QUESTION_TITLE.getMessage(), TestEnum.QUESTION_DESCRIPTION.getMessage(), TestEnum.QUESTION_EXPLAIN.getMessage(), null);
        addMemberQuestion(1, 0, member, question);
        //when
        boolean extractResult = memberQuestionRepository.existsByMemberAndQuestionAndSuccess(default_long, default_long, default_integer);
        //Then
        assertThat(extractResult).isEqualTo(true);
    }


    @Test
    public void 회원이_해당_문제를_풀었는지_성공_데이터가_있는지_판단_거짓() throws Exception{
        //given
        final long default_long = 1L;
        final int default_integer = 1;
        Member member = createMember(TestEnum.MEMBER_NAME.getMessage(), TestEnum.MEMBER_EMAIL.getMessage());
        Question question = createQuestion(TestEnum.QUESTION_TITLE.getMessage(), TestEnum.QUESTION_DESCRIPTION.getMessage(), TestEnum.QUESTION_EXPLAIN.getMessage(), null);
        addMemberQuestion(0, 1, member, question);
        //when
        boolean extractResult = memberQuestionRepository.existsByMemberAndQuestionAndSuccess(default_long, default_long, default_integer);
        //Then
        assertThat(extractResult).isEqualTo(false);
    }

    @Test
    public void 회원이_해당_문제를_풀었는지_실패_데이터가_있는지_판단_참() throws Exception{
        //given
        final long default_long = 1L;
        final int default_integer = 1;
        Member member = createMember(TestEnum.MEMBER_NAME.getMessage(), TestEnum.MEMBER_EMAIL.getMessage());
        Question question = createQuestion(TestEnum.QUESTION_TITLE.getMessage(), TestEnum.QUESTION_DESCRIPTION.getMessage(), TestEnum.QUESTION_EXPLAIN.getMessage(), null);
        addMemberQuestion(0, 1, member, question);
        //when
        boolean extractResult = memberQuestionRepository.existsByMemberAndQuestionAndFail(default_long, default_long, default_integer);
        //Then
        assertThat(extractResult).isEqualTo(true);
    }

    @Test
    public void 회원이_해당_문제를_풀었는지_실패_데이터가_있는지_판단_거짓() throws Exception{
        //given
        final long default_long = 1L;
        final int default_integer = 1;
        Member member = createMember(TestEnum.MEMBER_NAME.getMessage(), TestEnum.MEMBER_EMAIL.getMessage());
        Question question = createQuestion(TestEnum.QUESTION_TITLE.getMessage(), TestEnum.QUESTION_DESCRIPTION.getMessage(), TestEnum.QUESTION_EXPLAIN.getMessage(), null);
        addMemberQuestion(1, 0, member, question);
        //when
        boolean extractResult = memberQuestionRepository.existsByMemberAndQuestionAndFail(default_long, default_long, default_integer);
        //Then
        assertThat(extractResult).isEqualTo(false);
    }

    @Test
    public void 회원이_문제를_풀었는지_상태를_검증하는_로직() throws Exception{
        //given
        final long default_long = 1L;
        Member member = createMember(TestEnum.MEMBER_NAME.getMessage(), TestEnum.MEMBER_EMAIL.getMessage());
        Question question = createQuestion(TestEnum.QUESTION_TITLE.getMessage(), TestEnum.QUESTION_DESCRIPTION.getMessage(), TestEnum.QUESTION_EXPLAIN.getMessage(), null);
        addMemberQuestion(1, 0, member, question);
        //when
        MemberQuestion getMemberRelatedQuestion = memberQuestionRepository.findByQuestionAboutMemberIdAndQuestionId(default_long, default_long)
                .orElseThrow(()-> new NotFoundMemberId(default_long));
        //Then
        assertThat(getMemberRelatedQuestion.getQuestion().getId()).isEqualTo(1);
        assertThat(getMemberRelatedQuestion.getSuccess()).isEqualTo(1);
        assertThat(getMemberRelatedQuestion.getFail()).isEqualTo(0);
    }
}