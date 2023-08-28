package com.cstudy.moduleapi.argumentResolver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginInfoDto {
    private Long memberId;
    private List<String> roles = new ArrayList<>();
}
