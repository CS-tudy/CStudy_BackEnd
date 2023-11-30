package com.cstudy.moduleapi.controller.category;

import com.cstudy.moduleapi.dto.category.CategoryRequestDto;
import com.cstudy.modulecommon.domain.question.Category;
import com.cstudy.modulecommon.repository.question.CategoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.List;

@Tag(name = "카테고리 API", description = "카테고리 생성, 조회 api")
@Slf4j
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Operation(summary = "카테고리 전체 조회", description = "카테고리 전체 조회 @permitALL")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PermitAll
    public List<Category> getCategory() {
        return categoryRepository.findAll();
    }

    @Operation(summary = "카테고리 생성", description = "관리자만 카테고리 생성")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public void createCategory(@RequestBody @Valid CategoryRequestDto categoryRequestDto) {
        categoryRepository.save(Category.builder()
                .categoryTitle(categoryRequestDto.getCategoryName())
                .build());
    }
}
