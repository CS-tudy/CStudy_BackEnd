package com.cstudy.modulecommon.domain.alarm;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlarmType {

    NEW_REQUEST_USER("번 회원이 새로운 문제를 요청을 하였습니다."),
    JOIN_COMPETITION("의 경기에 참가를 하였습니다."),
    CREATE_COMPETITION("새로운 경기가 생겼습니다.");

    private final String alarmText;

}
