package com.cstudy.modulecommon.domain.notice;

import com.cstudy.modulecommon.domain.BaseEntity;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.dto.NoticeUpdateRequestDto;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Notice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "notice_title",nullable = false)
    private String title;

    @Column(name = "notice_content", nullable = false)
    private String content;

    @JoinColumn(name = "member_id")
    @ManyToOne(optional = false)
    private Member member;

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

    protected Notice() {

    }

    public void updateNotice(NoticeUpdateRequestDto noticeUpdateRequestDto) {
        this.title = noticeUpdateRequestDto.getTitle();
        this.content = noticeUpdateRequestDto.getContent();
    }
}
