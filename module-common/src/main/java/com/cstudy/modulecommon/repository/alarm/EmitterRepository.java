package com.cstudy.modulecommon.repository.alarm;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.model.source.internal.hbm.PluralAttributeElementSourceManyToManyImpl;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


/**
 * 현재 로컬 캐시에 저장을 했다. 이 부분은 다른 메세징을 추가하여 처리한다.
 */
@Slf4j
@Repository
public class EmitterRepository {

    private Map<String, SseEmitter> emitterMap = new HashMap<>();


    public SseEmitter save(Long memberId, SseEmitter sseEmitter) {
        final String key = getKey(memberId);
        emitterMap.put(key,sseEmitter);
        log.info("Set sseEmitter : {} ", key);
        return sseEmitter;
    }

    public Optional<SseEmitter> get(Long memberId) {
        final String key = getKey(memberId);
        return Optional.ofNullable(emitterMap.get(key));
    }

    public void delete(Long memberId) {
        emitterMap.remove(getKey(memberId));
    }


    public String getKey(Long memberId) {
        return "Emitter:UID" + memberId;
    }
}
