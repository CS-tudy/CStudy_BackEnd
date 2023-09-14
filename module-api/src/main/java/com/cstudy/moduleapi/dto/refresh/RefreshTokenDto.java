package com.cstudy.moduleapi.dto.refresh;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenDto {
    @NotNull(message = "refresh token을 입력하세요.")
    String refreshToken;
}
