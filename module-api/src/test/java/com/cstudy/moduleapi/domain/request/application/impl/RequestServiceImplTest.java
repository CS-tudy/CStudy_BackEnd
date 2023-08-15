package com.cstudy.moduleapi.domain.request.application.impl;

import com.cstudy.moduleapi.application.member.MemberService;
import com.cstudy.moduleapi.application.request.RequestService;
import com.cstudy.moduleapi.dto.member.MemberSignupRequest;
import com.cstudy.moduleapi.dto.request.CreateRequestRequestDto;
import com.cstudy.moduleapi.dto.request.FlagRequestDto;
import com.cstudy.moduleapi.dto.request.RequestResponseDto;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.error.member.NotFoundMemberEmail;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("local")
@Transactional
class RequestServiceImplTest {

    @Autowired
    private RequestService requestService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;

    private Long memberId1;
    private Long memberId2;

    @BeforeEach
    public void setUp(){
        MemberSignupRequest memberSignupRequest1 = MemberSignupRequest.builder()
                .email("test1@test.com")
                .name("테스트 유저1")
                .password("test1234!").build();
        memberService.signUp(memberSignupRequest1);

        MemberSignupRequest memberSignupRequest2 = MemberSignupRequest.builder()
                .email("test2@test.com")
                .name("테스트 유저2")
                .password("test1234!").build();
        memberService.signUp(memberSignupRequest2);

        Member member1 = memberRepository.findByEmail("test1@test.com")
                .orElseThrow(() -> new NotFoundMemberEmail("test1@test.com"));
        memberId1 = member1.getId();

        Member member2 = memberRepository.findByEmail("test2@test.com")
                .orElseThrow(() -> new NotFoundMemberEmail("test2@test.com"));
        memberId2 = member2.getId();
    }

    @Test
    @DisplayName("게시판 글 생성")
    public void createRequest(){
        CreateRequestRequestDto requestDto = CreateRequestRequestDto.builder()
                .title("문제 요청1")
                .description("문제 요청 내용1")
                .build();
        Long requestId = requestService.createRequest(requestDto, memberId1);
        RequestResponseDto request = requestService.getRequest(requestId);
        assertEquals(request.getTitle(), "문제 요청1");
        assertEquals(request.getDescription(), "문제 요청 내용1");
    }

    @Test
    @DisplayName("게시글 대기")
    public void approve(){
        CreateRequestRequestDto requestDto = CreateRequestRequestDto.builder()
                .title("문제 요청1")
                .description("문제 요청 내용1")
                .build();
        Long requestId = requestService.createRequest(requestDto, 1L);

        RequestResponseDto request1 = requestService.getRequest(requestId);
        assertFalse(request1.isFlag());

        FlagRequestDto flag = new FlagRequestDto(requestId, true);

        Assertions.assertThatThrownBy(() -> requestService.updateFlag(flag))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("권한이 일치하지 않습니다.");
    }



    @Test
    @DisplayName("내 게시글 리스트 조회")
    public void myList(){
        for(int i = 1; i <= 10; i++) {
            CreateRequestRequestDto requestDto1 = CreateRequestRequestDto.builder()
                    .title("문제 요청" + i + " - member1")
                    .description("문제 요청 내용" + i)
                    .build();
            requestService.createRequest(requestDto1, memberId1);
            CreateRequestRequestDto requestDto2 = CreateRequestRequestDto.builder()
                    .title("문제 요청" + i + " - member2")
                    .description("문제 요청 내용" + i)
                    .build();
            requestService.createRequest(requestDto2, memberId2);
        }
        Pageable pageable = PageRequest.of(0, 5, Sort.by("createdAt").descending());
        Page<RequestResponseDto> requestList = requestService.getRequestList(memberId1, pageable);
        for (int i = 10; i >= 6; i--) {
            assertEquals(requestList.getContent().get(10-i).getTitle(), "문제 요청" + i + " - member1");
        }
    }

    @Test
    @DisplayName("게시글 리스트 조회")
    public void requestList() {
        for(int i = 1; i <= 10; i++) {
            CreateRequestRequestDto requestDto = CreateRequestRequestDto.builder()
                    .title("문제 요청"+i)
                    .description("문제 요청 내용"+i)
                    .build();
            Long requestId = requestService.createRequest(requestDto, memberId1);
        }
        Pageable pageable = PageRequest.of(0, 5, Sort.by("createdAt").descending());
        Page<RequestResponseDto> requestList = requestService.getRequestList(memberId1, pageable);
        for (int i = 10; i >= 6; i--) {
            assertEquals(requestList.getContent().get(10-i).getTitle(), "문제 요청"+i);
            assertEquals(requestList.getContent().get(10-i).getDescription(), "문제 요청 내용"+i);
        }
    }
}