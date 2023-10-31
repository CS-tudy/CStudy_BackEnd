package com.cstudy.moduleapi.application.request.impl;

import com.cstudy.moduleapi.aop.AuthCheck;
import com.cstudy.moduleapi.application.alarm.AlarmService;
import com.cstudy.moduleapi.application.member.MemberLoadComponent;
import com.cstudy.moduleapi.application.request.RequestService;
import com.cstudy.moduleapi.config.redis.RedisPublisher;
import com.cstudy.moduleapi.dto.request.CreateRequestRequestDto;
import com.cstudy.moduleapi.dto.request.FlagRequestDto;
import com.cstudy.moduleapi.dto.request.RequestResponseDto;
import com.cstudy.modulecommon.domain.alarm.Alarm;
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

@Service
@Slf4j
public class RequestServiceImpl implements RequestService {

    private final static Long ADMIN_ID = 1L;

    private final RequestRepository requestRepository;
    private final MemberRepository memberRepository;
    private final RedisPublisher redisPublisher;
    private final AlarmRepository alarmRepository;
    private final MemberLoadComponent memberLoadComponent;
    private final AlarmService alarmService;

    public RequestServiceImpl(RequestRepository requestRepository, MemberRepository memberRepository, RedisPublisher redisPublisher, AlarmRepository alarmRepository, MemberLoadComponent memberLoadComponent, AlarmService alarmService) {
        this.requestRepository = requestRepository;
        this.memberRepository = memberRepository;
        this.redisPublisher = redisPublisher;
        this.alarmRepository = alarmRepository;
        this.memberLoadComponent = memberLoadComponent;
        this.alarmService = alarmService;
    }

    /**
     * Create requests for request problem.
     *
     * @param requestDto the request DTO containing the request information
     */
    @Override
    @Transactional
    public Long createRequest(
            CreateRequestRequestDto requestDto,
            LoginUserDto loginUserDto) {
        Member member = memberLoadComponent.loadMemberByEmail(loginUserDto.getMemberEmail());
        requestRepository.save(Request.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .member(member)
                .build());

        alarmService.send(AlarmType.NEW_REQUEST_USER, new AlarmArgs(loginUserDto.getMemberId(),ADMIN_ID, requestDto.getTitle()),ADMIN_ID);

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
     * Get requests for request problem.
     *
     * @param id id of request entity.
     */
    @Override
    @Transactional(readOnly = true)
    public RequestResponseDto getRequest(Long id) {

        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new NotFoundRequest(id));

        return RequestResponseDto.of(request);
    }

    /**
     * Get request list.
     *
     * @param pageable page information
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
     * Update flag for request problem. Only ADMIN can change flag
     *
     * @param flagDto id of request entity
     */
    @Override
    @Transactional
    public void updateFlag(FlagRequestDto flagDto) {
        requestRepository.findById(flagDto.getId())
                .orElseThrow(() -> new NotFoundRequest(flagDto.getId()))
                .updateFlag(flagDto.isFlag());
    }

    @Override
    @AuthCheck
    @Transactional
    public void deleteTodoById(Long id, LoginUserDto loginUserDto) {
        requestRepository.deleteById(id);
    }

    @Override
    @AuthCheck
    @Transactional
    public void updateRequest(UpdateRequestRequestDto updateRequestRequestDto, LoginUserDto loginUserDto) {
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
