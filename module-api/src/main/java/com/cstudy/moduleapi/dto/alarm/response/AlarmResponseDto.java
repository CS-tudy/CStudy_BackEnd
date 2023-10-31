package com.cstudy.moduleapi.dto.alarm.response;

import com.cstudy.moduleapi.dto.member.MemberLoginResponse;
import com.cstudy.modulecommon.domain.alarm.Alarm;
import com.cstudy.modulecommon.domain.alarm.AlarmArgs;
import com.cstudy.modulecommon.domain.alarm.AlarmType;
import com.cstudy.modulecommon.domain.member.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmResponseDto {
    private Long id;
    private String text;
    private AlarmType alarmType;
    private AlarmArgs alarmArgs;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime lastModifiedDate;


    public static AlarmResponseDto of(Alarm alarm) {
        return AlarmResponseDto.builder()
                .id(alarm.getId())
                .text(alarm.getAlarmType().getAlarmText())
                .alarmType(alarm.getAlarmType())
                .alarmArgs(alarm.getArgs())
                .createdDate(alarm.getCreatedDate())
                .lastModifiedDate(alarm.getLastModifiedDate())
                .build();
    }
}
