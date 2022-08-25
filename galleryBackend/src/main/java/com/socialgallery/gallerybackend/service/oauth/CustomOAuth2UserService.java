package com.socialgallery.gallerybackend.service.oauth;


import com.socialgallery.gallerybackend.config.security.JwtProvider;
import com.socialgallery.gallerybackend.dto.jwt.TokenDTO;
import com.socialgallery.gallerybackend.dto.oauth.OAuth2Attributes;
import com.socialgallery.gallerybackend.entity.user.AuthProvider;
import com.socialgallery.gallerybackend.entity.user.UserRole;
import com.socialgallery.gallerybackend.entity.user.Users;
import com.socialgallery.gallerybackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private JwtProvider jwtProvider;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        //  1번
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();

        //	2번
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);
        log.info("============================================");
        oAuth2User.getAttributes().forEach((k, v) -> {
            log.info(k +":"+v); //sub, picture, email, email_verified, EMAIL 등이 출력
        });
        String email = oAuth2User.getAttribute("email");

        Users users = Users.builder()
                .email(email)
                .username(oAuth2User.getAttribute("name"))
                .password(bCryptPasswordEncoder.encode("1111"))
                .phone(null)
                .picture(oAuth2User.getAttribute("picture"))
                .authProvider(String.valueOf(userRequest.getClientRegistration()))
                .fromSocial(true)
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
        Optional<Users> result = userRepository.findByEmail(email);
        if (result.isPresent()) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다. 가입된 이메일로 로그인을 해주세요.");
        }

        userRepository.save(users);



        //	3번
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        log.info("registrationId = {}", registrationId);
        log.info("userNameAttributeName = {}", userNameAttributeName);
        log.info("============================================");

        // 4번
        OAuth2Attributes oAuth2Attribute =
                OAuth2Attributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        var memberAttribute = oAuth2Attribute.convertToMap();

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                memberAttribute, "email");
    }
}
