package com.cstudy.modulecommon.domain.comment;

import com.cstudy.modulecommon.domain.BaseEntity;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.notice.Notice;
import lombok.*;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor
public class Comment extends BaseEntity {

    /********************************* PK 필드 *********************************/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @Setter
    @Column(updatable = false)
    private Long parentCommentId; // 부모 댓글 ID

    /********************************* 연관관계 매핑 *********************************/

    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id")
    private Member member;


    @ToString.Exclude
    @OrderBy("createdDate ASC")
    @OneToMany(mappedBy = "parentCommentId", cascade = CascadeType.ALL)
    private Set<Comment> childComments = new LinkedHashSet<>();

    @ManyToOne(optional = false)
    private Notice notice;


    /********************************* 빌더 *********************************/



    @Builder
    public Comment(Long id, String content, Long parentCommentId, Member member, Set<Comment> childComments, Notice notice) {
        this.id = id;
        this.content = content;
        this.parentCommentId = parentCommentId;
        this.member = member;
        this.childComments = childComments;
        this.notice = notice;
    }

    public Comment(Notice notice, Member member, Long parentCommentId, String content) {
        this.notice = notice;
        this.member = member;
        this.parentCommentId = parentCommentId;
        this.content = content;
    }

    public static Comment of(Notice notice, Member member, String content) {
        return new Comment(notice, member, null, content);
    }


    /********************************* 연관관계 편의 메서드 *********************************/

    public void addChildComment(Comment child) {
        child.setParentCommentId(this.getId());
        this.getChildComments().add(child);
    }

}
