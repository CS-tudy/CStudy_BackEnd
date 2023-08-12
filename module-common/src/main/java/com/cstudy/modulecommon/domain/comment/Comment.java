package com.cstudy.modulecommon.domain.comment;

import com.cstudy.modulecommon.domain.BaseEntity;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.notice.Notice;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Entity
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    @Setter
    @Column(updatable = false)
    private Long parentCommentId; // 부모 댓글 ID

    @ToString.Exclude
    @OrderBy("createdDate ASC")
    @OneToMany(mappedBy = "parentCommentId", cascade = CascadeType.ALL)
    private Set<Comment> childComments = new LinkedHashSet<>();

    @ManyToOne(optional = false)
    private Notice notice;

    @Builder
    public Comment(Long id, Member member, Long parentCommentId, Set<Comment> childComments, Notice notice) {
        this.id = id;
        this.member = member;
        this.parentCommentId = parentCommentId;
        this.childComments = childComments;
        this.notice = notice;
    }

    protected Comment() {

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

    public void addChildComment(Comment child) {
        child.setParentCommentId(this.getId());
        this.getChildComments().add(child);
    }

}
