package com.socialgallery.gallerybackend.service;

import com.socialgallery.gallerybackend.dto.MemberDTO;
import com.socialgallery.gallerybackend.entity.member.Member;

import java.util.HashMap;
import java.util.Map;

public interface MemberService {

    Long signUp(MemberDTO memberDTO);

    default MemberDTO entitiesToDTO(Member member) {
        MemberDTO memberDTO = MemberDTO.builder()
                .nickname(member.getNickname())
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
                .nickname(memberDTO.getNickname())
                .email(memberDTO.getEmail())
                .password(memberDTO.getPassword())
                .picture(memberDTO.getPicture())
                .phone(memberDTO.getPhone())
                .build();

        entityMap.put("user", member);

        return entityMap;
    }
}
