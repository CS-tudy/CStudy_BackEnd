package com.cstudy.moduleapi.controller.alarm;

import com.cstudy.moduleapi.application.alarm.AlarmService;
import com.cstudy.moduleapi.application.member.MemberService;
import com.cstudy.moduleapi.config.argumentResolver.IfLogin;
import com.cstudy.moduleapi.config.jwt.util.JwtTokenizer;
import com.cstudy.moduleapi.dto.alarm.response.AlarmResponseDto;
import com.cstudy.modulecommon.error.pathvariable.PositivePatriarchal;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import com.cstudy.modulecommon.util.LoginUserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
@RestControllerAdvice
@RequestMapping("/api/alarm")
@RequiredArgsConstructor
public class AlarmController {

    private final MemberService memberService;
    private final AlarmService alarmService;
    private final JwtTokenizer jwtTokenizer;

    @Operation(summary = "알람 받기", description = "알람 받기")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOM', 'ROLE_ADMIN')")
    public Page<AlarmResponseDto> alarmList(@Parameter(hidden = true) @IfLogin LoginUserDto loginUserDto,
                                            Pageable pageable) {
        return memberService.alarmList(loginUserDto, pageable);
    }

    @Operation(summary = "SSE 연결하기", description = "SSE 연결")
    @GetMapping("subscribe")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOM', 'ROLE_ADMIN')")
    public SseEmitter subscribe(@IfLogin LoginUserDto loginUserDto) {
        return alarmService.connectionAlarm(loginUserDto.getMemberId());
    }

    @Operation(summary = "알람 삭제하게 {id}", description = "알람 삭제하기")
    @DeleteMapping("{alarmId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOM', 'ROLE_ADMIN')")
    public void deleteAlarm(@PathVariable Long alarmId) {
        Optional.of(alarmId)
                .filter(id -> id >= 0)
                .orElseThrow(() -> new PositivePatriarchal(alarmId));
        alarmService.delete(alarmId);
    }
}
