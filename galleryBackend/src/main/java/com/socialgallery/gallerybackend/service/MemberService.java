package com.socialgallery.gallerybackend.service;

import com.socialgallery.gallerybackend.dto.MemberDTO;
import com.socialgallery.gallerybackend.entity.member.Member;
import com.socialgallery.gallerybackend.entity.member.MemberRole;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

public interface MemberService {

    Member signUp(MemberDTO memberDTO);

    Member signIn(String email, String password);

    MemberDTO findMember(String email);

    public String getServerInfo();

    default MemberDTO entitiesToDTO(Member member) {
        MemberDTO memberDTO = MemberDTO.builder()
                .username(member.getUsername())
                .email(member.getEmail())
                .password(member.getPassword())
                .picture(member.getPicture())
                .phone(member.getPhone())
                .regDate(member.getRegDate())
                .modDate(member.getModDate())
                .build();

        return memberDTO;
    }

    default Member dtoToEntity(MemberDTO memberDTO) {

        Member member = Member.builder()
                .username(memberDTO.getUsername())
                .email(memberDTO.getEmail())
                .password(memberDTO.getPassword())
                .picture(memberDTO.getPicture())
                .phone(memberDTO.getPhone())
                .build();
        member.addMemberRole(MemberRole.ROLE_MEMBER);

        return member;
    }

}
