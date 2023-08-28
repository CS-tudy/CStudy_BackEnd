package com.cstudy.modulecommon.domain.request;

import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.dto.UpdateRequestRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Request {
    /********************************* PK 필드 *********************************/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long id;
    /********************************* PK가 아닌 필드 *********************************/
    @Column(name = "request_flag")
    private boolean flag;

    @Column(name = "request_title")
    private String title;

    @Column(name = "request_description")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at")
    private final LocalDateTime createdAt = LocalDateTime.now();

    /********************************* 연관관계 매핑 *********************************/
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = "member_id")
    private Member member;

    /********************************* 빌더 *********************************/
    @Builder
    public Request(String title, String description, Member member) {
        this.flag = false;
        this.title = title;
        this.description = description;
        this.member = member;
    }

    /********************************* 비즈니스 로직 *********************************/
    public void updateFlag(boolean flag) {
        this.flag = flag;
    }


    public void updateRequest(UpdateRequestRequestDto updateRequestRequestDto) {
        this.title = updateRequestRequestDto.getTitle();
        this.description = updateRequestRequestDto.getDescription();
    }
}
