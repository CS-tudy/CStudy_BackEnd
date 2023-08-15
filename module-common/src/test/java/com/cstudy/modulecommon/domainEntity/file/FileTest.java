package com.cstudy.modulecommon.domainEntity.file;

import com.cstudy.modulecommon.domain.file.File;
import com.cstudy.modulecommon.domain.member.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileTest {
    private Member member;

    @BeforeEach
    public void setUp() {
        member = new Member();
    }

    @Test
    public void createFile() {
        File file = File.builder()
                .fileName("sample_file.txt")
                .member(member)
                .build();

        Assertions.assertEquals("sample_file.txt", file.getFileName());
        Assertions.assertEquals(member, file.getMember());
    }
}