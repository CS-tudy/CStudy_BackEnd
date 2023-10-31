package com.cstudy.moduleapi.application.alarm;

import com.cstudy.modulecommon.domain.alarm.AlarmArgs;
import com.cstudy.modulecommon.domain.alarm.AlarmType;

public interface AlarmFactory {
    String alarmFactory(AlarmType alarmType , AlarmArgs alarmArgs);
}
