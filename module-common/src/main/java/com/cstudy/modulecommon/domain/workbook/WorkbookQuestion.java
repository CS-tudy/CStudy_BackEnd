package com.cstudy.modulecommon.domain.workbook;

import com.cstudy.modulecommon.domain.question.Question;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class WorkbookQuestion {
    /********************************* PK 필드 *********************************/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workbook_question_id")
    private Long id;

    /********************************* 연관관계 매핑 *********************************/

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workbook_id")
    private Workbook workbook;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    /********************************* 빌더 *********************************/

    @Builder
    public WorkbookQuestion(Workbook workbook, Question question){
        this.workbook = workbook;
        this.question = question;
    }

}
