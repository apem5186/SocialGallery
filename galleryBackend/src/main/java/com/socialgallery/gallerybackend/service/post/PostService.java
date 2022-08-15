package com.socialgallery.gallerybackend.service.post;

import com.socialgallery.gallerybackend.config.security.JwtProvider;
import com.socialgallery.gallerybackend.dto.post.PostRequestDTO;
import com.socialgallery.gallerybackend.dto.user.UserResponseDTO;
import com.socialgallery.gallerybackend.entity.post.Post;
import com.socialgallery.gallerybackend.entity.security.RefreshToken;
import com.socialgallery.gallerybackend.entity.security.RefreshTokenJpaRepo;
import com.socialgallery.gallerybackend.repository.PostRepository;
import com.socialgallery.gallerybackend.service.user.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final RefreshTokenJpaRepo refreshTokenJpaRepo;

    private final UsersService usersService;

    private final JwtProvider jwtProvider;

    @Transactional
    public Long post(PostRequestDTO postRequestDTO) {
        String writer = postRequestDTO.getWriter();
        UserResponseDTO userPk = usersService.findByUsername(writer);
        Optional<RefreshToken> refreshToken = refreshTokenJpaRepo.findByKey(userPk.getId());
        String token = refreshToken.orElseThrow().getToken();

        if (jwtProvider.validationToken(token)) {
            Post entity = postRequestDTO.toEntity();
            postRepository.save(entity);

            return entity.getPid();
        }
        return null;
    }
}
