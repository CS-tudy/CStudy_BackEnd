package com.cstudy.moduleapi.controller.member;

import com.cstudy.moduleapi.application.member.DuplicateServiceFinder;
import com.cstudy.moduleapi.dto.member.DuplicateResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;


@Tag(name = "회원가입 중복 체크", description = "이메일, 이름 중복에 대한 체크")
@RestController
@RequestMapping("/api")
public class DuplicateController {

    private final DuplicateServiceFinder duplicateServiceFinder;

    public DuplicateController(DuplicateServiceFinder duplicateServiceFinder) {
        this.duplicateServiceFinder = duplicateServiceFinder;
    }

    @Operation(summary = "이메일 중복 체크", description = "Email 중복 체크 / @PermitAll")
    @GetMapping("/email")
    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    public DuplicateResponseDto checkEmailDuplication(@Parameter(name = "email", description = "email 중복 체크")
                                                      @RequestParam(value = "email", required = true) String account,
                                                      HttpServletRequest request) {
        return duplicateServiceFinder.getVerifyResponseDto(request.getParameterNames().nextElement(), account);
    }

    @Operation(summary = "이름 중복 체크", description = "name 중복 체크 / @PermitAll")
    @GetMapping("/name")
    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    public DuplicateResponseDto checkNameDuplication(@Parameter(name = "name", description = "name 중복 체크")
                                                     @RequestParam(value = "name", required = true) String account,
                                                     HttpServletRequest request) {
        return duplicateServiceFinder.getVerifyResponseDto(request.getParameterNames().nextElement(), account);
    }

}
