package com.socialgallery.gallerybackend.service;

import com.socialgallery.gallerybackend.dto.MemberDTO;
import com.socialgallery.gallerybackend.entity.member.Member;
import com.socialgallery.gallerybackend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public Long signUp(MemberDTO memberDTO) {
        Map<String, Object> entityMap = dtoToEntity(memberDTO);

        Member member = (Member) entityMap.get("user");

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
        Optional<Member> result = memberRepository.findByEmail(email);

        return result.isPresent() ? entitiesToDTO(result.get()) : null;
    }

}
