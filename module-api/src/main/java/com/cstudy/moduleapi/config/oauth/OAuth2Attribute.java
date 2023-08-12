package com.cstudy.moduleapi.config.oauth;

import java.util.HashMap;
import java.util.Map;

import com.cstudy.modulecommon.domain.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class OAuth2Attribute {
    private Map<String, Object> attributes;
    private Member member;
    private String email;

    static OAuth2Attribute of(Member member, Map<String, Object> attributes) {
        return ofGoogle(member, attributes);
    }

    private static OAuth2Attribute ofGoogle(Member member, Map<String, Object> attributes) {
        return OAuth2Attribute.builder()
                .email((String) attributes.get("email"))
                .member(member)
                .attributes(attributes)
                .build();
    }

    Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("member", member);
        map.put("email", email);

        return map;
    }
}