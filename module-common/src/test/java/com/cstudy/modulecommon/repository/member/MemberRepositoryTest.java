package com.cstudy.modulecommon.repository.member;

import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.question.Question;
import com.cstudy.modulecommon.error.member.NotFoundMemberEmail;
import com.cstudy.modulecommon.error.member.NotFoundMemberId;
import com.cstudy.modulecommon.util.DataJpaCustomUtil;
import com.cstudy.modulecommon.util.TestEnum;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class MemberRepositoryTest extends DataJpaCustomUtil {

    @Test
    public void 이메일을_기반으로_회원_찾기() throws Exception {
        //given
        createMember(TestEnum.MEMBER_NAME.getMessage(), TestEnum.MEMBER_EMAIL.getMessage());
        //when
        Member getMember = memberRepository.findByEmail(TestEnum.MEMBER_EMAIL.getMessage())
                .orElseThrow(() -> new NotFoundMemberEmail(TestEnum.MEMBER_EMAIL.getMessage()));

        //Then
        assertThat(getMember.getName()).isEqualTo(TestEnum.MEMBER_NAME.getMessage());
        assertThat(getMember.getEmail()).isEqualTo(TestEnum.MEMBER_EMAIL.getMessage());
    }


    @Test
    public void 회원과_관련되_문제() throws Exception {
        //given
        Member member = createMember(TestEnum.MEMBER_NAME.getMessage(), TestEnum.MEMBER_EMAIL.getMessage());
        Question question = createQuestion(TestEnum.QUESTION_TITLE.getMessage(), TestEnum.QUESTION_DESCRIPTION.getMessage(), TestEnum.QUESTION_EXPLAIN.getMessage(), null);

        addMemberQuestion(1, 0, member, question);

        //when
        List<Member> withQuestions = memberRepository.findAllWithQuestions();

        //Then
        assertThat(member.getEmail()).isEqualTo(TestEnum.MEMBER_EMAIL.getMessage());
        assertThat(withQuestions.size()).isEqualTo(1);
    }

    @Test
    public void 회원아이디를_기준으로_조회() throws Exception {
        //given
        final long memberId = 1L;
        Member member = createMember(TestEnum.MEMBER_NAME.getMessage(), TestEnum.MEMBER_EMAIL.getMessage());
        //when
        Member getMember = memberRepository.findByIdForUpdateOptimistic(memberId).orElseThrow(() -> new NotFoundMemberId(memberId));
        //Then
        assertThat(getMember.getName()).isEqualTo(TestEnum.MEMBER_NAME.getMessage());
        assertThat(getMember.getEmail()).isEqualTo(TestEnum.MEMBER_EMAIL.getMessage());
    }

    @Test
    @Transactional
    public void 회원과_파일_조회() throws Exception {
        //given
        final String fileName = "파일.jpg";

        Member member = createMember(TestEnum.MEMBER_NAME.getMessage(), TestEnum.MEMBER_EMAIL.getMessage());
        createFile(fileName, member);

        //when
        Member getMember = memberRepository.findByMemberFetchFile(1L)
                .orElseThrow(() -> new NotFoundMemberId(1L));

        //Then
        assertThat(getMember.getFile().get(0).getFileName()).isEqualTo(fileName);
    }

    @Test
    public void 회원이_문제를_풀었는지_판단() throws Exception {
        //given
        final long memberId = 1L;
        final long questionId = 1L;


        Member member = createMember(TestEnum.MEMBER_NAME.getMessage(), TestEnum.MEMBER_EMAIL.getMessage());
        Question question = createQuestion(TestEnum.QUESTION_TITLE.getMessage(), TestEnum.QUESTION_DESCRIPTION.getMessage(), TestEnum.QUESTION_EXPLAIN.getMessage(), null);
        addMemberQuestion(1, 0, member, question);

        //when
        boolean existsedByMemberIdAndCompetitionId = memberRepository.existsByMemberIdAndCompetitionId(memberId, questionId);
        //Then
        assertThat(existsedByMemberIdAndCompetitionId).isEqualTo(false);
    }
}
