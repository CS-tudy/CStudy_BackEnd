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


    @Transactional
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


    @Transactional
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
