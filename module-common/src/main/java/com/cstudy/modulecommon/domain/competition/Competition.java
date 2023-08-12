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
public class Competition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "competition_id")
    private Long id;

    @Column(name = "Competition_title")
    private String competitionTitle;

    @Column(name = "competition_participants")
    private int participants;

    private LocalDateTime competitionStart;

    private LocalDateTime competitionEnd;

    @Version
    private Long version;

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

    @Builder
    public Competition(String competitionTitle, int participants, LocalDateTime competitionStart, LocalDateTime competitionEnd, Workbook workbook) {
        this.competitionTitle = competitionTitle;
        this.participants = participants;
        this.competitionStart = competitionStart;
        this.competitionEnd = competitionEnd;
        this.workbook = workbook;
    }

    public void decreaseParticipantsCount() {
        this.participants--;
    }
}
