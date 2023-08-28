package com.cstudy.modulecommon.domain.refresh;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "RefreshToken", uniqueConstraints = {
        @UniqueConstraint(name = "value", columnNames = {"value"})
})
public class RefreshToken {
    /********************************* PK 필드 *********************************/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /********************************* PK가 아닌 필드 *********************************/
    private Long memberId;
    private String value;

    /********************************* 빌더 *********************************/
    @Builder
    public RefreshToken(Long id, Long memberId, String value) {
        this.id = id;
        this.memberId = memberId;
        this.value = value;
    }
}
