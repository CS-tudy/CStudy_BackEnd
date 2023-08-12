package com.cstudy.modulecommon.domain.refresh;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long memberId;
    private String value;

    @Builder
    public RefreshToken(Long id, Long memberId, String value) {
        this.id = id;
        this.memberId = memberId;
        this.value = value;
    }
}
