package com.cstudy.modulecommon.domain.notice;

import com.cstudy.modulecommon.domain.BaseEntity;
import com.cstudy.modulecommon.domain.comment.Comment;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.dto.NoticeUpdateRequestDto;
import lombok.*;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class Notice extends BaseEntity {

    /********************************* PK 필드 *********************************/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /********************************* PK가 아닌 필드 *********************************/

    @Column(name = "notice_title",nullable = false)
    private String title;

    @Column(name = "notice_content", nullable = false)
    private String content;

    /********************************* 연관관계 매핑 *********************************/

    @JoinColumn(name = "member_id")
    @ManyToOne(optional = false)
    private Member member;

    @ToString.Exclude
    @OrderBy("createdDate ASC")
    @OneToMany(mappedBy = "notice", cascade = CascadeType.REMOVE)
    private Set<Comment> comments = new LinkedHashSet<>();

    @Builder
    public Notice(
            Long id,
            String title,
            String content,
            Member member
    ) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.member = member;
    }

    /********************************* 비니지스 로직 *********************************/

    public void updateNotice(NoticeUpdateRequestDto noticeUpdateRequestDto) {
        this.title = noticeUpdateRequestDto.getTitle();
        this.content = noticeUpdateRequestDto.getContent();
    }
}
