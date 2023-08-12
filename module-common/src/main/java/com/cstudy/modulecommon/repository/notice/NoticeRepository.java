package com.cstudy.modulecommon.repository.notice;

import com.cstudy.modulecommon.domain.notice.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeCustomRepository {
    Optional<Notice> findByTitle(String title);
}
