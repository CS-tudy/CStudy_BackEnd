package com.cstudy.modulecommon.repository.file;


import com.cstudy.modulecommon.domain.file.File;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FileRepository extends JpaRepository<File,Long> {
    @Query("SELECT f.fileName FROM File f JOIN f.member m WHERE m.id = :memberId ORDER BY f.createdDate DESC")
    List<String> findLatestFileNamesByMemberId(@Param("memberId") Long memberId, Pageable pageable);

    default String findLatestFileNameByMemberId(Long memberId) {
        List<String> result = findLatestFileNamesByMemberId(memberId, PageRequest.of(0, 1));
        return result.isEmpty() ? null : result.get(0);
    }

}
