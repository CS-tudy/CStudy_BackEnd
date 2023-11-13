package com.cstudy.moduleapi.application.request.impl;

import com.cstudy.moduleapi.aop.AuthCheck;
import com.cstudy.moduleapi.application.alarm.AlarmService;
import com.cstudy.moduleapi.application.member.MemberLoadComponent;
import com.cstudy.moduleapi.application.request.RequestService;
import com.cstudy.moduleapi.config.redis.RedisPublisher;
import com.cstudy.moduleapi.dto.request.CreateRequestRequestDto;
import com.cstudy.moduleapi.dto.request.FlagRequestDto;
import com.cstudy.moduleapi.dto.request.RequestResponseDto;
import com.cstudy.modulecommon.domain.alarm.AlarmArgs;
import com.cstudy.modulecommon.domain.alarm.AlarmType;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.request.Request;
import com.cstudy.modulecommon.dto.UpdateRequestRequestDto;
import com.cstudy.modulecommon.error.member.NotFoundMemberId;
import com.cstudy.modulecommon.error.request.NotFoundRequest;
import com.cstudy.modulecommon.repository.alarm.AlarmRepository;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import com.cstudy.modulecommon.repository.request.RequestRepository;
import com.cstudy.modulecommon.util.LoginUserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class RequestServiceImpl implements RequestService {

    private final static Long ADMIN_ID = 1L;

    private final RequestRepository requestRepository;
    private final MemberRepository memberRepository;
    private final RedisPublisher redisPublisher;
    private final AlarmRepository alarmRepository;
    private final MemberLoadComponent memberLoadComponent;
    private final AlarmService alarmService;

    public RequestServiceImpl(
            RequestRepository requestRepository,
            MemberRepository memberRepository,
            RedisPublisher redisPublisher,
            AlarmRepository alarmRepository,
            MemberLoadComponent memberLoadComponent,
            AlarmService alarmService
    ) {
        this.requestRepository = requestRepository;
        this.memberRepository = memberRepository;
        this.redisPublisher = redisPublisher;
        this.alarmRepository = alarmRepository;
        this.memberLoadComponent = memberLoadComponent;
        this.alarmService = alarmService;
    }

    /**
     * 일반 회원이 관리자에게 새로운 문제를 풀고 싶어서 요청을 보낸다.
     * 이때 알림을 통해서 관리자에게 어떤 회원이 문제를 요청을 하였는지 알림을 보낸다.
     */
    @Override
    @Transactional
    public Long createRequest(CreateRequestRequestDto requestDto, LoginUserDto loginUserDto) {
        log.info("요청 문제 제목 {}", requestDto.getTitle());
        log.info("요청 문제 내용 {}", requestDto.getDescription());
        Member member = memberLoadComponent.loadMemberById(loginUserDto.getMemberId());
        requestRepository.save(Request.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .member(member)
                .build());

        log.info("알람 발송 관리자 전송");
        alarmService.send(AlarmType.NEW_REQUEST_USER, new AlarmArgs(loginUserDto.getMemberId(), ADMIN_ID, requestDto.getTitle()), ADMIN_ID);
        log.info("알람 발송 성공");

        member.addRequest(Request.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .member(memberLoadComponent.loadMemberByEmail(loginUserDto.getMemberEmail()))
                .build());

        return Request.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .member(memberLoadComponent.loadMemberByEmail(loginUserDto.getMemberEmail()))
                .build().getId();
    }

    private Member adminMember() {
        return memberRepository.findById(ADMIN_ID)
                .orElseThrow(() -> new NotFoundMemberId(ADMIN_ID));
    }


    /**
     * 단일 요청 게시글을 조회한다.
     */
    @Override
    @Transactional(readOnly = true)
    public RequestResponseDto getRequest(Long id) {
        log.info("단일 게시글 아이디 : {}", id);
        return RequestResponseDto.of(requestRepository.findById(id)
                .orElseThrow(() -> new NotFoundRequest(id)));
    }

    /**
     * 전체 페이징을 처리하는 부분으로 조건이 없이 페이징을 한다.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RequestResponseDto> getRequestList(Pageable pageable) {
        Page<Request> requests = requestRepository.findAll(pageable);
        return requests.map(RequestResponseDto::of);
    }

    /**
     * Get request list of member.
     *
     * @param memberId id of member who requested problem.
     * @param pageable page information
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RequestResponseDto> getRequestList(Long memberId, Pageable pageable) {
        Page<Request> request = requestRepository.findRequestByMemberId(memberId, pageable);
        return request.map(RequestResponseDto::of);
    }

    /**
     * 관리자가 문제를 생성을 하였다면 기존의 상태를 대기-> 완료로 변경을 한다.
     */
    @Override
    @Transactional
    public void updateFlag(FlagRequestDto flagDto) {
        log.info("허용할 아이디 : {}", flagDto.getId());
        requestRepository.findById(flagDto.getId())
                .orElseThrow(() -> new NotFoundRequest(flagDto.getId()))
                .updateFlag(flagDto.isFlag());
    }

    /**
     * 해당 게시글을 관리자, 작성자인지 판단하고 삭제한다.
     */
    @Override
    @AuthCheck
    @Transactional
    public void deleteTodoById(Long id, LoginUserDto loginUserDto) {
        requestRepository.deleteById(id);
    }

    /**
     * 해당 게시글을 관리자, 작성자인지 판단하고 업데이트 한다.
     */
    @Override
    @AuthCheck
    @Transactional
    public void updateRequest(UpdateRequestRequestDto updateRequestRequestDto, LoginUserDto loginUserDto) {
        log.info("updateRequest 아이디: {}",updateRequestRequestDto.getId());
        log.info("updateRequest 제목: {}", updateRequestRequestDto.getTitle());
        log.info("updateRequest 내용: {}", updateRequestRequestDto.getDescription());
        requestRepository.findById(updateRequestRequestDto.getId())
                .orElseThrow(() -> new NotFoundRequest(updateRequestRequestDto.getId()))
                .updateRequest(updateRequestRequestDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Request> getRequestListForDiscord() {
        return requestRepository.findByNotAcceptRequestList();
    }


}
