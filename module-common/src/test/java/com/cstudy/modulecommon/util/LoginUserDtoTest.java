package com.cstudy.modulecommon.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class LoginUserDtoTest {

    @Test
    public void testAddRole() {
        LoginUserDto loginUserDto = LoginUserDto.builder()
                .memberId(123L)
                .build();

        loginUserDto.addRole("USER");

        assertTrue(loginUserDto.getRoles().contains("USER"));
        assertEquals(1, loginUserDto.getRoles().size());
    }

    @Test
    public void testRoles() {
        LoginUserDto loginUserDto = LoginUserDto.builder()
                .memberId(123L)
                .build();

        assertNotNull(loginUserDto.getRoles());
        assertTrue(loginUserDto.getRoles().isEmpty());
    }

    @Test
    public void testRolesInit() {
        LoginUserDto loginUserDto = LoginUserDto.builder()
                .memberId(123L)
                .roles(Arrays.asList("USER", "ADMIN"))
                .build();

        assertNotNull(loginUserDto.getRoles());
        assertFalse(loginUserDto.getRoles().isEmpty());
        assertEquals(2, loginUserDto.getRoles().size());
    }
}