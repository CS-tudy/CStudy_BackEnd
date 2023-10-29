package com.cstudy.moduleapi.application.member.impl;

import com.cstudy.moduleapi.application.member.DuplicateServiceFinder;
import com.cstudy.moduleapi.application.member.EmailComponent;
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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
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
    private final EmailComponent emailComponent;
    private final StringRedisTemplate redisTemplate;

    @Value("${spring.mail.username}")
    private String EMAIL;
    private final static String RANKING_KEY = "MemberRank";

    public MemberServiceImpl(MemberRepository memberRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtTokenizer jwtTokenizer, JavaMailSender javaMailSender, RefreshTokenService refreshTokenService, DuplicateServiceFinder duplicateServiceFinder, ReviewService reviewNoteService, EmailComponent emailComponent, StringRedisTemplate redisTemplate) {
        this.memberRepository = memberRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenizer = jwtTokenizer;
        this.javaMailSender = javaMailSender;
        this.refreshTokenService = refreshTokenService;
        this.duplicateServiceFinder = duplicateServiceFinder;
        this.reviewNoteService = reviewNoteService;
        this.emailComponent = emailComponent;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 유저의 회원가입
     * 유저를 생성하고 다음과 같은 로직을 실행을 합니다.
     * 1. 유저를 생성한다.
     * 2. 유저에 대한 권한을 생성한다.
     * 3.  MongoDB의 ReviewUser를 생성한다.
     * <p>
     * 다음과 같은 체크를 한다.
     * 1. 이메일에 대한 중복을 체크한다.
     * 2. 이메일에 대한 로직을 체크한다.
     *
     * @param request 회원가입 Signup
     * @return 회원가입 성공을 하면 이메일, 이름을 리턴을 합니다.
     * @throws EmailDuplication 중복된 이메일 회원가입 요청
     */
    @Override
    @Transactional
    public MemberSignupResponse signUp(
            MemberSignupRequest request
//            GeoLocationDto geoLocationDto
    ) {
        checkEmailAndNameDuplication(request);

        Member member = Member.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .roles(new HashSet<>())
                .build();

        signupWithRole(member);
        Member savedMember = memberRepository.save(member);


        reviewNoteService.createUserWhenSignupSaveMongodb(request.getName());

        saveToRedisAsync(savedMember);
        return MemberSignupResponse.of(savedMember);
    }


    @Async
    public void saveToRedisAsync(Member member) {
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        zSetOps.add(RANKING_KEY, member.getName(), 0);
    }

    /**
     * 테스트 코드를 작성하면서 호원가입에 관한에서 중복을 막기 위한 로직의 불편함으로 새롭게 작성
     * 물론 mock을 통해서 문제를 해결할 수 있지만 관련 mock을 작성하기 너무 많기 때문에 JUnit을 사용하는 classic 방식에서
     * 이 문제를 해결할 수 있는 방식을 찾아보는게 좋겠음
     */
    @Override
    @Transactional
    public MemberSignupResponse signUpForTest(MemberSignupRequest request) {
        return getMemberSignupResponse(request);
    }


    /**
     * 회원 로그인을 처리한다. 이때 비밀번호를 일치하는지 확인하고 만약에 일치하지 않으면 exception이 발생한다.
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
     * 마이페이지를 조회한다. 이때 로그인한 회원의 id를 기반하여 조회를 한다.
     */
    @Override
    @Transactional(readOnly = true)
    public MyPageResponseDto getMyPage(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundMemberId(id));
        return MyPageResponseDto.of(member);
    }

    /**
     * 비밀번호를 변경한다.
     *
     * @param request
     * @param id
     */
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
    public CompletableFuture<String> sendEmail(String recipientEmail) throws MailException, MessagingException {
        CompletableFuture<String> future = new CompletableFuture<>();

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        String key = emailComponent.createKey();

        helper.setFrom(EMAIL);
        helper.setTo(recipientEmail);
        helper.setSubject("회원가입 코드 메일");
        helper.setText(emailComponent.emailHtml(key), true);

        try {
            javaMailSender.send(message);
            future.complete(key);
        } catch (Exception e) {
            future.completeExceptionally(e);
        }

        return future;
    }

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

    private void signupWithRole(Member member) {
        Optional<Role> userRole = roleRepository.findByName(RoleEnum.CUSTOM.getRoleName());
        userRole.ifPresent(member::changeRole);
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

    private void checkEmailAndNameDuplication(MemberSignupRequest request) {
        DuplicateResponseDto email = duplicateServiceFinder.getVerifyResponseDto("email", request.getEmail());
        Optional.of(email)
                .filter(duplicateResponseDto -> duplicateResponseDto.getVerify().equals(DuplicateResult.FALSE.getDivisionResult()))
                .ifPresent(duplicateResponseDto -> {
                    throw new EmailDuplication("중복 이메일");
                });
        DuplicateResponseDto name = duplicateServiceFinder.getVerifyResponseDto("name", request.getName());
        Optional.of(name)
                .filter(duplicateResponseDto -> duplicateResponseDto.getVerify().equals(DuplicateResult.FALSE.getDivisionResult()))
                .ifPresent(duplicateResponseDto -> {
                    throw new NameDuplication("중복 이름");
                });
    }

    private MemberSignupResponse getMemberSignupResponse(MemberSignupRequest request) {
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
}
