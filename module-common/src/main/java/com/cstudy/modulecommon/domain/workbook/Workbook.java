package com.cstudy.modulecommon.domain.workbook;

import com.cstudy.modulecommon.domain.competition.Competition;
import com.cstudy.modulecommon.dto.UpdateWorkbookRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Workbook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workbook_id")
    private Long id;

    @Column(name = "workbook_title")
    private String title;

    @Column(name = "workbook_description")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at")
    private final LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "competition_end_time")
    private LocalDateTime competitionEndTime;

    @OneToMany(
            mappedBy = "workbook",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    List<WorkbookQuestion> questions = new ArrayList<>();

    @OneToOne(
        mappedBy = "workbook",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL
    )
    private Competition competition;

    @Builder
    public Workbook(String title, String description, LocalDateTime endTime){
        this.title = title;
        this.description = description;
        this.competitionEndTime = LocalDateTime.now();
    }

    public void addQuestion(WorkbookQuestion question){
        this.questions.add(question);
    }

    public void setCompetition(Competition competition){
        this.competition = competition;
        this.competitionEndTime = competition.getCompetitionEnd();
    }

    public void changeWorkbook(UpdateWorkbookRequestDto workbookDto) {
        this.title = workbookDto.getTitle();
        this.description = workbookDto.getDescription();
    }

    public void deleteQuestion(WorkbookQuestion question) {
        this.questions.remove(question);
    }
}
