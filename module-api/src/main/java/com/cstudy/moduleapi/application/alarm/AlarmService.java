package com.cstudy.moduleapi.application.alarm;

import com.cstudy.modulecommon.domain.alarm.Alarm;
import com.cstudy.modulecommon.domain.alarm.AlarmArgs;
import com.cstudy.modulecommon.domain.alarm.AlarmType;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.error.alarm.AlarmInvalidException;
import com.cstudy.modulecommon.error.member.NotFoundMemberId;
import com.cstudy.modulecommon.repository.alarm.AlarmRepository;
import com.cstudy.modulecommon.repository.alarm.EmitterRepository;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {

    private final static Long ADMIN_ID = 1L;
    private final static Long DEFAULT_TIME_OUT = 60L * 1000 * 60;
    private final static String ALARM_NAME = "alarm";


    private final MemberRepository memberRepository;
    private final EmitterRepository emitterRepository;
    private final AlarmRepository alarmRepository;
    private final AlarmFactory alarmFactory;


    /**
     *  새로운 알림을 보낸다. 현재 알림은 3가지가 있다.
     *  1. 새로운 문제 요청을 보냈을 때
     *  - 일반 회원이 새로운 문제를 관리자에게 요청하면 관리자에게 요청 알림을 보낸다.
     *
     *  2. 관리자가 새로운 경기를 생성한다.
     *  - 관리자가 새로운 경기를 만들면 모든 회원에게 알림을 발송한다.
     *
     *  3. 일반 회원이 경기에 참가를 하면 관리자에게 알림을 보낸다.
     *  - 일반 회원이 경기에 참가를 하면 관리자에게 n번 회원이 경기에 참가를 하였습니다.
     */
    public void send(AlarmType type, AlarmArgs args, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberId(memberId));

        Alarm alarm = alarmRepository.save(Alarm.builder()
                .member(member)
                .alarmType(type)
                .args(new AlarmArgs(args.getFromMemberId(), args.getTargetId(), args.getAlarmData()))
                .build());

        log.warn("args from member: {}", args.getFromMemberId());
        log.warn("args target id: {}", args.getTargetId());
        String alarmData = alarmFactory.alarmFactory(type, args);
        emitterRepository.get(memberId).ifPresentOrElse(sseEmitter -> {
            try {
                sseEmitter.send(SseEmitter.event()
                        .id(alarm.getId().toString())
                        .name(ALARM_NAME)
                        .data(alarmData));
            } catch (IOException e) {
                emitterRepository.delete(memberId);
                throw new AlarmInvalidException(e.getMessage());
            }
        }, () -> log.info("No emitter founded"));

    }

    /**
     * 처음 sse 연결을 하기 위하여 작성 이때 처음에 Dummy를 전송
     * 이때 jpa의 open-in-view를 false로 설정을 해야된다.
     * SSE 통신을 하는 동안에 HTTP Connection이 계속 열려있다. -> true로 설정을 하면 DB Connection이 유지된다.
     */
    public SseEmitter connectionAlarm(Long userId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIME_OUT);
        emitterRepository.save(userId, emitter);
        emitter.onCompletion(() -> emitterRepository.delete(userId));
        emitter.onTimeout(() -> emitterRepository.delete(userId));

        try {
            log.info("send");
            emitter.send(SseEmitter.event()
                    .id("id")
                    .name(ALARM_NAME)
                    .data("connect completed"));
        } catch (IOException e) {
            throw new AlarmInvalidException(e.getMessage());
        }
        return emitter;
    }

    @Transactional
    public void delete(Long alarmId) {
        alarmRepository.deleteById(alarmId);
    }
}
