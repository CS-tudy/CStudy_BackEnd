package com.cstudy.moduleapi.controller.member;

import com.cstudy.moduleapi.application.member.DuplicateServiceFinder;
import com.cstudy.moduleapi.dto.member.DuplicateResponseDto;
import com.cstudy.moduleapi.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@Tag(name = "회원가입 중복 체크", description = "이메일, 이름 중복에 대한 체크")
@RestController
public class DuplicateController {

    private final DuplicateServiceFinder duplicateServiceFinder;

    public DuplicateController(DuplicateServiceFinder duplicateServiceFinder) {
        this.duplicateServiceFinder = duplicateServiceFinder;
    }

    @Operation(summary = "이메일 중복 체크", description = "Email 중복 체크")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "중복 없음 - 회원 가입 가능"),
            @ApiResponse(responseCode = "400", description = "중복  - 반환 값 false를 반환하여 회원 가입 불가능", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/email")
    @ResponseStatus(HttpStatus.OK)
    public DuplicateResponseDto checkEmailDuplication(
            @Parameter(name = "email", description = "email 중복 체크")
            @RequestParam(value = "account", required = true) String account,
            HttpServletRequest request
    ) {
        return duplicateServiceFinder.getVerifyResponseDto(request.getParameterNames().nextElement(), account);
    }

    @Operation(summary = "이름 중복 체크", description = "name 중복 체크")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "중복 없음 - 회원 가입 가능"),
            @ApiResponse(responseCode = "400", description = "중복  - 반환 값 false를 반환하여 회원 가입 불가능", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/name")
    @ResponseStatus(HttpStatus.OK)
    public DuplicateResponseDto checkNameDuplication(
            @Parameter(name = "name", description = "name 중복 체크")
            @RequestParam(value = "account", required = true) String account,
            HttpServletRequest request
    ) {
        return duplicateServiceFinder.getVerifyResponseDto(request.getParameterNames().nextElement(), account);
    }

}
