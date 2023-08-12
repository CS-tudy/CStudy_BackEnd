package com.cstudy.moduleapi.dto.refresh;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenDto {
    @NotEmpty(message = "refresh token을 입력하세요.")
    String refreshToken;
}
