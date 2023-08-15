package com.cstudy.modulecommon.domainEntity.role;

import com.cstudy.modulecommon.domain.role.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {
    @Test
    public void 특정롤_생성_테스트() {
        Role role = Role.builder()
                .name("ROLE_USER")
                .build();

        Assertions.assertEquals("ROLE_USER", role.getName());
    }

    @Test
    public void 롤_생성() {
        Role role = new Role("ROLE_ADMIN");

        Assertions.assertEquals("ROLE_ADMIN", role.getName());
    }
}