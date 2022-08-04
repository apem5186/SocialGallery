package com.socialgallery.gallerybackend.security;

import com.socialgallery.gallerybackend.entity.member.Member;
import com.socialgallery.gallerybackend.entity.member.MemberRole;
import com.socialgallery.gallerybackend.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemberTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insertDummies() {

        //1 - 80까지는 USER만 지정
        //81 - 90까지는 USER, MANAGER
        //91 - 100까지는 USER, MANAGER, ADMIN

        IntStream.rangeClosed(1, 100).forEach(i -> {
            Member member = Member.builder()
                    .username("user"+i)
                    .email("user"+i+"social.com")
                    .fromSocial(false)
                    .password(passwordEncoder.encode("1111"))
                    .build();

            //default role
            member.addMemberRole(MemberRole.ROLE_USER);

            if (i>80)
                member.addMemberRole(MemberRole.ROLE_MEMBER);
            if (i>90)
                member.addMemberRole(MemberRole.ROLE_ADMIN);

            memberRepository.save(member);
        });
    }

    @Test
    public void testRead() {

        Optional<Member> result = memberRepository.findByEmail("user95@social.com");
        System.out.println(result);

        Member member = result.get();

        System.out.println(member);
    }
}
