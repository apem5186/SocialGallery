package com.socialgallery.gallerybackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {

    private String username;

    private String email;

    private String password;

    private String picture;

    private String phone;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

}
