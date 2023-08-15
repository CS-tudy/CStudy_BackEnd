package com.cstudy.moduleapi.domainEntity.notice;

import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.notice.Notice;
import com.cstudy.modulecommon.dto.NoticeUpdateRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NoticeTest {

    private Member member;

    @BeforeEach
    public void setUp() {
        member = new Member();
    }

    @Test
    public void testCreateNotice() {
        Notice notice = Notice.builder()
                .title("Title")
                .content("Content")
                .member(member)
                .build();

        Assertions.assertEquals("Title", notice.getTitle());
        Assertions.assertEquals("Content", notice.getContent());
        Assertions.assertEquals(member, notice.getMember());
    }

    @Test
    public void testUpdateNotice() {
        Notice notice = Notice.builder()
                .title("Title")
                .content("Content")
                .member(member)
                .build();

        NoticeUpdateRequestDto updateRequestDto = new NoticeUpdateRequestDto();
        updateRequestDto.setTitle("Updated Title");
        updateRequestDto.setContent("Updated Content");

        notice.updateNotice(updateRequestDto);

        Assertions.assertEquals("Updated Title", notice.getTitle());
        Assertions.assertEquals("Updated Content", notice.getContent());
    }

    @Test
    public void testCreateEmptyNotice() {
        Notice notice = new Notice();

        Assertions.assertEquals(null, notice.getTitle());
        Assertions.assertEquals(null, notice.getContent());
        Assertions.assertEquals(null, notice.getMember());
    }

    @Test
    public void testUpdateNoticeMultipleTimes() {
        Notice notice = Notice.builder()
                .title("Original Title")
                .content("Original Content")
                .member(member)
                .build();

        NoticeUpdateRequestDto updateRequestDto1 = new NoticeUpdateRequestDto();
        updateRequestDto1.setTitle("Updated Title 1");
        updateRequestDto1.setContent("Updated Content 1");

        NoticeUpdateRequestDto updateRequestDto2 = new NoticeUpdateRequestDto();
        updateRequestDto2.setTitle("Updated Title 2");
        updateRequestDto2.setContent("Updated Content 2");

        notice.updateNotice(updateRequestDto1);
        notice.updateNotice(updateRequestDto2);

        Assertions.assertEquals("Updated Title 2", notice.getTitle());
        Assertions.assertEquals("Updated Content 2", notice.getContent());
    }


}
