package com.cstudy.moduleapi.config.oauth;

import com.cstudy.moduleapi.application.refershToken.RefreshTokenService;
import com.cstudy.moduleapi.config.jwt.util.JwtTokenizer;
import com.cstudy.moduleapi.config.security.auth.OAuthAttributes;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.role.Role;
import com.cstudy.modulecommon.domain.role.RoleEnum;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import com.cstudy.modulecommon.repository.role.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberRepository memberRepository;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenizer jwtTokenizer;
    private final RoleRepository roleRepository;

    public CustomOAuth2UserService(MemberRepository memberRepository, RefreshTokenService refreshTokenService, JwtTokenizer jwtTokenizer, RoleRepository roleRepository) {
        this.memberRepository = memberRepository;
        this.refreshTokenService = refreshTokenService;
        this.jwtTokenizer = jwtTokenizer;
        this.roleRepository = roleRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Member member = saveOrUpdate(attributes);

        refreshTokenService.addRefreshToken(jwtTokenizer
                .createRefreshToken(member.getId(), member.getEmail(), List.of(RoleEnum.CUSTOM.getRoleName())));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(RoleEnum.CUSTOM.getRoleName())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }


    private Member saveOrUpdate(OAuthAttributes attributes) {
        Member member = Member.builder()
                .email(attributes.getEmail())
                .name(attributes.getName())
                .roles(new HashSet<>())
                .build();

        Optional<Role> userRole = roleRepository.findByName(RoleEnum.CUSTOM.getRoleName());
        userRole.ifPresent(member::changeRole);
        return memberRepository.save(member);
    }

}
