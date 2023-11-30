package com.cstudy.modulecommon.repository.question;

import com.cstudy.modulecommon.domain.question.Category;
import com.cstudy.modulecommon.error.category.NotFoundCategoryTile;
import com.cstudy.modulecommon.util.DataJpaCustomUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryRepositoryTest extends DataJpaCustomUtil {
    @Test
    public void 카테고리_조회() throws Exception {
        //given
        final String categoryTitle = "네트워크";
        Category category = createCategory(categoryTitle);
        //when
        Category getCategory = categoryRepository.findByTitle(categoryTitle)
                .orElseThrow(() -> new NotFoundCategoryTile(categoryTitle));
        //Then
        assertThat(getCategory.getCategoryTitle()).isEqualTo(categoryTitle);
    }
}