package com.socialgallery.gallerybackend.service;

import com.socialgallery.gallerybackend.dto.MemberDTO;
import com.socialgallery.gallerybackend.entity.member.Member;
import com.socialgallery.gallerybackend.entity.member.MemberRole;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface MemberService {

    Long signUp(MemberDTO memberDTO);

    MemberDTO findMember(String email);

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

    default Map<String, Object> dtoToEntity(MemberDTO memberDTO) {
        Map<String, Object> entityMap = new HashMap<>();

        Member member = Member.builder()
                .username(memberDTO.getUsername())
                .email(memberDTO.getEmail())
                .password(memberDTO.getPassword())
                .picture(memberDTO.getPicture())
                .phone(memberDTO.getPhone())
                .build();
        member.addMemberRole(MemberRole.ROLE_MEMBER);
        entityMap.put("user", member);

        return entityMap;
    }
}
