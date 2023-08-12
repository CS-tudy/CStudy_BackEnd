package com.cstudy.moduleapi.application.member.impl.duplicate;

import com.cstudy.moduleapi.application.member.DuplicateService;
import com.cstudy.moduleapi.dto.member.DuplicateResponseDto;
import com.cstudy.moduleapi.dto.member.DuplicateResult;
import com.cstudy.moduleapi.dto.member.DuplicateType;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements DuplicateService {

    private final MemberRepository memberRepository;

    public EmailServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    @Override
    public DuplicateResponseDto signupDivisionDuplicateCheck(String type, String value) {
        return DuplicateResponseDto.of(memberRepository.findByEmail(value)
                .isPresent() ? DuplicateResult.FALSE.getDivisionResult() : DuplicateResult.TRUE.getDivisionResult());
    }

    @Override
    public DuplicateType getType() {
        return DuplicateType.email;
    }
}
