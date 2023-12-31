package com.cstudy.moduleapi.config.argumentResolver;

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
    private String memberEmail;
    private List<String> roles = new ArrayList<>();
}
