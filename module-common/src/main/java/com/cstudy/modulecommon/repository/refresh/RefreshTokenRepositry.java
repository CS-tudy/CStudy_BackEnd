package com.cstudy.modulecommon.repository.refresh;


import com.cstudy.modulecommon.domain.refresh.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepositry extends JpaRepository<RefreshToken, Long> {
}
