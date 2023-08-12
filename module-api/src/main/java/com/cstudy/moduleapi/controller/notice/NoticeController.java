package com.cstudy.moduleapi.controller.notice;

import com.cstudy.moduleapi.application.notice.NoticeService;
import com.cstudy.modulecommon.dto.NoticeResponseDto;
import com.cstudy.moduleapi.dto.notice.NoticeSaveRequestDto;
import com.cstudy.modulecommon.dto.NoticeSearchRequestDto;
import com.cstudy.modulecommon.dto.NoticeUpdateRequestDto;
import com.cstudy.moduleapi.util.IfLogin;
import com.cstudy.modulecommon.util.LoginUserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "공지사항 API", description = "공지사항 생성, 조회 API")
@Slf4j
@RestController
@RequestMapping("/api/notice")
public class NoticeController {

    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @Operation(summary = "공지사항 조회 페이징", description = "공지사항 조회 페이징 처리")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<NoticeResponseDto> findNoticeWithPage(
            @Parameter(name = "page", description = "페이지")
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @Parameter(name = "size", description = "기본 default 10 , size")
            @RequestParam(value = "size", defaultValue = "10", required = false) int size,
            @Parameter(name = "noticeSearchRequestDto", description = "공지사항 조회 조건")
            NoticeSearchRequestDto noticeSearchRequestDto
    ) {
        return noticeService.findNoticePage(size, page, noticeSearchRequestDto);
    }

    @Operation(summary = "공지사항 생성", description = "공지사항 생성")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveNotice(
            @IfLogin LoginUserDto loginUserDto,
            @Parameter(name = "NoticeSaveRequestDto", description = "공지사항 생성 DTO")
            @Valid @RequestBody NoticeSaveRequestDto noticeSaveRequestDto
    ) {
        noticeService.saveNotice(noticeSaveRequestDto, loginUserDto);
    }

    @Operation(summary = "공지사항 UPDATE", description = "공지사항 수정")
    @PutMapping("/{noticeId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateNotice(
            @Parameter(name = "noticeId", description = "공지사항 아이디")
            @PathVariable Long noticeId,
            @Parameter(name = "NoticeUpdateRequestDto", description = "공지사항 update dto")
            @RequestBody NoticeUpdateRequestDto noticeUpdateRequestDto,
            @IfLogin LoginUserDto loginUserDto
    ) {
        noticeService.updateNotice(noticeId, noticeUpdateRequestDto, loginUserDto);
    }

    @Operation(summary = "공지사항 삭제", description = "공지사항 삭제")
    @DeleteMapping("/{noticeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNotice(
            @Parameter(name = "noticeId", description = "공지사항 아이디")
            @PathVariable Long noticeId,
            @IfLogin LoginUserDto loginUserDto
    ) {
        noticeService.deleteNotice(noticeId, loginUserDto);
    }
}
