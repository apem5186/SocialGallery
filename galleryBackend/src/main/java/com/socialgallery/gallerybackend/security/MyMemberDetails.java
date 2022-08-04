package com.socialgallery.gallerybackend.security;

import com.socialgallery.gallerybackend.entity.member.Member;
import com.socialgallery.gallerybackend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyMemberDetails implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("MyMemberDetails loadUserByUsername : " + username);

        final Member member = memberRepository.findByUsername(username);

        if (member == null) {
            throw new UsernameNotFoundException("User '" + username + "' not found");
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(member.getPassword())
                .authorities(member.getRoleSet().stream()
                        .map(role -> new SimpleGrantedAuthority(
                                "ROLE_"+role.name()
                        )).collect(Collectors.toSet()))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
