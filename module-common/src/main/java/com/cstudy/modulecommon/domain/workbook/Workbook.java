package com.cstudy.modulecommon.domain.workbook;

import com.cstudy.modulecommon.domain.competition.Competition;
import com.cstudy.modulecommon.domain.file.File;
import com.cstudy.modulecommon.dto.UpdateWorkbookRequestDto;
import lombok.AllArgsConstructor;
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
@Table(name = "Workbook", uniqueConstraints = {
        @UniqueConstraint(name = "Workbook_title", columnNames = {"title"}),
        @UniqueConstraint(name = "Workbook_description", columnNames = {"description"})
})
@Builder
@AllArgsConstructor
public class Workbook  {

    /********************************* PK 필드 *********************************/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workbook_id")
    private Long id;

    /********************************* PK가 아닌 필드 *********************************/
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @CreationTimestamp
    @Column(name = "created_at")
    private final LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "competition_end_time")
    private LocalDateTime competitionEndTime;


    /********************************* 연관관계 매핑 *********************************/

    @OneToMany(
            mappedBy = "workbook",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    List<WorkbookQuestion> questions = new ArrayList<>();

    @OneToMany(
            mappedBy = "workbook",
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST)
    List<File> files = new ArrayList<>();

    @OneToOne(
        mappedBy = "workbook",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL
    )
    private Competition competition;

    /********************************* 빌더 *********************************/

//    @Builder
//    public Workbook(String title, String description, LocalDateTime endTime){
//        this.title = title;
//        this.description = description;
//        this.competitionEndTime = LocalDateTime.now();
//    }

    /********************************* 비즈니스 로직 *********************************/

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
