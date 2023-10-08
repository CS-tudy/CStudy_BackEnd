package com.cstudy.moduleapi.application.request.impl;

import com.cstudy.moduleapi.aop.AuthCheck;
import com.cstudy.moduleapi.application.request.RequestService;
import com.cstudy.moduleapi.dto.request.CreateRequestRequestDto;
import com.cstudy.moduleapi.dto.request.FlagRequestDto;
import com.cstudy.moduleapi.dto.request.RequestResponseDto;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.request.Request;
import com.cstudy.modulecommon.dto.UpdateRequestRequestDto;
import com.cstudy.modulecommon.error.member.NotFoundMemberId;
import com.cstudy.modulecommon.error.request.NotFoundRequest;
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

    private final RequestRepository requestRepository;
    private final MemberRepository memberRepository;

    public RequestServiceImpl(
            RequestRepository requestRepository,
            MemberRepository memberRepository
    ) {
        this.requestRepository = requestRepository;
        this.memberRepository = memberRepository;
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
            Long memberId
    ) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberId(memberId));

        Request request = Request.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .member(member)
                .build();

        requestRepository.save(request);

        member.addRequest(request);

        return request.getId();
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
