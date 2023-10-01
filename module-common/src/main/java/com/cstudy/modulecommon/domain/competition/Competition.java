package com.cstudy.modulecommon.domain.competition;

import com.cstudy.modulecommon.domain.workbook.Workbook;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "Competition", uniqueConstraints = {
//        @UniqueConstraint(name = "competitionTitle", columnNames = {"competitionTitle"})
})
public class Competition {

    /********************************* PK 필드 *********************************/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "competition_id")
    private Long id;

    /********************************* PK가 아닌 필드 *********************************/

    @Column(nullable = false)
    private String competitionTitle;

    @Column(nullable = false)
    private int participants;

    private LocalDateTime competitionStart;

    private LocalDateTime competitionEnd;

    /********************************* 동시성 버전 *********************************/

    @Version
    private Long version;

    /********************************* 연관관계 매핑 *********************************/

    @OneToMany(
            mappedBy = "competition",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    Set<MemberCompetition> memberCompetitionSet = new HashSet<>();

    @OneToOne(
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL
    )
    @JoinColumn(name = "workbook_id")
    private Workbook workbook;

    /********************************* 빌더 *********************************/
    @Builder
    public Competition(String competitionTitle, int participants, LocalDateTime competitionStart, LocalDateTime competitionEnd, Workbook workbook) {
        this.competitionTitle = competitionTitle;
        this.participants = participants;
        this.competitionStart = competitionStart;
        this.competitionEnd = competitionEnd;
        this.workbook = workbook;
    }

    /********************************* 비니지스 로직 *********************************/

    public void decreaseParticipantsCount() {
        this.participants--;
    }
}
