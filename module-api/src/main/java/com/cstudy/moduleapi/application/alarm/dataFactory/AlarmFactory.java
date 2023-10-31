package com.cstudy.moduleapi.application.alarm.dataFactory;

import com.cstudy.modulecommon.domain.alarm.AlarmArgs;
import com.cstudy.modulecommon.domain.alarm.AlarmType;
import org.springframework.stereotype.Component;

@Component
public class AlarmFactory implements com.cstudy.moduleapi.application.alarm.AlarmFactory {

    @Override
    public String alarmFactory(AlarmType alarmType, AlarmArgs args) {
        switch (alarmType) {
            case NEW_REQUEST_USER:
                return args.getFromMemberId()+AlarmType.NEW_REQUEST_USER.getAlarmText();
            case JOIN_COMPETITION:
                return args.getFromMemberId()+"번 회원이"+args.getAlarmData()+AlarmType.JOIN_COMPETITION.getAlarmText();
            case CREATE_COMPETITION:
                return AlarmType.CREATE_COMPETITION.getAlarmText();
            default:
                return "타입을 확인을 해주세요";
        }
    }
}
