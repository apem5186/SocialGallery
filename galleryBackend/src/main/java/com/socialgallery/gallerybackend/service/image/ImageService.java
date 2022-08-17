package com.socialgallery.gallerybackend.service.image;

import com.socialgallery.gallerybackend.advice.exception.ImageNotFoundCException;
import com.socialgallery.gallerybackend.dto.image.ImageDTO;
import com.socialgallery.gallerybackend.dto.image.ImageResponseDTO;
import com.socialgallery.gallerybackend.entity.image.Image;
import com.socialgallery.gallerybackend.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ImageService {
    private final ImageRepository imageRepository;

    /**
     * 이미지 개별 조회
     */
    @Transactional(readOnly = true)
    public ImageDTO findByFileIid(Long iid) {
        Image image = imageRepository.findById(iid).orElseThrow(ImageNotFoundCException::new);

        ImageDTO imageDTO = ImageDTO.builder()
                .originFilename(image.getOriginFileName())
                .filePath(image.getFilePath())
                .fileSize(image.getFileSize())
                .build();

        return imageDTO;
    }

    /**
     * 이미지 전체 조회
     */
    @Transactional(readOnly = true)
    public List<ImageResponseDTO> findAllByPost(Long pid) {
        List<Image> imageList = imageRepository.findAllByPostPid(pid);

        return imageList.stream()
                .map(ImageResponseDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * 이미지 삭제
     */
    @Transactional
    public Image deleteImage(Long iid) {

        Image image = imageRepository.findById(iid).orElseThrow(ImageNotFoundCException::new);
        imageRepository.delete(image);

        return image;
    }
}
