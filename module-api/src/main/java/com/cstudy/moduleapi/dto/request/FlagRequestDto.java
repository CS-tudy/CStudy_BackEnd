package com.cstudy.moduleapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlagRequestDto {
    private Long id;
    private boolean flag;
}
