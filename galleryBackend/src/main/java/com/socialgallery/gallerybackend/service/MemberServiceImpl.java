package com.socialgallery.gallerybackend.service;

import com.socialgallery.gallerybackend.dto.MemberDTO;
import com.socialgallery.gallerybackend.entity.member.Member;
import com.socialgallery.gallerybackend.repository.MemberRepository;
import com.socialgallery.gallerybackend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final JwtUtil jwtUtil;

    @Override
    public Member signIn(String email, String password) {

        if (memberRepository.findByEmail(email) != null) {
            Member member = memberRepository.findByEmail(email);
            if (member.getPassword().equals(password)) {
                String authToken = jwtUtil.createAuthToken(email);
                entitiesToDTO(member);
                log.info("authToken : " + authToken);
                Member.builder().authToken(email);
                log.info("Member : " + member);
                return member;
            } else {
                throw new RuntimeException("비밀번호 틀림");
            }
        } else {
            throw new RuntimeException("이메일 혹은 비밀번호가 틀림");
        }

    }

    @Transactional
    @Override
    public Long signUp(MemberDTO memberDTO) {

        Member member = dtoToEntity(memberDTO);

        // 이메일이나 네임을 안적었을 때 예외 발생
        if (member.getEmail() == null || member.getUsername() == null) {
            throw new RuntimeException("Invalid argument");
        }
        final String email = member.getEmail();
        final String username = member.getUsername();
        
        // 이메일이 이미 존재할 시 예외 발생
        if (memberRepository.existsByEmail(email)) {
            log.warn("Email already exists {}", email);
            throw new RuntimeException("Email already exists");
        }
        // 네임이 이미 존재할 시 예외 발생
        if (memberRepository.existsByUsername(username)) {
            log.warn("Username already exists {}", username);
            throw new RuntimeException("Username already exists");
        }
        memberRepository.save(member);
        log.info("member = " + member);
        return member.getId();
    }

    @Override
    public MemberDTO findMember(String email) {
       Member result = memberRepository.findByEmail(email);

       return result == null ? entitiesToDTO(result) : null;
        //return result.isPresent() ? entitiesToDTO(result.get()) : null;
    }

    @Override
    public String getServerInfo() {
        return String.format("현재 시각은 %s", new Date());
    }

}
