package com.cstudy.modulecommon.domain.alarm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlarmArgs {

    //알람을 발생시킨 사람
    private Long fromMemberId;

    private Long targetId;

    private String alarmData;
}
