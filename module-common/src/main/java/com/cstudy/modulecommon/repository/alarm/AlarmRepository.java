package com.cstudy.modulecommon.repository.alarm;

import com.cstudy.modulecommon.domain.alarm.Alarm;
import com.cstudy.modulecommon.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    Page<Alarm> findAllByMember(Member member, Pageable pageable);
}
