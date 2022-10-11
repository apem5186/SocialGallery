package com.socialgallery.gallerybackend.config.file;

import com.amazonaws.services.s3.AmazonS3Client;
import com.socialgallery.gallerybackend.dto.image.ImageDTO;
import com.socialgallery.gallerybackend.entity.image.Image;
import com.socialgallery.gallerybackend.entity.post.Post;
import com.socialgallery.gallerybackend.service.image.ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/***
 * @Reference https://velog.io/@yu-jin-song/SpringBoot-%EA%B2%8C%EC%8B%9C%ED%8C%90-%EA%B5%AC%ED%98%84-4-MultipartFile-%EB%8B%A4%EC%A4%91-%ED%8C%8C%EC%9D%BC%EC%9D%B4%EB%AF%B8%EC%A7%80-%EC%97%85%EB%A1%9C%EB%93%9C
 */

@Component
public class FileHandler {

    private final ImageService imageService;

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket-name}")
    private String S3Bucket;

    @Value("${cloud.aws.s3.dir}")
    private String dir;

    public FileHandler(ImageService imageService, AmazonS3Client amazonS3Client) {
        this.imageService = imageService;
        this.amazonS3Client = amazonS3Client;
    }

    public List<Image> parseFileInfo(
            List<MultipartFile> multipartFiles,
            Post post   // post에 존재하는 파일인지 확인하기 위함
    ) throws Exception {
        // 반환할 파일 리스트
        List<Image> fileList = new ArrayList<>();

        // 전달되어 온 파일이 존재할 경우
        if (!CollectionUtils.isEmpty(multipartFiles)) {
            // 파일명을 업로드 한 날짜로 변환하여 저장
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter =
                    DateTimeFormatter.ofPattern("yyyyMMdd");
            String current_date = now.format(dateTimeFormatter);

            // 프로젝트 디렉터리 내의 저장을 위한 절대 경로 설정
            // 경로 구분자 File.separator 사용
//            String absolutePath = new File("galleryfrontend"
//            + File.separator + File.separator + "public" + File.separator + File.separator
//            + "assets" + File.separator + File.separator + "Img").getAbsolutePath() + File.separator + File.separator;

            // 배포용 절대 경로
            String absolutePath = new File(S3Bucket + File.separator + File.separator + dir).getAbsolutePath();
            // 파일을 저장할 세부 경로 지정
            String path = "images" + File.separator + current_date;
            File file = new File(absolutePath + path);

            // 디렉터리가 존재하지 않을 경우
            if (!file.exists()) {
                boolean wasSuccessful = file.mkdirs();

                // 디렉터리 생성에 실패했을 경우
                if (!wasSuccessful)
                    System.out.println("file: was not successful");
            }

            // 다중 파일 처리
            for (MultipartFile multipartFile : multipartFiles) {

                // 파일의 확장자 추출
                String originalFileExtension;
                String contentType = multipartFile.getContentType();

                // 확장자명이 존재하지 않을 경우 처리 x
                if (ObjectUtils.isEmpty(contentType)) {
                    break;
                } else {  // 확장자가 jpeg, png인 파일들만 받아서 처리
                    if (contentType.contains("image/jpeg"))
                        originalFileExtension = ".jpg";
                    else if (contentType.contains("image/png"))
                        originalFileExtension = ".png";
                    else  // 다른 확장자일 경우 처리 x
                        break;
                }

                // 파일명 중복 피하고자 나노초까지 얻어와 지정
                String new_file_name = System.nanoTime() + originalFileExtension;

                // 파일 DTO 생성
                ImageDTO imageDTO = ImageDTO.builder()
                        .originFilename(multipartFile.getOriginalFilename())
                        .filePath(path + File.separator + new_file_name)
                        .fileSize(multipartFile.getSize())
                        .build();

                // 파일 DTO 이용하여 Image 엔티티 생성
                Image image = new Image(
                        imageDTO.getOriginFilename(),
                        imageDTO.getFilePath(),
                        imageDTO.getFileSize()
                );

                // 게시글에 존재 x -> 게시글에 사진 정보 저장
                if(post.getPid() != null) {
                    image.setPost(post);
                }

                // 생성 후 리스트에 추가
                fileList.add(image);

                // 업로드 한 파일 데이터를 지정한 파일에 저장
                file = new File(absolutePath + path + File.separator + new_file_name);
                multipartFile.transferTo(file);

                // 파일 권한 설정(쓰기, 읽기)
                file.setWritable(true);
                file.setReadable(true);
            }
        }

        return fileList;
    }
}
