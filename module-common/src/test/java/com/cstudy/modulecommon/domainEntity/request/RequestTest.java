package com.cstudy.modulecommon.domainEntity.request;

import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.request.Request;
import com.cstudy.modulecommon.dto.UpdateRequestRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RequestTest {
    private Member member = new Member();

    @Test
    public void testCreateRequest() {
        Request request = Request.builder()
                .title("제목")
                .description("설명")
                .member(member)
                .build();

        Assertions.assertFalse(request.isFlag());
        Assertions.assertEquals("제목", request.getTitle());
        Assertions.assertEquals("설명", request.getDescription());
        Assertions.assertEquals(member, request.getMember());
        Assertions.assertEquals(LocalDateTime.now().withNano(0), request.getCreatedAt().withNano(0));
    }

    @Test
    public void testUpdateFlag() {
        Request request = Request.builder()
                .title("제목")
                .description("설명")
                .member(member)
                .build();

        request.updateFlag(true);

        Assertions.assertEquals(true, request.isFlag());
    }

    @Test
    public void testUpdateRequest() {
        Request request = Request.builder()
                .title("Original Title")
                .description("Original Description")
                .member(member)
                .build();

        UpdateRequestRequestDto updateRequestRequestDto = new UpdateRequestRequestDto();
        updateRequestRequestDto.setTitle("Updated Title");
        updateRequestRequestDto.setDescription("Updated Description");

        request.updateRequest(updateRequestRequestDto);

        Assertions.assertEquals("Updated Title", request.getTitle());
        Assertions.assertEquals("Updated Description", request.getDescription());
    }
}