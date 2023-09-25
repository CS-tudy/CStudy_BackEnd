package com.cstudy.moduleapi.controller.member;


import com.cstudy.moduleapi.application.member.FileService;
import com.cstudy.moduleapi.application.member.MemberService;
import com.cstudy.moduleapi.application.refershToken.RefreshTokenService;
import com.cstudy.moduleapi.config.argumentResolver.IfLogin;
import com.cstudy.moduleapi.dto.member.*;
import com.cstudy.moduleapi.dto.refresh.RefreshTokenDto;
import com.cstudy.modulecommon.util.LoginUserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.PermitAll;
import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;


@Tag(name = "Member(회원 API)", description = "회원 관련 API(회원가입, 로그인, 로그아웃, 재할당)")
@Slf4j
@RestController
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;
    private final RefreshTokenService refreshTokenService;
    private final FileService fileService;
//    private final GeoService geoService;

    public MemberController(
            MemberService memberService,
            RefreshTokenService refreshTokenService,
            FileService fileService
//            GeoService geoService
    ) {
        this.memberService = memberService;
        this.refreshTokenService = refreshTokenService;
        this.fileService = fileService;
//        this.geoService = geoService;
    }

    @Operation(summary = "회원가입", description = "Email, Password, Name을 이용하여 회원가입을 합니다. / @PermitAll")
    @PostMapping("signup")
    @PermitAll
    @ResponseStatus(HttpStatus.CREATED)
    public void signUpWithRole(@Parameter(name = "MemberSignupRequest", description = "회원 이메일, 비밀번호, 이름")
                               @Valid @RequestBody MemberSignupRequest request) {
        //todo : 회원 아이피
//        GeoLocationDto location = geoService.findLocation(getIpAddress());
        log.info(String.format("request:>>{%s}", request));
        memberService.signUp(request);
    }


    @Operation(summary = "로그인", description = "회원 로그인 jwt, refresh Token을 발급한다. / @PermitAll")
    @PostMapping("login")
    @PermitAll
    @ResponseStatus(HttpStatus.CREATED)
    public MemberLoginResponse login(@Parameter(name = "MemberLoginRequest", description = "회원 이메일, 비밀번호")
                                     @Valid @RequestBody MemberLoginRequest request) {
        log.info(String.format("request:>>{%s}", request));
        return memberService.login(request);
    }

    @Operation(summary = "로그아웃", description = "Local Storage를 access Token을 삭제하고 redis에 refresh Token을 삭제합니다. / ROLE_CUSTOM', 'ROLE_ADMIN")
    @DeleteMapping("logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOM', 'ROLE_ADMIN')")
    public void logout(@Parameter(name = "RefreshTokenDto", description = "refresh Token")
                       @Valid @RequestBody RefreshTokenDto refreshTokenDto) {
        String refreshToken = refreshTokenDto.getRefreshToken();
        log.info(String.format("Refresh Token:>>{%s}", refreshToken));
        refreshTokenService.deleteRefreshToken(refreshToken);
    }

    @Operation(summary = "재발급", description = "access Token 만료되면 refresh Token을 이용하여 재발급을 합니다. /permitAll")
    @PostMapping("refreshToken")
    @ResponseStatus(HttpStatus.OK)
    @PermitAll
    public MemberLoginResponse refreshTokenWithAccessToken(@Parameter(name = "RefreshTokenDto", description = "refresh Token")
                                                           @Valid @RequestBody RefreshTokenDto refreshTokenDto) {
        log.info(String.format("Refresh Token:>>{%s}", refreshTokenDto));
        return refreshTokenService.AccessTokenWithRefreshToken(refreshTokenDto);
    }

    @Operation(summary = "S3 파일 업로드", description = "AWS S3 버켓에 IAM 파일 업로드 /ROLE_CUSTOM', 'ROLE_ADMIN")
    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOM', 'ROLE_ADMIN')")
    public void upload(@Parameter(name = "multipartFileList", description = "Multi part file")
                       @RequestParam("files") MultipartFile multipartFile,
                       @Parameter(hidden = true)
                       @IfLogin LoginUserDto loginUserDto) {
        fileService.uploadFiles(multipartFile, loginUserDto);
    }


    @Operation(summary = "S3 버켓에서 사진 가져오기", description = "버켓을 기준으로 업로드 회원 사진 가져오기 / ROLE_CUSTOM', 'ROLE_ADMIN")
    @GetMapping("/download")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOM', 'ROLE_ADMIN')")
    public ResponseEntity<byte[]> downloadFile(@Parameter(hidden = true)
                                               @IfLogin LoginUserDto loginUserDto) {
        byte[] imageBytes = fileService.getImageBytes(loginUserDto);
        // 이미지 데이터를 반환
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageBytes);
    }

    @Operation(summary = "마이페이지", description = "마이페이지 / ROLE_CUSTOM', 'ROLE_ADMIN")
    @GetMapping("/member")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOM', 'ROLE_ADMIN')")
    public MyPageResponseDto myPage(@Parameter(hidden = true)
                                    @IfLogin LoginUserDto loginUserDto) {
        return memberService.getMyPage(loginUserDto.getMemberId());
    }

    @Operation(summary = "비밀번호 수정", description = "비밀번호 수정 / ROLE_CUSTOM', 'ROLE_ADMIN")
    @PatchMapping("/member")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOM', 'ROLE_ADMIN')")
    public void changePassword(@Parameter(name = "memberPasswordChangeRequest", description = "회원 이전 비밀번호, 새로운 비밀번호")
                               @Valid @RequestBody MemberPasswordChangeRequest memberPasswordChangeRequest,
                               @Parameter(hidden = true) @IfLogin LoginUserDto loginUserDto) {
        memberService.changePassword(memberPasswordChangeRequest, loginUserDto.getMemberId());
    }


    @Operation(summary = "회원 가입시 이메인 인증", description = "기존사용하고 있는 이메일을 통해 인증 /@PermitAll")
    @PermitAll
    @GetMapping("/email/send")
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<String> sendEmail(@Parameter(name = "to", description = "전송을 원하는 이메일 주소")
                                               String to) throws MessagingException {
        return memberService.sendEmail(to);
    }
}
