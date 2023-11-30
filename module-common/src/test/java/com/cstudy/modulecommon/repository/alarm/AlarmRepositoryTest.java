package com.cstudy.modulecommon.repository.alarm;

import com.cstudy.modulecommon.config.QueryDslConfig;
import com.cstudy.modulecommon.domain.alarm.Alarm;
import com.cstudy.modulecommon.domain.alarm.AlarmType;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryDslConfig.class})
class AlarmRepositoryTest {

    @Autowired
    private AlarmRepository alarmRepository;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원_알람() throws Exception{
        //given
        final String name = "김무건";
        final String name2 = "우민호";

        final String email1 = "email1@email.com";
        final String email2 = "email2@email.com";


        Member member = Member.of(name, email1);
        Member member2 = Member.of(name2, email2);

        memberRepository.save(member);
        memberRepository.save(member2);

        Alarm alarm = Alarm.of(member, AlarmType.NEW_REQUEST_USER);
        Alarm alarm2 = Alarm.of(member2, AlarmType.JOIN_COMPETITION);

        alarmRepository.save(alarm);
        alarmRepository.save(alarm2);
        //when
        Pageable pageable = Pageable.ofSize(4);
        Page<Alarm> allByMember = alarmRepository.findAllByMember(member, pageable);

        //Then
        assertThat(allByMember.getContent().get(0).getMember().getName()).isEqualTo(name);
    }
}