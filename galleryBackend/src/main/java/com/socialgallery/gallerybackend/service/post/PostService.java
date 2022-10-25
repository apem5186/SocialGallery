package com.socialgallery.gallerybackend.service.post;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.socialgallery.gallerybackend.advice.exception.PostNotFoundCException;
import com.socialgallery.gallerybackend.advice.exception.RefreshTokenCException;
import com.socialgallery.gallerybackend.advice.exception.UserNotFoundCException;
import com.socialgallery.gallerybackend.config.file.FileHandler;
import com.socialgallery.gallerybackend.config.security.JwtProvider;
import com.socialgallery.gallerybackend.dto.image.ImageDTO;
import com.socialgallery.gallerybackend.dto.jwt.TokenRequestDTO;
import com.socialgallery.gallerybackend.dto.post.PostRequestDTO;
import com.socialgallery.gallerybackend.dto.post.PostResponseDTO;
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
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * @Reference https://velog.io/@yu-jin-song/Spring-Boot-%EA%B2%8C%EC%8B%9C%ED%8C%90-%EA%B5%AC%ED%98%84-5-%EA%B2%8C%EC%8B%9C%EA%B8%80-%EC%88%98%EC%A0%95-%EB%B0%8F-%EC%82%AD%EC%A0%9C-%EB%8B%A4%EC%A4%91-%ED%8C%8C%EC%9D%BC%EC%9D%B4%EB%AF%B8%EC%A7%80-%EB%B0%98%ED%99%98-%EB%B0%8F-%EC%A1%B0%ED%9A%8C-%EC%B2%98%EB%A6%AC-MultipartFile,
 * https://pjh3749.tistory.com/187
 * https://velog.io/@hyemco/TIL-AWS-Spring-boot-S3%EB%A1%9C-%EC%9D%B4%EB%AF%B8%EC%A7%80-%EC%97%85%EB%A1%9C%EB%93%9C
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

    @Value("${cloud.aws.s3.dir}")
    private String dir;

    private final AmazonS3 amazonS3;

    @Transactional
    public List<String > upload(List<MultipartFile> files, PostRequestDTO postRequestDTO,
                                HttpServletRequest request) {
        List<String > imgUrlList = new ArrayList<>();
        List<String > result = new ArrayList<>();

        // 파일 DTO 생성
        Post entity = postRequestDTO.toEntity();
        log.info("ENTITY : " + entity);
        if (checkToken(postRequestDTO.getUsers().getId(), request)) {
            if(files.isEmpty()) {
                postRepository.save(entity);
                result.add(String.valueOf(entity.getPid()));
                return result;
            }
            for (MultipartFile file : files) {
                // 파일명을 업로드 한 날짜로 변환하여 저장
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter dateTimeFormatter =
                        DateTimeFormatter.ofPattern("yyyyMMdd");
                String current_date = now.format(dateTimeFormatter);
                File folder = new File(bucket + dir + current_date);

                // 디렉터리가 없을 경우
                if (!folder.exists()) {
                    boolean wasSuccessful = folder.mkdirs();

                    // 디렉터리 생성에 실패했을 경우
                    if (!wasSuccessful)
                        System.out.println("file: was not successful");
                }

                String fileName = createFileName(file.getOriginalFilename());

                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentLength(file.getSize());
                objectMetadata.setContentType(file.getContentType());

                try(InputStream inputStream = file.getInputStream()) {
                    amazonS3.putObject(new PutObjectRequest(bucket+"/images/"+current_date, fileName, inputStream, objectMetadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead));
                    imgUrlList.add(amazonS3.getUrl(bucket+"/images/"+current_date, fileName).toString());

                    ImageDTO imageDTO = ImageDTO.builder()
                            .originFilename(file.getOriginalFilename())
                            .filePath(amazonS3.getUrl(bucket+"/images/"+current_date, fileName).toString())
                            .fileSize(file.getSize())
                            .build();

                    // 파일 DTO 이용하여 Image 엔티티 생성
                    Image image = new Image(
                            imageDTO.getOriginFilename(),
                            imageDTO.getFilePath(),
                            imageDTO.getFileSize()
                    );

                    entity.addImage(imageRepository.save(image));
                    postRepository.save(entity);

                } catch (AmazonServiceException ase) {
                    log.info("Caught an AmazonServiceException from PUT requests, rejected reasons:");
                    log.info("Error Message : " + ase.getErrorCode());
                    log.info("HTTP Status Code : " + ase.getStatusCode());
                    log.info("AWS Error Code : " + ase.getErrorCode());
                    log.info("Error Type : " + ase.getErrorType());
                    log.info("Request ID : " + ase.getRequestId());
                    log.info("Service Name : " + ase.getServiceName());
                } catch (AmazonClientException ace) {
                    log.info("Caught an AmazonClientException: ");
                    log.info("Error Message : " + ace.getMessage());
                } catch (IOException e) {
                    log.info("=====================IMAGE TEST======================");
                    log.info("bucket : " + bucket);
                    log.info("objMeta : " + objectMetadata);
                    e.printStackTrace();
                    log.info("=====================IMAGE TEST======================");
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return imgUrlList;
        } throw new IllegalArgumentException("다시 로그인을 해야합니다.");
    }

    // s3 게시글 수정
//    @Transactional
//    public Long postUpdate(Long pid, PostRequestDTO postRequestDTO, List<MultipartFile> files,
//                           HttpServletRequest request) throws Exception {
//        Post post = postRepository.findById(pid).orElseThrow(PostNotFoundCException::new);
//        if (checkToken(post.getUsers().getId(), request)) {
//            List<Image> imageList = fileHandler.parseFileInfo(files, post);
//
//            if (!imageList.isEmpty()) {
//                imageRepository.saveAll(imageList);
//            }
//            post.update(postRequestDTO.getTitle(), postRequestDTO.getContent());
//        }
//    }

    // 이미지파일명 중복 방지
    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    // 파일 유효성 검사
    private String getFileExtension(String fileName) {
        if (fileName.length() == 0) {
            throw new IllegalArgumentException("파일이 존재하지 않습니다.");
        }
        ArrayList<String> fileValidate = new ArrayList<>();
        fileValidate.add(".jpg");
        fileValidate.add(".jpeg");
        fileValidate.add(".png");
        fileValidate.add(".JPG");
        fileValidate.add(".JPEG");
        fileValidate.add(".PNG");
        String idxFileName = fileName.substring(fileName.lastIndexOf("."));
        if (!fileValidate.contains(idxFileName)) {
            throw new IllegalArgumentException("적합한 확장자가 아닙니다.");
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    // 게시글 등록
//    @Transactional
//    public Long post(PostRequestDTO postRequestDTO,
//                     List<MultipartFile> files,
//                     HttpServletRequest request) throws Exception{
//
//        if (checkToken(postRequestDTO.getUsers().getId(), request)) {
//            Post entity = postRequestDTO.toEntity();
//            log.info("ENTITY : " + entity);
//            List<Image> imageList = fileHandler.parseFileInfo(files, entity);
//            ObjectMetadata objMeta = new ObjectMetadata();
//            if (!imageList.isEmpty()) {
//                for (Image image : imageList) {
//                    // i 변수같이 증가하는 값 사용할라고 사용
//                    AtomicInteger atomicInteger = new AtomicInteger(0);
//                    // 파일을 DB에 저장
//                    entity.addImage(imageRepository.save(image));
//                    files.forEach(file -> {
//                        objMeta.setContentType(file.getContentType());
//                        objMeta.setContentLength(file.getSize());
//                        try (InputStream inputStream = file.getInputStream()) {
//                            amazonS3.putObject(new PutObjectRequest(bucket, imageList.get(atomicInteger.get()).getOriginFileName(), inputStream, objMeta)
//                                    .withCannedAcl(CannedAccessControlList.PublicRead));
//                            atomicInteger.incrementAndGet();
//                        } catch (AmazonServiceException ase) {
//                            log.info("Caught an AmazonServiceException from PUT requests, rejected reasons:");
//                            log.info("Error Message : " + ase.getErrorCode());
//                            log.info("HTTP Status Code : " + ase.getStatusCode());
//                            log.info("AWS Error Code : " + ase.getErrorCode());
//                            log.info("Error Type : " + ase.getErrorType());
//                            log.info("Request ID : " + ase.getRequestId());
//                            log.info("Service Name : " + ase.getServiceName());
//                        } catch (AmazonClientException ace) {
//                            log.info("Caught an AmazonClientException: ");
//                            log.info("Error Message : " + ace.getMessage());
//                        } catch (IOException e) {
//                            log.info("=====================IMAGE TEST======================");
//                            log.info("bucket : " + bucket);
//                            log.info("objMeta : " + objMeta);
//                            e.printStackTrace();
//                            log.info("=====================IMAGE TEST======================");
//                            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다.");
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    });
//                }
//            }
//
//            return postRepository.save(entity).getPid();
//        } throw new IllegalArgumentException("다시 로그인을 해야합니다.");
//    }
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

    public void s3Upload(List<MultipartFile> files, Post post) {
        for (MultipartFile file : files) {
            // 파일명을 업로드 한 날짜로 변환하여 저장
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter =
                    DateTimeFormatter.ofPattern("yyyyMMdd");
            String current_date = now.format(dateTimeFormatter);
            File folder = new File(bucket + dir + current_date);

            // 디렉터리가 없을 경우
            if (!folder.exists()) {
                boolean wasSuccessful = folder.mkdirs();

                // 디렉터리 생성에 실패했을 경우
                if (!wasSuccessful)
                    System.out.println("file: was not successful");
            }

            String fileName = createFileName(file.getOriginalFilename());

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try(InputStream inputStream = file.getInputStream()) {
                amazonS3.putObject(new PutObjectRequest(bucket+"/images/"+current_date, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
                ImageDTO imageDTO = ImageDTO.builder()
                        .originFilename(file.getOriginalFilename())
                        .filePath(amazonS3.getUrl(bucket+"/images/"+current_date, fileName).toString())
                        .fileSize(file.getSize())
                        .build();

                // 파일 DTO 이용하여 Image 엔티티 생성
                Image image = new Image(
                        imageDTO.getOriginFilename(),
                        imageDTO.getFilePath(),
                        imageDTO.getFileSize()
                );
                image.setPost(post);
                imageRepository.save(image);

            } catch (AmazonServiceException ase) {
                log.info("Caught an AmazonServiceException from PUT requests, rejected reasons:");
                log.info("Error Message : " + ase.getErrorCode());
                log.info("HTTP Status Code : " + ase.getStatusCode());
                log.info("AWS Error Code : " + ase.getErrorCode());
                log.info("Error Type : " + ase.getErrorType());
                log.info("Request ID : " + ase.getRequestId());
                log.info("Service Name : " + ase.getServiceName());
            } catch (AmazonClientException ace) {
                log.info("Caught an AmazonClientException: ");
                log.info("Error Message : " + ace.getMessage());
            } catch (IOException e) {
                log.info("=====================IMAGE TEST======================");
                log.info("bucket : " + bucket);
                log.info("objMeta : " + objectMetadata);
                e.printStackTrace();
                log.info("=====================IMAGE TEST======================");
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    // 게시글 수정
    @Transactional
    public Long update(Long pid, PostRequestDTO postRequestDTO, List<MultipartFile> files,
                       HttpServletRequest request) throws Exception {

        Post post = postRepository.findById(pid).orElseThrow(PostNotFoundCException::new);
        if (checkToken(post.getUsers().getId(), request)) {
            List<Image> imgDBList;
            imgDBList = imageRepository.findAllByPostPid(pid);
            // 전달되어 온 파일이 존재할 경우
            if (!files.isEmpty()) {
                // DB에 파일 존재할 경우
                if (!imgDBList.isEmpty()) {
                    // s3와 DB에 이미지 제거
                    imgDBList.forEach(image -> {
                        String date = image.getFilePath().split("/")[5];
                        deleteImage(image.getFilePath().substring(image.getFilePath().lastIndexOf("/")+1), date);
                        imageRepository.delete(image);
                        log.info("=======================delete File S3==============================");
                        log.info("FILENAME = " + image.getFilePath().substring(image.getFilePath().lastIndexOf("/")+1) +
                                "DATE = " + date);
                        log.info("=======================delete File S3==============================");
                    });
                    log.info("===============files TEST=================");
                    log.info("FILES TEST : " + !files.isEmpty());
                    log.info("===============files TEST=================");
                    s3Upload(files, post);
                } else { // DB에 파일이 없을 경우
                    log.info("===============files TEST=================");
                    log.info("FILES TEST : " + !files.isEmpty());
                    log.info("===============files TEST=================");
                    s3Upload(files, post);
                }

            } else {    // 전달되어 온 파일이 없을 경우
                // DB에 파일이 존재할 경우
                if (!imgDBList.isEmpty()) {
                    // s3와 DB에 이미지 제거
                    imgDBList.forEach(image -> {
                        String date = image.getFilePath().split("/")[5];
                        deleteImage(image.getFilePath().substring(image.getFilePath().lastIndexOf("/")+1), date);
                        imageRepository.delete(image);
                        log.info("=======================delete File S3==============================");
                        log.info("FILENAME = " + image.getFilePath().substring(image.getFilePath().lastIndexOf("/")+1) +
                                "DATE = " + date);
                        log.info("=======================delete File S3==============================");
                    });
                }   // DB에 파일이 없을 경우 코드 불필요
            }

            // title이랑 content는 항상 수정
            post.update(postRequestDTO.getTitle(), postRequestDTO.getContent());
            // 오류 방지용
            return pid;
        } throw new IllegalArgumentException("다시 로그인을 해야합니다.");
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
                    String date = image.getFilePath().split("/")[5];
                    deleteImage(image.getFilePath().substring(image.getFilePath().lastIndexOf("/")+1), date);
                    log.info("=======================delete File S3==============================");
                    log.info("FILENAME = " + image.getFilePath().substring(image.getFilePath().lastIndexOf("/")+1) +
                            "DATE = " + date);
                    log.info("=======================delete File S3==============================");
                });
            }
            postRepository.delete(post);
        }
        return pid;
    }

    // s3 bucket image delete
    public void deleteImage(String fileName, String date) {
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket+"/images/" + date, fileName);
        amazonS3.deleteObject(deleteObjectRequest);
    }
}
