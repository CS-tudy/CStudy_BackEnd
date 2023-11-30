package com.cstudy.modulecommon.util;

import com.cstudy.modulecommon.config.QueryDslConfig;
import com.cstudy.modulecommon.domain.choice.Choice;
import com.cstudy.modulecommon.domain.file.File;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.question.Category;
import com.cstudy.modulecommon.domain.question.MemberQuestion;
import com.cstudy.modulecommon.domain.question.Question;
import com.cstudy.modulecommon.repository.choice.ChoiceRepository;
import com.cstudy.modulecommon.repository.comment.CommentRepository;
import com.cstudy.modulecommon.repository.file.FileRepository;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import com.cstudy.modulecommon.repository.notice.NoticeRepository;
import com.cstudy.modulecommon.repository.question.CategoryRepository;
import com.cstudy.modulecommon.repository.question.MemberQuestionRepository;
import com.cstudy.modulecommon.repository.question.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryDslConfig.class})
public class DataJpaCustomUtil {
    @Autowired
    protected ChoiceRepository choiceRepository;
    @Autowired
    protected CategoryRepository categoryRepository;
    @Autowired
    protected EntityManager entityManager;
    @Autowired
    protected MemberRepository memberRepository;
    @Autowired
    protected MemberQuestionRepository memberQuestionRepository;
    @Autowired
    protected NoticeRepository noticeRepository;
    @Autowired
    protected CommentRepository commentRepository;
    @Autowired
    protected QuestionRepository questionRepository;
    @Autowired
    protected FileRepository fileRepository;

    public Member createMember(String memberName, String memberEmail) {
        Member member = Member.of(memberName, memberEmail);
        return memberRepository.save(member);
    }

    public Question createQuestion(String title, String desc, String explain, List<Choice> choices) {
        Question question = Question.of(title, desc, explain, choices);
        return questionRepository.save(question);
    }

    public MemberQuestion addMemberQuestion(int success, int fail, Member member, Question question) {
        MemberQuestion memberQuestion = MemberQuestion.of(success, fail, 1L, member, question);
        return memberQuestionRepository.save(memberQuestion);
    }

    public Member memberRelatedFile(String memberName, String memberEmail, List<File> file) {
        Member member = Member.builder()
                .name(memberName)
                .email(memberEmail)
                .file(file)
                .build();
        return memberRepository.save(member);
    }

    public File createFile(String fileName, Member member) {
        File file = File.builder()
                .fileName(fileName)
                .member(member)
                .build();
        return fileRepository.save(file);
    }

    public Category createCategory(String categoryName) {
        Category category = Category.of(categoryName);
        return categoryRepository.save(category);
    }
}
