package com.cstudy.moduleapi.application.member;

import com.cstudy.moduleapi.application.member.impl.MemberCacheRepository;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.error.member.NotFoundMemberEmail;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import org.springframework.stereotype.Component;

@Component
public class MemberLoadComponent {
    private final MemberCacheRepository memberCacheRepository;
    private final MemberRepository memberRepository;

    public MemberLoadComponent(MemberCacheRepository memberCacheRepository, MemberRepository memberRepository) {
        this.memberCacheRepository = memberCacheRepository;
        this.memberRepository = memberRepository;
    }

    public Member loadMemberByEmail(String memberEmail) {
        return memberCacheRepository.getMember(memberEmail).orElseGet(() ->
                memberRepository.findByEmail(memberEmail).orElseThrow(() -> new NotFoundMemberEmail(memberEmail)));
    }
}
