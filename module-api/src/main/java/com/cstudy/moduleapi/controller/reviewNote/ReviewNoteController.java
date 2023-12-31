package com.cstudy.moduleapi.controller.reviewNote;

import com.cstudy.moduleapi.application.reviewNote.ReviewService;
import com.cstudy.moduleapi.config.argumentResolver.IfLogin;
import com.cstudy.moduleapi.dto.review.ReviewUserResponseDto;
import com.cstudy.modulecommon.util.LoginUserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ReviewNote 오답노트", description = "단일 회원의 오답노트")
@RestController
@RequestMapping("/api/review")
public class ReviewNoteController {
    private final ReviewService reviewService;

    public ReviewNoteController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Operation(summary = "오답노트 페이징", description = "단일 회원의 오답노트 조회하기 ROLE_CUSTOM/ ROLE_ADMIN ")
    @GetMapping("/page")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOM', 'ROLE_ADMIN')")
    public Page<ReviewUserResponseDto> findReviewPaging(@Parameter(hidden = true)
                                                        @IfLogin LoginUserDto loginUserDto,
                                                        @PageableDefault Pageable pageable) {
        return reviewService.findReviewPaging(loginUserDto, pageable);
    }


    @Operation(summary = "오답노트 조회", description = "단일 회원의 오답노트 조회하기 ROLE_CUSTOM/ ROLE_ADMIN ")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOM', 'ROLE_ADMIN')")
    public ReviewUserResponseDto findReviewPaging(@Parameter(hidden = true)
                                                  @IfLogin LoginUserDto loginUserDto) {
        return reviewService.findMongoAboutReviewNote(loginUserDto);
    }
}
