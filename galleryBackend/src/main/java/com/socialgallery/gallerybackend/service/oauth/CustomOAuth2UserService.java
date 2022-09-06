package com.socialgallery.gallerybackend.service.oauth;

import com.socialgallery.gallerybackend.config.security.JwtProvider;
import com.socialgallery.gallerybackend.dto.oauth.OAuth2Attribute;
import com.socialgallery.gallerybackend.entity.security.RefreshTokenJpaRepo;
import com.socialgallery.gallerybackend.entity.user.Users;
import com.socialgallery.gallerybackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    private final JwtProvider jwtProvider;

    private final RefreshTokenJpaRepo refreshTokenJpaRepo;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuth2Attribute oAuth2Attribute =
                OAuth2Attribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        log.info("{}", oAuth2Attribute);

        var memberAttribute = oAuth2Attribute.convertToMap();

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                memberAttribute, "email");
    }

//    private Users saveOrUpdate(OAuth2Attribute attributes) {
//        Users users = userRepository.findByEmail(attributes.getEmail())
//                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
//                .orElse(attributes.toEntity());
//
//        return userRepository.save(users);
//    }

}
