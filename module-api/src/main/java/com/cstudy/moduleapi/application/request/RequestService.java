package com.cstudy.moduleapi.application.request;

import com.cstudy.moduleapi.dto.request.CreateRequestRequestDto;
import com.cstudy.moduleapi.dto.request.FlagRequestDto;
import com.cstudy.moduleapi.dto.request.RequestResponseDto;
import com.cstudy.modulecommon.dto.UpdateRequestRequestDto;
import com.cstudy.modulecommon.util.LoginUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RequestService {

    Long createRequest(CreateRequestRequestDto requestDto, Long memberId);

    RequestResponseDto getRequest(Long id);

    Page<RequestResponseDto> getRequestList(Pageable pageable);

    Page<RequestResponseDto> getRequestList(Long memberId, Pageable pageable);

    void updateFlag(FlagRequestDto flagDto);

    void deleteTodoById(Long id, LoginUserDto loginUserDto);

    void updateRequest(UpdateRequestRequestDto updateRequestRequestDto, LoginUserDto loginUserDto);

}
