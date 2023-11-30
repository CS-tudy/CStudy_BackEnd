package com.cstudy.modulecommon.repository.file;

import com.cstudy.modulecommon.domain.file.File;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.util.DataJpaCustomUtil;
import com.cstudy.modulecommon.util.TestEnum;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class FileRepositoryTest extends DataJpaCustomUtil {

//    @Query("SELECT f.fileName FROM File f JOIN f.member m WHERE m.id = :memberId ORDER BY f.createdDate DESC")
//    List<String> findLatestFileNamesByMemberId(@Param("memberId") Long memberId, Pageable pageable);

    @Test
    public void 가장_최근의_회원_파일을_찾는다() throws Exception{
        //given
        final String fileName ="이미지.jpg";
        Member member = createMember(TestEnum.MEMBER_NAME.getMessage(), TestEnum.MEMBER_EMAIL.getMessage());
        File file = createFile(fileName, member);
        //when
        Pageable pageable = Pageable.ofSize(1);
        List<String> memberFileList = fileRepository.findLatestFileNamesByMemberId(1L, pageable);

        //Then
        assertThat(memberFileList.size()).isEqualTo(1);
    }

}