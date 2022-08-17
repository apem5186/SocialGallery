package com.socialgallery.gallerybackend.controller.image;

import com.socialgallery.gallerybackend.dto.image.ImageDTO;
import com.socialgallery.gallerybackend.entity.image.Image;
import com.socialgallery.gallerybackend.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
@RestController
public class ImageController {

    private final ImageService imageService;

    /**
     * 썸네일용 이미지 조회
     */
    @CrossOrigin
    @GetMapping(
            value = "/thumbnail/{iid}",
            produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE}
    )
    public ResponseEntity<byte[]> getThumbnail(@PathVariable Long iid) throws IOException {

        // 이미지가 저장된 절대경로 추출
        String absolutePath =
                new File("").getAbsolutePath() + File.separator + File.separator;
        String path;

        if (iid != 0) { // 전달되어 온 이미지가 기본 썸네일이 아닐 경우
            ImageDTO imageDTO = imageService.findByFileIid(iid);
            path = imageDTO.getFilePath();
        } else {    // 전달되어 온 이미지가 기본 썸네일일 경우
            path = "images" + File.separator + "thumbnail" + File.separator + "thumbnail.png";
        }

        // FileInputstream의 객체를 생성하여
        // 이미지가 저장된 경로를 byte[] 형태의 값으로 encoding
        InputStream imageStream = new FileInputStream(absolutePath + path);
        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
        imageStream.close();

        return new ResponseEntity<>(imageByteArray, HttpStatus.OK);
    }

    /**
     * 이미지 개별 조회
     */
    @CrossOrigin
    @GetMapping(
            value = "/image/{iid}",
            produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE}
    )
    public ResponseEntity<byte[]> getImage(@PathVariable Long iid) throws IOException {
        ImageDTO imageDTO = imageService.findByFileIid(iid);
        String absolutePath
                = new File("").getAbsolutePath() + File.separator + File.separator;
        String path = imageDTO.getFilePath();

        InputStream imageStream = new FileInputStream(absolutePath + path);
        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
        imageStream.close();

        return new ResponseEntity<>(imageByteArray, HttpStatus.OK);
    }
}
