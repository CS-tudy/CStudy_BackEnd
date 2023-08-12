package com.cstudy.modulecommon.domain.question;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "Category", indexes = {
        @Index(name = "idx_category_title", columnList = "category_title")
})
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "category_title")
    private String categoryTitle;

    @OneToMany(mappedBy = "category")
    Set<Question>questions = new HashSet<>();

    @Builder
    public Category(Long id, String categoryTitle, Set<Question> questions) {
        this.id = id;
        this.categoryTitle = categoryTitle;
        this.questions = questions;
    }
}
