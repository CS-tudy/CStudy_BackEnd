package com.cstudy.moduleapi.config.security.auth;


import lombok.Getter;

import java.io.Serializable;

@Getter
public class JwtMember implements Serializable {
    private String name;
    private String email;
    private String picture;

    public JwtMember(
            String name,
            String email,
            String picture
    ) {
        this.name = name;
        this.email = email;
        this.picture = picture;
    }
}
