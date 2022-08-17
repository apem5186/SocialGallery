package com.socialgallery.gallerybackend.controller.post;

import com.socialgallery.gallerybackend.advice.exception.PostNotFoundCException;
import com.socialgallery.gallerybackend.config.file.FileHandler;
import com.socialgallery.gallerybackend.dto.image.ImageDTO;
import com.socialgallery.gallerybackend.dto.image.ImageResponseDTO;
import com.socialgallery.gallerybackend.dto.image.PostFileVO;
import com.socialgallery.gallerybackend.dto.post.PostListResponseDTO;
import com.socialgallery.gallerybackend.dto.post.PostRequestDTO;
import com.socialgallery.gallerybackend.dto.post.PostResponseDTO;
import com.socialgallery.gallerybackend.dto.post.PostUpdateRequestDTO;
import com.socialgallery.gallerybackend.entity.image.Image;
import com.socialgallery.gallerybackend.entity.post.Post;
import com.socialgallery.gallerybackend.entity.user.Users;
import com.socialgallery.gallerybackend.model.response.ListResult;
import com.socialgallery.gallerybackend.model.response.SingleResult;
import com.socialgallery.gallerybackend.repository.ImageRepository;
import com.socialgallery.gallerybackend.repository.PostRepository;
import com.socialgallery.gallerybackend.repository.UserRepository;
import com.socialgallery.gallerybackend.service.image.ImageService;
import com.socialgallery.gallerybackend.service.post.PostService;
import com.socialgallery.gallerybackend.service.response.ResponseService;
import com.socialgallery.gallerybackend.service.user.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Api(tags = {"4. post"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    private final ImageService imageService;

    private final UsersService usersService;

    private final UserRepository userRepository;

    private final ImageRepository imageRepository;

    private final PostRepository postRepository;

    private final FileHandler fileHandler;

    private final ResponseService responseService;

    @ApiOperation(value = "업로드", notes = "게시글 업로드를 합니다.")
    @PostMapping("/post/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public SingleResult<Long> post(
            @ApiParam(value = "게시글 등록 DTO", required = true)
            @RequestBody PostRequestDTO postRequestDTO,
            PostFileVO postFileVO) throws Exception{
        Optional<Users> users = userRepository.findById(Long.parseLong(postFileVO.getUsersId()));
        postRequestDTO = PostRequestDTO.builder()
                .users(users.orElseThrow())
                .title(postFileVO.getTitle())
                .content(postFileVO.getContent())
                .build();
        Long pid = postService.post(postRequestDTO, postFileVO.getFiles());
        return responseService.getSingleResult(pid);
    }

    @ApiOperation(value = "수정", notes = "게시글을 수정합니다.")
    @PutMapping("/post/modify/{pid}")
    public SingleResult<Long> update(
            @ApiParam(value = "게시글 수정 DTO", required = true)
            @PathVariable Long pid,
            @RequestBody PostRequestDTO postRequestDTO,
            PostFileVO postFileVO) throws Exception {

        postRequestDTO = PostRequestDTO.builder()
                .title(postFileVO.getTitle())
                .content(postFileVO.getContent())
                .build();

        // DB에 저장되어 있는 파일 불러오기
        List<Image> dbImageList = imageRepository.findAllByPostPid(pid);
        // 전달되어온 파일들
        List<MultipartFile> multipartList = postFileVO.getFiles();
        // 새롭게 전달되어온 파일들의 목록을 저장할 list 선언
        List<MultipartFile> addFileList = new ArrayList<>();

        if (CollectionUtils.isEmpty(dbImageList)) { // DB에 아예 존재 x
            if (!CollectionUtils.isEmpty(multipartList)) { // 전달되어온 파일이 하나라도 존재
                // 저장할 파일 목록에 추가
                addFileList.addAll(multipartList);
            }
        } else {  // DB에 한 장 이상 존재
            if (CollectionUtils.isEmpty(multipartList)) { // 전달되어온 파일 아예 x
                // 파일 삭제
                for (Image dbImage : dbImageList)
                    imageService.deleteImage(dbImage.getIid());
            } else {  // 전달되어온 파일 한 장 이상 존재

                // DB에 저장되어있는 파일 원본명 목록
                List<String> dbOriginNameList = new ArrayList<>();

                // DB의 파일 원본명 추출
                for (Image dbImage : dbImageList) {
                    // file id로 DB에 저장된 파일 정보 얻어오기
                    ImageDTO dbImageDTO = imageService.findByFileIid(dbImage.getIid());
                    // DB의 파일 원본명 얻어오기
                    String dbOriginFileName = dbImageDTO.getOriginFilename();

                    if (!multipartList.contains(dbOriginFileName))  // 서버에 저장된 파일들 중 전달되어온 파일이 존재하지 않는다면
                        imageService.deleteImage(dbImage.getIid());  // 파일 삭제
                    else  // 그것도 아니라면
                        dbOriginNameList.add(dbOriginFileName);    // DB에 저장할 파일 목록에 추가
                }

                for (MultipartFile multipartFile : multipartList) { // 전달되어온 파일 하나씩 검사
                    // 파일의 원본명 얻어오기
                    String multipartOriginName = multipartFile.getOriginalFilename();
                    if (!dbOriginNameList.contains(multipartOriginName)) {   // DB에 없는 파일이면
                        addFileList.add(multipartFile); // DB에 저장할 파일 목록에 추가
                    }
                }
            }
        }
            return responseService.getSingleResult(postService.update(pid, postRequestDTO, addFileList));
    }


    // 개별 조회
    @ApiOperation(value = "단일 검색", notes = "게시글 하나를 검색합니다.")
    @GetMapping("/post/{pid}")
    public SingleResult<PostResponseDTO> searchById(
            @ApiParam(value = "게시글 번호", required = true)
            @PathVariable Long pid) {
            // 게시글 id로 해당 게시글 첨부파일 전체 조회
            List<ImageResponseDTO> imageResponseDTOList =
                    imageService.findAllByPost(pid);
            // 게시글 첨부파일 id 담을 List 객체 생성
            List<Long> imageId = new ArrayList<>();
            // 각 첨부파일 id 추가
            for(ImageResponseDTO imageResponseDTO : imageResponseDTOList)
                imageId.add(imageResponseDTO.getFileId());

            // 게시글 id와 첨부파일 id 목록 전달받아 결과 반환
            return responseService.getSingleResult(postService.searchById(pid, imageId));
        }

    // 전체 조회
    @ApiOperation(value = "전체 조회", notes = "게시글 전체를 검색합니다.")
    @GetMapping("/post")
    public ListResult<PostListResponseDTO> searchAllDesc() {
        // 게시글 전체 조회
        List<Post> postList = postService.searchAllDesc();
        // 반환할 List<BoardListResponseDto> 생성
        List<PostListResponseDTO> responseDTOList = new ArrayList<>();

        for(Post post : postList){
            // 전체 조회하여 획득한 각 게시글 객체를 이용하여 BoardListResponseDto 생성
            PostListResponseDTO responseDto = new PostListResponseDTO(post);
            responseDTOList.add(responseDto);
        }

        return responseService.getListResult(responseDTOList);
    }

    // 게시글 삭제
    @ApiOperation(value = "게시글 삭제", notes = "게시글을 삭제합니다.")
    @DeleteMapping("/post/delete/{pid}")
    public SingleResult<Long> delete(
            @ApiParam(value = "게시글 번호", required = true)
            @PathVariable Long pid,
            @RequestBody PostRequestDTO postRequestDTO) {
        return responseService.getSingleResult(postService.delete(pid, postRequestDTO));
    }


}
