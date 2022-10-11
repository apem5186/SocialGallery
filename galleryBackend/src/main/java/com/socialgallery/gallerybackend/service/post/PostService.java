package com.socialgallery.gallerybackend.service.post;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.socialgallery.gallerybackend.advice.exception.AuthenticationEntryPointCException;
import com.socialgallery.gallerybackend.advice.exception.PostNotFoundCException;
import com.socialgallery.gallerybackend.advice.exception.RefreshTokenCException;
import com.socialgallery.gallerybackend.advice.exception.UserNotFoundCException;
import com.socialgallery.gallerybackend.config.file.FileHandler;
import com.socialgallery.gallerybackend.config.security.JwtProvider;
import com.socialgallery.gallerybackend.dto.jwt.TokenDTO;
import com.socialgallery.gallerybackend.dto.jwt.TokenRequestDTO;
import com.socialgallery.gallerybackend.dto.post.PostRequestDTO;
import com.socialgallery.gallerybackend.dto.post.PostResponseDTO;
import com.socialgallery.gallerybackend.dto.user.UserRequestDTO;
import com.socialgallery.gallerybackend.dto.user.UserResponseDTO;
import com.socialgallery.gallerybackend.entity.image.Image;
import com.socialgallery.gallerybackend.entity.post.Category;
import com.socialgallery.gallerybackend.entity.post.Post;
import com.socialgallery.gallerybackend.entity.security.RefreshToken;
import com.socialgallery.gallerybackend.entity.security.RefreshTokenJpaRepo;
import com.socialgallery.gallerybackend.entity.user.Users;
import com.socialgallery.gallerybackend.repository.ImageRepository;
import com.socialgallery.gallerybackend.repository.PostRepository;
import com.socialgallery.gallerybackend.repository.UserRepository;
import com.socialgallery.gallerybackend.service.security.SignService;
import com.socialgallery.gallerybackend.service.user.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
 * @Reference https://velog.io/@yu-jin-song/Spring-Boot-%EA%B2%8C%EC%8B%9C%ED%8C%90-%EA%B5%AC%ED%98%84-5-%EA%B2%8C%EC%8B%9C%EA%B8%80-%EC%88%98%EC%A0%95-%EB%B0%8F-%EC%82%AD%EC%A0%9C-%EB%8B%A4%EC%A4%91-%ED%8C%8C%EC%9D%BC%EC%9D%B4%EB%AF%B8%EC%A7%80-%EB%B0%98%ED%99%98-%EB%B0%8F-%EC%A1%B0%ED%9A%8C-%EC%B2%98%EB%A6%AC-MultipartFile
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final ImageRepository imageRepository;

    private final UserRepository userRepository;

    private final RefreshTokenJpaRepo refreshTokenJpaRepo;

    private final UsersService usersService;

    private final JwtProvider jwtProvider;

    private final SignService signService;

    private final FileHandler fileHandler;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;
    // 게시글 등록
    @Transactional
    public Long post(PostRequestDTO postRequestDTO,
                     List<MultipartFile> files,
                     HttpServletRequest request) throws Exception{

        if (checkToken(postRequestDTO.getUsers().getId(), request)) {
            Post entity = postRequestDTO.toEntity();
            log.info("ENTITY : " + entity);
            List<Image> imageList = fileHandler.parseFileInfo(files, entity);
            ObjectMetadata objMeta = new ObjectMetadata();
            if (!imageList.isEmpty()) {
                for (Image image : imageList) {
                    // 파일을 DB에 저장
                    entity.addImage(imageRepository.save(image));
                    files.forEach(file -> {
                        try (InputStream inputStream = file.getInputStream()) {
                            amazonS3.putObject(new PutObjectRequest(bucket, image.getOriginFileName(), inputStream, objMeta)
                                    .withCannedAcl(CannedAccessControlList.PublicRead));
                        } catch (IOException e) {
                            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다.");
                        }
                    });
                }
            }

            return postRepository.save(entity).getPid();
        } throw new IllegalArgumentException("다시 로그인을 해야합니다.");
    }
    @Transactional
    public boolean checkToken(Long id, HttpServletRequest request) {

        Users users = userRepository.findById(id).orElseThrow(UserNotFoundCException::new);
        UserResponseDTO userPk = usersService.findByUsername(users.getUsername());
        Optional<RefreshToken> refreshToken = refreshTokenJpaRepo.findByKey(userPk.getId());
        String accessToken = jwtProvider.resolveToken(request);
        log.info("ACCESSTOKEN : " + accessToken);
        String rtoken = refreshToken.orElseThrow().getToken();
        log.info("REFRESHTOKEN : " + rtoken);
        if (!jwtProvider.validationToken(rtoken)) {
            request.setAttribute("Authorization", "");
            signService.logout(id);
            throw new RefreshTokenCException();
        }
        if (!jwtProvider.validationToken(accessToken)) {
            TokenRequestDTO tokenRequestDTO = TokenRequestDTO.builder()
                            .accessToken(accessToken)
                                    .refreshToken(rtoken)
                                            .build();
            signService.reissue(tokenRequestDTO);
            return true;
        }
        return true;
    }
    // 게시글 수정
    @Transactional
    public Long update(Long pid, PostRequestDTO postRequestDTO, List<MultipartFile> files,
                       HttpServletRequest request) throws Exception{

        Post post = postRepository.findById(pid).orElseThrow(PostNotFoundCException::new);
        if(checkToken(post.getUsers().getId(), request)) {
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
    public Page<Post> searchAllDesc(Pageable pageable) {
        return postRepository.findAllByOrderByPidDesc(pageable);
    }
    
    // 키워드랑 검색
    @Transactional(readOnly = true)
    public Page<Post> searchByKeyword(Pageable pageable, String keyword) {

        return postRepository.findByTitleContaining(keyword, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Post> searchByCategory(Pageable pageable, Category category) {
        return postRepository.findByCategory(pageable, category);
    }

    @Transactional(readOnly = true)
    public Page<Post> searchByKeywordWithCategory(Pageable pageable, String keyword, Category category) {
        return postRepository.findByTitleAndCategory(pageable, keyword, category);
    }

    // 게시글 삭제
    @Transactional
    public Long delete(Long pid, HttpServletRequest request) {

        Post post = postRepository.findById(pid).orElseThrow(PostNotFoundCException::new);
        List<Image> images = imageRepository.findAllByPostPid(pid);
        if (checkToken(post.getUsers().getId(), request)) {
            if (!images.isEmpty()) {
                images.forEach(image -> {
                    deleteImage(image.getOriginFileName());
                });
            }
            postRepository.delete(post);
        }
        return pid;
    }

    // s3 bucket image delete
    public void deleteImage(String fileName) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }
}
