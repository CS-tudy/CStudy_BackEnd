package com.cstudy.modulecommon.repository.question;

import com.cstudy.modulecommon.domain.question.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(
            "select c from Category c" +
            " where c.categoryTitle =:title"
    )
    Optional<Category> findByTitle(@Param("title") String title);

}
