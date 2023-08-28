package com.cstudy.modulecommon.domain.file;

import com.cstudy.modulecommon.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "File", uniqueConstraints = {
        @UniqueConstraint(name = "fileName", columnNames = {"fileName"})
})
public class File {

    /********************************* PK 필드 *********************************/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /********************************* PK가 아닌 필드 *********************************/
    private String fileName;

    /********************************* 연관관계 매핑 *********************************/
    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    /********************************* 빌더 *********************************/
    @Builder
    public File(String fileName, Member member) {
        this.fileName = fileName;
        this.member = member;
    }
}
