package com.socialgallery.gallerybackend.service.post;

import com.socialgallery.gallerybackend.advice.exception.PostNotFoundCException;
import com.socialgallery.gallerybackend.advice.exception.RefreshTokenCException;
import com.socialgallery.gallerybackend.advice.exception.UserNotFoundCException;
import com.socialgallery.gallerybackend.config.file.FileHandler;
import com.socialgallery.gallerybackend.config.security.JwtProvider;
import com.socialgallery.gallerybackend.dto.post.PostListResponseDTO;
import com.socialgallery.gallerybackend.dto.post.PostRequestDTO;
import com.socialgallery.gallerybackend.dto.post.PostResponseDTO;
import com.socialgallery.gallerybackend.dto.post.PostUpdateRequestDTO;
import com.socialgallery.gallerybackend.dto.user.UserResponseDTO;
import com.socialgallery.gallerybackend.entity.image.Image;
import com.socialgallery.gallerybackend.entity.post.Post;
import com.socialgallery.gallerybackend.entity.security.RefreshToken;
import com.socialgallery.gallerybackend.entity.security.RefreshTokenJpaRepo;
import com.socialgallery.gallerybackend.entity.user.Users;
import com.socialgallery.gallerybackend.repository.ImageRepository;
import com.socialgallery.gallerybackend.repository.PostRepository;
import com.socialgallery.gallerybackend.service.user.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final ImageRepository imageRepository;

    private final RefreshTokenJpaRepo refreshTokenJpaRepo;

    private final UsersService usersService;

    private final JwtProvider jwtProvider;

    private final FileHandler fileHandler;
    
    // 게시글 등록
    @Transactional
    public Long post(PostRequestDTO postRequestDTO,
                     List<MultipartFile> files) throws Exception{

        if (postRequestDTO.getUsers() == null) {
            throw new UserNotFoundCException();
        }
        Users users = postRequestDTO.getUsers();
        UserResponseDTO userPk = usersService.findByUsername(users.getUsername());
        Optional<RefreshToken> refreshToken = refreshTokenJpaRepo.findByKey(userPk.getId());
        String token = refreshToken.orElseThrow().getToken();

        if (jwtProvider.validationToken(token)) {
            Post entity = postRequestDTO.toEntity();
            List<Image> imageList = fileHandler.parseFileInfo(files, entity);
            if (!imageList.isEmpty()) {
                for (Image image : imageList) {
                    // 파일을 DB에 저장
                    entity.addImage(imageRepository.save(image));
                }
            }
            postRepository.save(entity);

            return entity.getPid();
        } else throw new RefreshTokenCException();

    }
    
    // 게시글 수정
    @Transactional
    public Long update(Long pid, PostRequestDTO postRequestDTO, List<MultipartFile> files) throws Exception{

        Post post = postRepository.findById(pid).orElseThrow(PostNotFoundCException::new);


        if (postRequestDTO.getUsers() == null) {
            throw new UserNotFoundCException();
        }
        Users users = postRequestDTO.getUsers();
        UserResponseDTO userPk = usersService.findByUsername(users.getUsername());
        Optional<RefreshToken> refreshToken = refreshTokenJpaRepo.findByKey(userPk.getId());
        String token = refreshToken.orElseThrow().getToken();
        if (jwtProvider.validationToken(token)) {
            List<Image> imageList = fileHandler.parseFileInfo(files, post);

            if (!imageList.isEmpty()) {
                imageRepository.saveAll(imageList);
            }
            post.update(postRequestDTO.getTitle(), postRequestDTO.getContent());
        }

        return pid;
    }

    // 단일 검색
    @Transactional(readOnly = true)
    public PostResponseDTO searchById(Long pid, List<Long> fileId) {
        Post post = postRepository.findById(pid).orElseThrow(PostNotFoundCException::new);

        return new PostResponseDTO(post, fileId);
    }

    // 복수 검색
    @Transactional(readOnly = true)
    public List<Post> searchAllDesc() {
        return postRepository.findAllByOrderByPidDesc();
    }

    // 게시글 삭제
    @Transactional
    public Long delete(Long pid, PostRequestDTO postRequestDTO) {
        if (postRequestDTO.getUsers() == null) {
            throw new UserNotFoundCException();
        }
        Users users = postRequestDTO.getUsers();
        UserResponseDTO userPk = usersService.findByUsername(users.getUsername());
        Optional<RefreshToken> refreshToken = refreshTokenJpaRepo.findByKey(userPk.getId());
        String token = refreshToken.orElseThrow().getToken();
        if (jwtProvider.validationToken(token)) {
            Post post = postRepository.findById(pid).orElseThrow(PostNotFoundCException::new);
            postRepository.delete(post);
        }

        return pid;
    }
}
