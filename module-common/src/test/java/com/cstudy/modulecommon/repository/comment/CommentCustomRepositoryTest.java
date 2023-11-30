package com.cstudy.modulecommon.repository.comment;

import com.cstudy.modulecommon.config.QueryDslConfig;
import com.cstudy.modulecommon.domain.comment.Comment;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.notice.Notice;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import com.cstudy.modulecommon.repository.notice.NoticeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryDslConfig.class})
class CommentCustomRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private NoticeRepository noticeRepository;

    @Test
    @DisplayName("개인 공지사항에 존재하는 댓글")
    public void getComment_Related_Notice() throws Exception {
        //given
        final String memberName = "김무건";
        final String memberEmail = "test1234@.com";

        final Long noticeId = 1L;
        final String title = "제목";
        final String content = "내용";

        Member member = Member.of(memberName, memberEmail);
        Notice notice = Notice.of(title, content, member);

        memberRepository.save(member);
        noticeRepository.save(notice);

        Comment comment1 = Comment.of(notice, member, "내용1");
        Comment comment2 = Comment.of(notice, member, "내용2");
        Comment comment3 = Comment.of(notice, member, "내용3");

        commentRepository.saveAll(List.of(comment1, comment2, comment3));


        //when
        List<Comment> commentList = commentRepository.findNoticePage(noticeId);
        //Then
        Assertions.assertThat(commentList.size()).isEqualTo(3);
        Assertions.assertThat(commentList.get(0).getContent()).isEqualTo("내용1");
        Assertions.assertThat(commentList.get(1).getContent()).isEqualTo("내용2");
        Assertions.assertThat(commentList.get(2).getContent()).isEqualTo("내용3");
    }

}