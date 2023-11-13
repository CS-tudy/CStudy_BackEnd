package com.cstudy.moduleapi.application.member;

import com.cstudy.moduleapi.application.member.impl.MemberCacheRepository;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.error.member.NotFoundMemberEmail;
import com.cstudy.modulecommon.error.member.NotFoundMemberId;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class MemberLoadComponent {
    private final MemberCacheRepository memberCacheRepository;
    private final MemberRepository memberRepository;

    public MemberLoadComponent(MemberCacheRepository memberCacheRepository, MemberRepository memberRepository) {
        this.memberCacheRepository = memberCacheRepository;
        this.memberRepository = memberRepository;
    }

    public Member loadMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberId(memberId));
    }

    public Member loadMemberByEmail(String memberEmail) {
         return memberCacheRepository.getMember(memberEmail).orElseThrow(()-> new NotFoundMemberEmail("cache e"));
//        return memberRepository.findByEmail(memberEmail)
//                .orElseThrow(() -> new NotFoundMemberEmail(memberEmail));
//
//        if (cachedMember.isPresent()) {
//            log.info("Member loaded from Redis: " + memberEmail);
//            return cachedMember.get();
//        } else {
//            Member dbMember = memberRepository.findByEmail(memberEmail)
//                    .orElseThrow(() -> new NotFoundMemberEmail(memberEmail));
//            log.info("Member loaded from Database: " + memberEmail);
//            memberCacheRepository.setMember(dbMember);
//
//            return dbMember;
//        }
    }

}
