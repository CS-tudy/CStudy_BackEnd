package com.cstudy.moduleapi.application.member.impl;

import com.cstudy.moduleapi.application.member.DuplicateServiceFinder;
import com.cstudy.moduleapi.application.member.MemberService;
import com.cstudy.moduleapi.application.refershToken.RefreshTokenService;
import com.cstudy.moduleapi.application.reviewNote.ReviewService;
import com.cstudy.moduleapi.config.jwt.util.JwtTokenizer;
import com.cstudy.moduleapi.dto.member.*;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.role.Role;
import com.cstudy.modulecommon.domain.role.RoleEnum;
import com.cstudy.modulecommon.error.member.*;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import com.cstudy.modulecommon.repository.role.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenizer jwtTokenizer;
    private final JavaMailSender javaMailSender;
    private final RefreshTokenService refreshTokenService;
    private final DuplicateServiceFinder duplicateServiceFinder;
    private final ReviewService reviewNoteService;

    @Value("${spring.mail.username}")
    private  String EMAIL;

    public MemberServiceImpl(
            MemberRepository memberRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenizer jwtTokenizer,
            JavaMailSender javaMailSender,
            RefreshTokenService refreshTokenService,
            DuplicateServiceFinder duplicateServiceFinder,
            ReviewService reviewNoteService
    ) {
        this.memberRepository = memberRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenizer = jwtTokenizer;
        this.javaMailSender = javaMailSender;
        this.refreshTokenService = refreshTokenService;
        this.duplicateServiceFinder = duplicateServiceFinder;
        this.reviewNoteService = reviewNoteService;
    }

    /**
     * Returns the MemberSignupResponse With request
     *
     * @param request 회원가입 Signup
     * @return 회원가입 성공을 하면 이메일, 이름을 리턴을 합니다.
     * @throws EmailDuplication 중복된 이메일 회원가입 요청
     */
    @Override
    @Transactional
    public MemberSignupResponse signUp(
            MemberSignupRequest request
    ) {
        checkEmailAndNameDuplication(request);

        signupWithRole(Member.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .roles(new HashSet<>())
                .build());

        reviewNoteService.createUserWhenSignupSaveMongodb(request.getName());

        return MemberSignupResponse.of(memberRepository.save(Member.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .roles(new HashSet<>())
                .build()));
    }

    private void checkEmailAndNameDuplication(MemberSignupRequest request) {
        divisionDuplicationAboutNickEmail(request);
        divisionDuplicationAboutName(request);
    }

    private void divisionDuplicationAboutName(MemberSignupRequest request) {
        DuplicateResponseDto name = duplicateServiceFinder.getVerifyResponseDto("name", request.getName());
        Optional.of(name)
                .filter(duplicateResponseDto -> duplicateResponseDto.getVerify().equals(DuplicateResult.FALSE.getDivisionResult()))
                .ifPresent(duplicateResponseDto -> {
                    throw new NameDuplication("중복 이름");
                });
    }

    private void divisionDuplicationAboutNickEmail(MemberSignupRequest request) {
        DuplicateResponseDto email = duplicateServiceFinder.getVerifyResponseDto("email", request.getEmail());
        Optional.of(email)
                .filter(duplicateResponseDto -> duplicateResponseDto.getVerify().equals(DuplicateResult.FALSE.getDivisionResult()))
                .ifPresent(duplicateResponseDto -> {
                    throw new EmailDuplication("중복 이메일");
                });
    }

    /**
     * Returns login member with LoginRequest
     *
     * @param request 회원 로그인 Request Dto
     * @return 로그인 성공하면 회원 아이디, JWT(Access, Refresh Token)을 리턴을 합니다.
     */
    @Override
    @Transactional
    public MemberLoginResponse login(MemberLoginRequest request) {

        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundMemberEmail(request.getEmail()));

        Optional.of(request.getPassword())
                .filter(password -> passwordEncoder.matches(password, member.getPassword()))
                .orElseThrow(() -> new InvalidMatchPasswordException(request.getPassword()));

        return createToken(member);
    }

    /**
     * oauthSignUp
     *
     * @param email 회원 이름
     * @param name  회원 이름
     */
    @Override
    @Transactional
    public Member oauthSignUp(String email, String name) {

        Member member = Member.builder()
                .email(email)
                .name(name)
                .roles(new HashSet<>())
                .build();

        signupWithRole(member);

        memberRepository.save(member);

        return member;
    }

    /**
     * Returns login member with LoginRequest
     *
     * @param email 회원 이메일
     * @return 로그인 성공하면 회원 아이디, JWT(Access, Refresh Token)을 리턴을 합니다.
     */
    @Override
    @Transactional(readOnly = true)
    public MemberLoginResponse oauthLogin(String email) {
        return createToken(memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundMemberEmail(email)));
    }

    @Override
    @Transactional(readOnly = true)
    public MyPageResponseDto getMyPage(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundMemberId(id));
        return MyPageResponseDto.of(member);
    }

    @Override
    @Transactional
    public void changePassword(MemberPasswordChangeRequest request, Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundMemberId(id));

        if (!passwordEncoder.matches(request.getOldPassword(), member.getPassword())) {
            throw new InvalidMatchPasswordException(request.getOldPassword());
        }

        String newPassword = passwordEncoder.encode(request.getNewPassword());
        member.changePassword(newPassword);
    }

    //todo : 반환 타입을 Future로 설정 / 학습
    @Async
    @Override
    @Transactional
    public String sendEmail(String recipientEmail) throws MailException, MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        String key = createKey();

        helper.setFrom(EMAIL);
        helper.setTo(recipientEmail);
        helper.setSubject("회원가입 코드 메일");
        helper.setText(emailHtml(key), true);

        javaMailSender.send(message);
        return key;
    }

    @NotNull
    private static String emailHtml(String key) {
        String msgg = "<div style='margin:100px;'>";
        msgg += "<h1>안녕하세요 CS;tudy입니다!!!</h1>";
        msgg += "<br>";
        msgg += "<p>아래 코드를 회원가입 창으로 돌아가 입력해주세요<p>";
        msgg += "<br>";
        msgg += "<p>감사합니다!<p>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<h3 style='color:blue;'>회원가입 코드입니다.</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "CODE : <strong>";
        msgg += key + "</strong><div><br/> ";
        msgg += "</div>";
        return msgg;
    }


    private void signupWithRole(Member member) {
        Optional<Role> userRole = roleRepository.findByName(RoleEnum.CUSTOM.getRoleName());
        userRole.ifPresent(member::changeRole);
    }

    private void duplicationWithEmail(MemberSignupRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new EmailDuplication(request.getEmail());
        }
    }

    private MemberLoginResponse createToken(Member member) {

        List<String> roles = member.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        String accessToken = jwtTokenizer.createAccessToken(
                member.getId(),
                member.getEmail(),
                roles
        );

        String refreshToken = jwtTokenizer.createRefreshToken(
                member.getId(),
                member.getEmail(),
                roles
        );

        refreshTokenService.addRefreshToken(refreshToken);
        return MemberLoginResponse.of(member, accessToken, refreshToken);
    }

    private String createKey() {
        StringBuilder key = new StringBuilder();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) {
            int index = rnd.nextInt(3);

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    break;
            }
        }

        return key.toString();
    }
}
