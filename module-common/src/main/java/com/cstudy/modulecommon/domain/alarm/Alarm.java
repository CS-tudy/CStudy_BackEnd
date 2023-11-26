package com.cstudy.modulecommon.domain.alarm;

import com.cstudy.modulecommon.domain.BaseEntity;
import com.cstudy.modulecommon.domain.member.Member;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "\"alarm\"", indexes = {
        @Index(name = "member_id_idx", columnList = "member_id")
})
@TypeDef(name = "jsonb", typeClass = JsonType.class)
@NoArgsConstructor
@AllArgsConstructor
public class Alarm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    @Type(type = "jsonb")
    @Column(columnDefinition = "json")
    private AlarmArgs args;

    public Alarm(AlarmType alarmType, AlarmArgs args) {
        this.alarmType = alarmType;
        this.args = args;
    }

    public static Alarm of(Member member, AlarmType alarmType) {
        return Alarm.builder()
                .member(member)
                .alarmType(alarmType)
                .build();
    }

    @Builder
    public Alarm(Member member, AlarmType alarmType, AlarmArgs args) {
        this.member = member;
        this.alarmType = alarmType;
        this.args = args;
    }
}
