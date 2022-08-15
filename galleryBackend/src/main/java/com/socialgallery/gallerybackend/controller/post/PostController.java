package com.socialgallery.gallerybackend.controller.post;

import com.socialgallery.gallerybackend.dto.post.PostRequestDTO;
import com.socialgallery.gallerybackend.model.response.SingleResult;
import com.socialgallery.gallerybackend.service.post.PostService;
import com.socialgallery.gallerybackend.service.response.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"4. post"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    private final ResponseService responseService;

    @ApiOperation(value = "업로드", notes = "게시글 업로드를 합니다.")
    @PostMapping("/post")
    public SingleResult<Long> post(
            @ApiParam(value = "게시글 등록 DTO", required = true)
            @RequestBody PostRequestDTO postRequestDTO) {

        Long pid = postService.post(postRequestDTO);
        return responseService.getSingleResult(pid);
    }

}
