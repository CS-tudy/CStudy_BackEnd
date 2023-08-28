package com.cstudy.modulecommon.domain.notice;

import com.cstudy.modulecommon.domain.BaseEntity;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.dto.NoticeUpdateRequestDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
