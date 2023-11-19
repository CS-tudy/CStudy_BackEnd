package com.cstudy.modulecommon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionSearchCondition {
    private String questionTitle;
    private String categoryTitle;
    private Integer  status;
    private Long memberId;
}
