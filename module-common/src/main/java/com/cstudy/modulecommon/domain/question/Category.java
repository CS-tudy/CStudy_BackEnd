package com.cstudy.modulecommon.domain.question;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor

@Table(name = "Category",
        indexes = {@Index(name = "idx_category_title", columnList = "categoryTitle")},
        uniqueConstraints = {@UniqueConstraint(name = "categoryTitle", columnNames = {"categoryTitle"})})
public class Category {

    /********************************* PK 필드 *********************************/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    /********************************* PK가 아닌 필드 *********************************/

    @Column(nullable = false)
    private String categoryTitle;

    /********************************* 연관관계 매핑 *********************************/
//    @JsonBackReference
    @OneToMany(mappedBy = "category")
    Set<Question> questions = new HashSet<>();

    /********************************* 빌더 *********************************/
    @Builder
    public Category(Long id, String categoryTitle, Set<Question> questions) {
        this.id = id;
        this.categoryTitle = categoryTitle;
        this.questions = questions;
    }
}
