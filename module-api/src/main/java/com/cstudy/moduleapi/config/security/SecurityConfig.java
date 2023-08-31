package com.cstudy.moduleapi.config.security;


import com.cstudy.moduleapi.config.jwt.exception.CustomAccessDeniedHandler;
import com.cstudy.moduleapi.config.jwt.exception.CustomAuthenticationEntryPoint;
import com.cstudy.moduleapi.config.oauth.CustomOAuth2UserService;
import com.cstudy.moduleapi.config.oauth.OAuth2FailureHandler;
import com.cstudy.moduleapi.config.oauth.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, proxyTargetClass = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationManagerConfig authenticationManagerConfig;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler successHandler;
    private final OAuth2FailureHandler failureHandler;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .csrf().disable()
                .cors()
                .and()
                .apply(authenticationManagerConfig)
                .and()
                .httpBasic().disable()
                .authorizeRequests()

                //swagger
                .antMatchers("/v2/api-docs").permitAll()
                .antMatchers("/swagger-resources").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/configuration/ui").permitAll()
                .antMatchers("/configuration/security").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/v3/api-docs/**").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()

                //cors preflight
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // MemberController
                .antMatchers(HttpMethod.POST, "/api/member/signup").permitAll()//회원가입
                .antMatchers(HttpMethod.POST, "/api/member/login").permitAll()// 로그인
                .antMatchers(HttpMethod.POST, "/api/member/refreshToken").permitAll()// 재발급

                .antMatchers(HttpMethod.GET, "/api/member/email/send").permitAll() // 이메일 전송
                .antMatchers(HttpMethod.GET, "/api/member/upload").authenticated() //이미지 업로드
                .antMatchers(HttpMethod.GET, "/api/member/download").authenticated() // 다운로드
                .antMatchers(HttpMethod.GET, "/api/member/member").authenticated() // 마이페이지
                .antMatchers(HttpMethod.GET, "/api/email").authenticated() //이메일 중복검사
                .antMatchers(HttpMethod.GET, "/api/name").authenticated() // 닉네임 중복검사

                .antMatchers(HttpMethod.PUT, "/api/member/member").authenticated() // 비밀번호 변경
                .antMatchers(HttpMethod.DELETE, "/api/member/logout").authenticated() //로그아웃

                //CommentController
                .antMatchers(HttpMethod.GET, "/api/comment/*").permitAll() // 계층형 댓글 조회
                .antMatchers(HttpMethod.POST, "/api/comment").authenticated() // 댓글 생성하기
                .antMatchers(HttpMethod.DELETE, "/api/comment/*").authenticated() // 계층형 댓글 삭제

                //NoticeController
                .antMatchers(HttpMethod.GET, "/api/notice").permitAll() //공지사항 조회
                .antMatchers(HttpMethod.POST, "/api/notice").authenticated() //공지사항 생성
                .antMatchers(HttpMethod.PUT, "/api/notice/*").authenticated() //공지사항 수정
                .antMatchers(HttpMethod.DELETE, "/api/notice/*").authenticated() //공지사항 삭제

                //QuestionController
                .antMatchers(HttpMethod.GET, "/api/questions").permitAll() // 전체 문제 탐색하게 페이징 처리
                .antMatchers(HttpMethod.GET, "/api/questions/*").permitAll() // 단일 문제 탐색하기
                .antMatchers(HttpMethod.GET, "/api/questions/my-questions").authenticated() // 내가 해결한 문제 조회하기
                .antMatchers(HttpMethod.POST, "/api/questions").authenticated()// 관리자가 문제 생성하기
                .antMatchers(HttpMethod.POST, "/api/questions/bulk").authenticated() // 관리자가 대량 문제 생성하기
                .antMatchers(HttpMethod.POST, "/api/questions/*/answer").authenticated() // 단일 문제에 대한 정답 선택하기

                //RankingController
                .antMatchers(HttpMethod.GET, "/api/rank").permitAll() // 전체 랭킹 조회하기

                //RequestController
                .antMatchers(HttpMethod.GET, "/api/request/*").permitAll()  // id를 기반해서 단일 게시판 글 조회
                .antMatchers(HttpMethod.GET, "/api/request/requests").permitAll() // 게시판 전체 리스트 조회 페이징
                .antMatchers(HttpMethod.GET, "/api/request/member").authenticated() // 내가 요청한 문제 조회
                .antMatchers(HttpMethod.POST, "/api/request").authenticated()  // 회원 문제 요청 생성
                .antMatchers(HttpMethod.PUT, "/api/request/approve").authenticated() //관리자가 요청 문제 승인
                .antMatchers(HttpMethod.PUT, "/api/request").authenticated() // 회원이 작성한 문제 요청 변경
                .antMatchers(HttpMethod.DELETE, "/api/request/*").authenticated() // 회원이 게시판 글 삭제

                //WorkbookController
                .antMatchers(HttpMethod.GET, "/api/workbook/**").permitAll() // 특정 GET 요청은 모든 사용자에게 허용
                .antMatchers(HttpMethod.POST, "/api/workbook").authenticated() // POST 요청은 ROLE_ADMIN 권한 필요
                .antMatchers(HttpMethod.POST, "/api/workbook/questions").authenticated()
                .antMatchers(HttpMethod.PUT, "/api/workbook").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/workbook").authenticated()


                // CompetitionScoreController
                .antMatchers(HttpMethod.GET, "/api/competitions/active").permitAll()
                .antMatchers(HttpMethod.GET, "/api/competitions/finished").permitAll()
                .antMatchers(HttpMethod.GET, "/api/competitions/{competitionId}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/competitions/{competitionId}/member/rank").authenticated()
                .antMatchers(HttpMethod.POST, "/api/competitions").authenticated()
                .antMatchers(HttpMethod.POST, "/api/competitions/{competitionId}/join").authenticated()
                .antMatchers(HttpMethod.GET, "/api/competitions/{competitionId}/questions").authenticated()
                .antMatchers(HttpMethod.POST, "/api/competitions/questions/add").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/competitions/questions/delete").authenticated()
                .antMatchers(HttpMethod.GET, "/api/competitions/{competitionId}/ranks").authenticated()
                .antMatchers(HttpMethod.POST, "/api/competitions/scores/submit").authenticated()
                .antMatchers(HttpMethod.GET, "/api/competitions/scores/{competitionId}").authenticated()

                //ReviewNoteController
                .antMatchers(HttpMethod.GET, "/api/review").authenticated()

                .anyRequest().permitAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)
                .and()
                .oauth2Login()
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .userInfoEndpoint()
                .userService(oAuth2UserService);



        return http.build();
    }
}