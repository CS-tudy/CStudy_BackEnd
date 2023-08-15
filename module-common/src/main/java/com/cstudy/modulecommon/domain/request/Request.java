package com.cstudy.modulecommon.domain.request;

import com.cstudy.modulecommon.domain.BaseEntity;
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
public class Request extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long id;

    @Column(name = "request_flag")
    private boolean flag;

    @Column(name = "request_title")
    private String title;

    @Column(name = "request_description")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at")
    private final LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL
    )
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Request(String title, String description, Member member){
        this.flag = false;
        this.title = title;
        this.description = description;
        this.member = member;
    }

    public void updateFlag(boolean flag){
        this.flag = flag;
    }


    public void updateRequest(UpdateRequestRequestDto updateRequestRequestDto) {
        this.title = updateRequestRequestDto.getTitle();
        this.description = updateRequestRequestDto.getDescription();
    }
}
