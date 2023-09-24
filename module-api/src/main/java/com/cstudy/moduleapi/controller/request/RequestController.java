package com.cstudy.moduleapi.controller.request;

import com.cstudy.moduleapi.application.request.RequestService;
import com.cstudy.moduleapi.config.argumentResolver.IfLogin;
import com.cstudy.moduleapi.dto.request.CreateRequestRequestDto;
import com.cstudy.moduleapi.dto.request.FlagRequestDto;
import com.cstudy.moduleapi.dto.request.RequestResponseDto;
import com.cstudy.modulecommon.dto.UpdateRequestRequestDto;
import com.cstudy.modulecommon.error.pathvariable.PositivePatriarchal;
import com.cstudy.modulecommon.util.LoginUserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.Optional;

@Tag(name = "Request(게시판 API)", description = "게시판 관련 API(게시판 조회, 생성)")
@RestController
@RequestMapping("/api/request")
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @Operation(summary = "게시판 문제 요청글 생성", description = "게시판 문제 요청글 생성. / ROLE_CUSTOM ")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOM')")
    public void createRequest(@Parameter(name = "CreateRequestRequestDto", description = "title: 게시글 제목, description: 게시글 내용")
                              @Valid @RequestBody CreateRequestRequestDto requestDto,
                              @Parameter(hidden = true)
                              @IfLogin LoginUserDto loginUser) {
        requestService.createRequest(requestDto, loginUser.getMemberId());
    }

    @Operation(summary = "게시글 상태 수정", description = "게시판 상태를 대기에서 승인으로 변경. / ROLE_ADMIN ")
    @PatchMapping("/approve")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public void updateFlag(@Parameter(description = "id: 게시글 id, flag: 승인-true, 대기-false")
                           @Valid @RequestBody FlagRequestDto flagDto,
                           @Parameter(hidden = true)
                           @IfLogin LoginUserDto loginUser) {
        if (loginUser.getRoles().contains("ROLE_ADMIN")) {
            requestService.updateFlag(flagDto);
        }
    }

    @PermitAll
    @Operation(summary = "게시판 글 조회", description = "게시글 id를 이용해 게시글을 조회 / PermitAll")
    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public RequestResponseDto getRequest(@Parameter(description = "게시글 id", name = "requestId")
                                         @PathVariable Long requestId) {
        Optional.of(requestId)
                .filter(id -> id >= 0)
                .orElseThrow(() -> new PositivePatriarchal(requestId));
        return requestService.getRequest(requestId);
    }

    @Operation(summary = "내가 요청한 문제 조회", description = "내가 요청한 문제 조회 / ROLE_CUSTOM', 'ROLE_ADMIN")
    @GetMapping("/member")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOM', 'ROLE_ADMIN')")
    public Page<RequestResponseDto> getMemberRequestList(@Parameter(description = "page: 페이지 번호, size: 한 페이지 문제 수.")
                                                         @PageableDefault(sort = {"createdAt"}, direction = Direction.DESC) Pageable pageable,
                                                         @Parameter(hidden = true)
                                                         @IfLogin LoginUserDto loginUser) {
        return requestService.getRequestList(loginUser.getMemberId(), pageable);
    }

    @Operation(summary = "게시판 전체 리스트 조회", description = "게시판 전체 리스트 조회 / PermitAll")
    @GetMapping("/requests")
    @ResponseStatus(HttpStatus.OK)
    @PermitAll
    public Page<RequestResponseDto> getRequestList(@Parameter(description = "page: 페이지 번호, size: 한 페이지 문제 수.")
                                                   @PageableDefault(sort = {"createdAt"}, direction = Direction.DESC) Pageable pageable) {
        return requestService.getRequestList(pageable);
    }

    @Operation(summary = "게시판 업데이트", description = "단일 게시판 업데이트 / ROLE_CUSTOM', 'ROLE_ADMIN")
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOM', 'ROLE_ADMIN')")
    public void updateRequest(@Parameter(description = "updateRequestRequestDto", name = "게시판 update dto")
                              @Valid @RequestBody UpdateRequestRequestDto updateRequestRequestDto,
                              @Parameter(hidden = true)
                              @IfLogin LoginUserDto loginUserDto) {
        requestService.updateRequest(updateRequestRequestDto, loginUserDto);
    }

    @Operation(summary = "게시판 삭제", description = "단일 게시판 삭제 / ROLE_CUSTOM', 'ROLE_ADMIN'")
    @DeleteMapping("/{requestId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOM', 'ROLE_ADMIN')")
    public void deleteRequestById(@Parameter(description = "게시판 아이디", name = "단일 게시판 아이디")
                                  @PathVariable Long requestId,
                                  @Parameter(hidden = true)
                                  @IfLogin LoginUserDto loginUserDto) {
        Optional.of(requestId)
                .filter(id -> id >= 0)
                .orElseThrow(() -> new PositivePatriarchal(requestId));
        requestService.deleteTodoById(requestId, loginUserDto);
    }
}
