package com.cstudy.moduleapi.aop;

import com.cstudy.modulecommon.dto.UpdateRequestRequestDto;
import com.cstudy.modulecommon.error.request.NotMathRequestAuthCheckAop;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import com.cstudy.modulecommon.repository.request.RequestRepository;
import com.cstudy.modulecommon.util.LoginUserDto;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class AuthCheckAOP {

    private final RequestRepository requestRepository;
    private final MemberRepository memberRepository;

    public AuthCheckAOP(RequestRepository requestRepository, MemberRepository memberRepository) {
        this.requestRepository = requestRepository;
        this.memberRepository = memberRepository;
    }

    @Around("@annotation(com.cstudy.moduleapi.aop.AuthCheck)")
    public Object checkTodoAuthority(ProceedingJoinPoint joinPoint) throws Throwable {

        Object[] args = joinPoint.getArgs();

        if (args.length > 1 && args[1] instanceof LoginUserDto) {
            long id;
            if (args[0] instanceof Long) {
                id = (Long) args[0];
                log.info("Long Id :{}", id);
            } else if (args[0] instanceof UpdateRequestRequestDto) {
                id = ((UpdateRequestRequestDto) args[0]).getId();
                log.info("UpdateTodoRequestDto:{}", id);
            } else {
                throw new IllegalArgumentException("잘못된 메소드 인자입니다.");
            }

            LoginUserDto loginUserDto = (LoginUserDto) args[1];
            log.info("LoginUserDto:{}", loginUserDto.getMemberId());


            if (loginUserDto.getMemberId() == 1) {
                return joinPoint.proceed();
            }

            if (requestRepository.findByIdAndMemberId(id, loginUserDto.getMemberId()).isEmpty()) {
                log.info("권한 체크 error 발생");
                throw new NotMathRequestAuthCheckAop(id);
            }
        }
        return joinPoint.proceed();
    }
}
